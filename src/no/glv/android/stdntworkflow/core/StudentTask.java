package no.glv.android.stdntworkflow.core;

import java.util.Date;
import java.util.List;

/**
 * This interface supports different types of tasks. A task must have a name and
 * an expiration date. Any classes that is connected to a task, will
 * automatically implement the task for every student in that task.
 * 
 * TODO: 
 * 		- Implement how to remove certain students from a task 
 * 
 * @author GleVoll
 *
 */
public interface StudentTask {
	
	public String getName();
	
	public Date getEpirationDate();
	
	public String getStudentClassName();
	
	public int getTaskType();
	
	public List<Student> getStudentsDelivered();
	
	public int getStudentsDeliveredCount();

}
