package no.glv.android.stdntworkflow.intrfc;

import java.util.Date;

/**
 * Represents the bridge between the {@link Task} and the {@link Student}. A
 * StudentTask will keep track of weather the student has executed the task, and
 * at what date the task was executed.
 * 
 * <p>
 * A comment may be useful if there are a specific message to remember about a
 * certain hand in.
 * 
 * @author GleVoll
 *
 */
public interface StudentTask {

	/** Hand in of a Task */
	public static final int MODE_HANDIN = 0;
	/** The task is not yet finished */
	public static final int MODE_PENDING = 2;
	/** The task has expired */
	public static final int MODE_EXPIRED = 4;
	/** The student handed in the task later than the expiration date */
	public static final int MODE_LATE = 8;

	/**
	 * Gets the comment related to this students task.
	 * 
	 * @return
	 */
	public String getComment();

	/**
	 * 
	 * @param comment The comment to set on this students task.
	 */
	public void setComment( String comment );

	/** PK in database */
	public int getID();

	/**
	 * Get the Student ID. This is the link to the {@link Student} instance that
	 * is a member of this a task.
	 * 
	 * @return
	 */
	public String getIdent();

	/**
	 * The name of the task this Studenttask is a member of.
	 * 
	 * @return
	 */
	public String getTaskName();

	public Date getHandInDate();

	public boolean isHandedIn();

	public void handIn();

	public void handIn( int mode );

	public int getMode();

	public String getModeAsString();

	public Student getStudent();

	public void setStudent( Student std );

	public void setTaskName( String name );

	public int getTaskID();

	public void setTaskID( int id );

	public String toSimpleString();

}
