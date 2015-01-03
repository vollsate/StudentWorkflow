package no.glv.android.stdntworkflow.sql;

import java.util.Date;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;

/**
 * Keeps track of every student registered and every task they are currently
 * engaged in.
 * 
 * @author GleVoll
 *
 */
public class StudentTaskImpl implements StudentTask {

	private String ident;
	private String taskName;
	private Date handinDate;
	private int mode;

	private Task task;
	private Student student;

	public StudentTaskImpl( String ident, String taskName, int mode, Date date ) {
		this.ident = ident;
		this.taskName = taskName;
		this.mode = mode;
		this.handinDate = date;
	}

	public String getIdent() {
		return ident;
	}

	@Override
	public String getTaskName() {
		return taskName;
	}

	@Override
	public Date getHandInDate() {
		return handinDate;
	}

	@Override
	public void handIn() {
		if ( handinDate != null )
			throw new IllegalStateException( "Student " + ident + " has already handed in task " + taskName );

		handinDate = new Date();
		mode = MODE_HANDIN;
	}

	@Override
	public int getMode() {
		return mode;
	}

	@Override
	public boolean isHandedIn() {
		return (handinDate != null);
	}

	@Override
	public void setTask( Task task ) {
		if ( !task.getName().equals( taskName ) )
			throw new IllegalStateException( "Task in task [" + taskName + "] and setTask [" + task.getName()
					+ "] does not match" );
		
		this.task = task;
	}

	@Override
	public Task getTask() {
		return task;
	}

	@Override
	public void setStudent( Student student ) {
		if ( !student.getIdent().equals( ident ) )
			throw new IllegalStateException( "Student in task [" + ident + "] and setStudent [" + student.getIdent()
					+ "] does not match" );

		this.student = student;
	}

	@Override
	public Student getStudent() {
		return student;
	}

}
