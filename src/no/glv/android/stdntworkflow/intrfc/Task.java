package no.glv.android.stdntworkflow.intrfc;

import java.util.Date;
import java.util.List;

/**
 * This interface supports different types of tasks. A task must have a name and
 * an expiration date. Any classes that is connected to a task, will
 * automatically implement the task for every student in that task.
 * 
 * TODO: - Implement how to remove certain students from a task
 * 
 * @author GleVoll
 *
 */
public interface Task {
	
	public static final String PROP_NAME = "NAME";
	public static final String PROP_DESC = "DESC";
	public static final String PROP_CLASS = "CLASSES";
	public static final String PROP_STDNT = "STUDENT";
	public static final String PROP_STDNT_PEND = "STUDENT_PEND";
	public static final String PROP_TYPE = "TYPE";
	public static final String PROP_DATE = "DATE";

	public String getName();

	public void setName( String name );

	public Date getDate();

	public int getType();
	
	public void setType( int type );

	public String getDesciption();

	public void setDescription( String desc );

	/**
	 * 
	 * @return The number of Student who has handed in the assignment
	 */
	public int getStudentsHandedInCount();

	/**
	 * 
	 * @return A list of Student how has handed in the assignment.
	 */
	public List<String> getStudentsHandedIn();

	public List<String> getStudentsPending();

	public List<String> getClasses();

	public void addClass( StudentClass stdClass );

	public void removeClass( StudentClass stdClass );
	
	public void setDate( Date date );
	

	/**
	 * This method will only remove the student from the ones who have not
	 * handed in the assigment (task).
	 * 
	 * @param ident
	 *            The identity of the Student
	 * @return true if the Student was removed
	 */
	public boolean removeStudent( String ident );

	public boolean addStudent( Student ident );

	public boolean hasClass( String className );

	public boolean hasStudent( String ident );

	public List<String> getStudents();

}
