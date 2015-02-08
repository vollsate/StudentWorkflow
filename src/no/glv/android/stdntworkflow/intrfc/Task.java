package no.glv.android.stdntworkflow.intrfc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;

/**
 * This interface supports different types of tasks. A task must have a name and
 * an expiration date. Any classes that is connected to a task, will
 * automatically implement the task for every student in that task. A student
 * MUST be assigned to a task through a class, but certain students may be
 * removed individually from a task later.
 * 
 * A student may be removed individually from a task calling the
 * <tt>removeStudent</tt> method.
 * 
 * <p>
 * Any changes to the task will result in calling any listeners registered in
 * the task. A listener will be called when the following events occur:
 * <blockquote>
 * <ul compact>
 * <li>A student is added
 * <li>A student is updated
 * <li>A student is removed
 * </ul>
 * <p>
 * <ul>
 * <li>A class is added
 * <li>A class is removed
 * <li>A class is updated
 * </ul>
 * </blockquote>
 * 
 * <p>
 * A listener will also be called when the task itself is updated: <blockquote>
 * <ul>
 * <li>The date is changed
 * <li>The name is changed
 * </ul>
 * </blockquote>
 * 
 * <p>
 * A task is handed in (meaning the student is finished with the assignment hand
 * delivers the work to the teacher) using a <tt>mode</tt> parameter.
 * 
 * <p>
 * The hand in date (expiration date) for the task, is only concerned with the
 * day, not minutes or seconds. If a student hands in a task on the same day as
 * the expiration date, that is fine. Any hand ins later than that, will set a
 * flag in the students hand in date telling that the delivery was late.
 * 
 * <p>
 * The task has several states. The <tt>TASK_STATE_OPEN</tt> is the default
 * state, where it is accessible for students handing in work. A task can be in
 * the state of <tt>TASK_STATE_EXPIRED</tt>. In this state it is still
 * accessible for hand ins, but the student who hands in late will be flagged by
 * the task. The state <tt>TASK_STATE_EXPIRED</tt> will be set automatically by
 * the system, and will be set when the todays date is
 * <code>expirationDate + 1</code>.
 * 
 * 
 * @author GleVoll
 */
public interface Task extends Serializable {

	/** The normal flag for handing in a task. */
	public static final int HANDIN_DATE = 0;
	/** The flag for students who hands in after the tasks expiration date. */
	public static final int HANDIN_LATE = 1;
	/** The student has been sick, and therefore hands in late */
	public static final int HANDIN_SICK = 128;
	/** The student has been away for some reason, and therefore hands in late */
	public static final int HANDIN_AWAY = 256;
	/** The hand in is canceled. May be done by the teacher later on. */
	public static final int HANDIN_CANCEL = 512;

	/** The task is open for handing in. This is the default state */
	public static final int TASK_STATE_OPEN = 1;
	/** The task is closed for hand in */
	public static final int TASK_STATE_CLOSED = 2;
	/** The task has expired, but still accepts hand ins */
	public static final int TASK_STATE_EXPIRED = 4;

	/** The name of the extra parameter when stored on instance save */
	public static final String EXTRA_TASKNAME = BaseValues.EXTRA_BASEPARAM + "task";

	/**
	 * 
	 * @return
	 */
	public int getID();

	/**
	 * 
	 * @return The name of the task
	 */
	public String getName();

	/**
	 * Sets the name of the task
	 * 
	 * @param name
	 */
	public void setName( String name );

	/**
	 * 
	 * @return
	 */
	public Date getDate();

	/**
	 * Sets the new expiration date for this task.
	 * 
	 * @param date
	 */
	public void setDate( Date date );

	/**
	 * 
	 * @return The current state of the task
	 */
	public int getState();
	
	/**
	 * 
	 * @return
	 */
	public String getStateAsString();

	/**
	 * 
	 * @param newState The new state of the task
	 */
	public void setState( int newState );

	/**
	 * 
	 * @return The tasks description. May be null
	 */
	public String getDesciption();

	/**
	 * 
	 * @param desc The tasks description. May be null
	 */
	public void setDescription( String desc );

	/**
	 * Get the subject this class is related to
	 * 
	 * @return
	 */
	public int getSubject();

	public void setSubject( int subject );

	public int getType();

	public void setType( int type );

	/**
	 * @return The number of Student who has handed in this task
	 */
	public int getStudentsHandedInCount();

	/**
	 * A student is handing in the task. This is the default hand in method. If
	 * the student is on time, the hand in will be flagged as
	 * <tt>HANDIN_DATE</tt>. If the hand in is overdue, the flag
	 * <tt>HANDIN_LATE</tt> is set on the student.
	 * 
	 * @param ident The unique identifier of a {@link Student}
	 * @return <code>true</code> if handed in successfully.
	 */
	public boolean handIn( String ident );

	/**
	 * Hand in a students task with a specific mode.
	 * 
	 * @param ident The student ID.
	 * @param mode The mode.
	 * @return <code>true</code> if handed in successfully.
	 */
	public boolean handIn( String ident, int mode );

	/**
	 * Checks to see if there are any changes to the task. A modification occurs
	 * when a student hands in, is added or is removed.
	 * 
	 * @return <code>true</code> if any of the students linked to this task is
	 *         modified.
	 */
	public boolean isModified();

	/**
	 * Resets the task after a DB commit.
	 * 
	 * <p>
	 * Must only be called after the task has been committed to the database.
	 * Any history the task contains for keeping track with adding, removal and
	 * updating is lost when this is called.
	 */
	public void markAsCommitted();

	/**
	 * This list will contain only students who has recently been added to the
	 * task. Any students already registered to the task will not be contained
	 * within this list.
	 * 
	 * <p>
	 * Make sure to use the <tt>DataHandler.commitTask</tt> to commit the task
	 * to the database.
	 * 
	 * @return A list of {@link StudentTask} instances that has been added to
	 *         the task after its latest commit.
	 */
	public List<StudentTask> getAddedStudents();

	/**
	 * This list will contain only the students who has been updated since the
	 * last commit to the database.
	 * 
	 * @return A list of newly updated students.
	 */
	public List<StudentTask> getUpdatedStudents();

	/**
	 * This list will only contain the newly added classes. Any classes
	 * committed to the database will not be contained in this list.
	 * 
	 * @return
	 */
	public List<String> getAddedClasses();

	/**
	 * This list will only contain the classes that has recently been removed
	 * from the task.
	 * 
	 * @return
	 */
	public List<String> getRemovedClasses();

	/**
	 * This list will not differentiate in the hand in state of the student.
	 * Both <tt>HANDIN_DATE</tt> and <tt>HANDIN_LATE</tt> will be returned here.
	 * 
	 * @return A list of student IDs how has handed in the assignment.
	 */
	public List<StudentTask> getStudentsHandedIn();

	/**
	 * The list of students pending to hand in. This list will not differentiate
	 * on the state of the student.
	 * 
	 * @return
	 */
	public List<StudentTask> getStudentsPending();

	/**
	 * 
	 * @return How many students has not yet handed in the task.
	 */
	public int getStudentsPendingCount();

	/**
	 * This will return the current value, even if the task is not yet
	 * committed.
	 * 
	 * @return How many students are involved in this task
	 */
	public int getStudentCount();

	/**
	 * 
	 * @return A list of the current classes registered with this task.
	 */
	public List<String> getClasses();

	/**
	 * Adds a {@link StudentClass} to the task. Every {@link Student} in this
	 * class will be automatically assigned to the task, unless some of them are
	 * removed.
	 * 
	 * <p>
	 * A reference to the {@link StudentClass} is not held within the Task.
	 * 
	 * @param stdClass
	 */
	public void addClass( StudentClass stdClass );

	/**
	 * Adds a class by name
	 * 
	 * @param stdClass
	 */
	public void addClassName( String stdClass );

	/**
	 * Removes an entire class from the task. Any student registered within this
	 * class, will be automatically removed from the task. Any students that has
	 * handed in the task, will also be removed.
	 * 
	 * <p>
	 * This cannot be undone after a commit!
	 * 
	 * @param stdClass
	 */
	public void removeClass( StudentClass stdClass );

	/**
	 * This method will only remove the student from the ones who have not
	 * handed in the assignment (task).
	 * 
	 * <p>
	 * The task will be in the state of <tt>modified</tt> and the removed
	 * student will be moved to the tasks history as an removed student. Commit
	 * the change to the database before calling <tt>markAsCommitted</tt>
	 * 
	 * @param ident The identity of the Student
	 * @return true if the Student was removed
	 */
	public boolean removeStudent( String ident );

	/**
	 * Adds a {@link Student} to the task. The student will be set to the state
	 * of <tt>PENDING</tt>
	 * 
	 * <p>
	 * The student will not be registered in the database. This must be done
	 * through the {@link DataHandler}.
	 * 
	 * @param ident The ID of the student.
	 * @return <code>true</code> if successfully added.
	 */
	public boolean addStudent( Student ident );

	/**
	 * 
	 * @param className Name of class to check for.
	 * @return <code>true</code> if Task has this class registered.
	 */
	public boolean hasClass( String className );

	/**
	 * Checks to see if the task has a certain student registered within the
	 * task. The task will check every student, and not differentiate between
	 * different classes.
	 * 
	 * @param ident The ID of the student.
	 * @return <code>true</code> if student is registered in task.
	 */
	public boolean hasStudent( String ident );

	/**
	 * 
	 * @return A list containing all the Student IDs registered in the class.
	 */
	public List<String> getStudentNames();

	/**
	 * A list of all the students registered in the task. Will not differentiate
	 * between different states of the student.
	 * 
	 * @return
	 */
	public List<StudentTask> getStudentsInTask();

	/**
	 * Adds a list of students to the task. Every {@link Student} in this list
	 * will have it's own {@link StudentTask} instance created.
	 * 
	 * <p>
	 * The task must be committed to the DB.
	 * 
	 * @param students
	 */
	public void addStudents( List<Student> students );

	/**
	 * Adds a list of already prepared {@link StudentTask} instances. Usually as
	 * a result of a database query.
	 * 
	 * @param students
	 */
	public void addStudentTasks( List<StudentTask> students );

	/**
	 * Checks to see if the task has expired. Returns true, only if the todays
	 * date is <code>expirationDate + 1</code>.
	 * 
	 * @return <code>true</code> if the task has expired.
	 */
	public boolean isExpired();

	public void addOnTaskChangeListener( OnTaskChangeListener listener );

	public void removeOnTaskChangeListener( OnTaskChangeListener listener );

	public void notifyChange( int mode );

	public void notifyChange();

	/**
	 * Gets a list of all the students removed from the task. Remember to commit
	 * the task through the {@link DataHandler}.
	 * 
	 * @return
	 */
	public List<StudentTask> getRemovedStudents();

	/**
	 * A callback interface used when a the following updates happen to the
	 * task:
	 * 
	 * <blockquote>
	 * <ul>
	 * <li>Date is changed
	 * <li>Name is changed
	 * <li>Description has changed
	 * <li>A student is added, deleted, updated or hands in
	 * <li>A class is added, deleted or updated
	 * </ul>
	 * </blockquote>
	 * 
	 * @author glevoll
	 */
	public static interface OnTaskChangeListener {

		public static final int MODE_DATE_CHANGE = 101;
		public static final int MODE_NAME_CHANGE = 102;
		public static final int MODE_DESC_CHANGE = 103;

		public static final int MODE_STD_ADD = 104;
		public static final int MODE_STD_DEL = 105;
		public static final int MODE_STD_UPD = 106;
		public static final int MODE_STD_HANDIN = 107;

		public static final int MODE_CLS_ADD = 108;
		public static final int MODE_CLS_DEL = 109;
		public static final int MODE_CLS_UPD = 110;
		
		public static final int MODE_TASK_SORT = 111;

		/**
		 * A callback method to inform that a task has changed in's state
		 * somehow. Triggered by a call to the {@link
		 * Task.notifyTaskChange(int)}
		 * 
		 * @param task
		 * @param mode
		 */
		public void onTaskChange( Task task, int mode );
	}

}
