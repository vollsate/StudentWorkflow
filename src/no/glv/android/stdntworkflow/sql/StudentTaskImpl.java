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

	private Date handinDate;
	private int mode;
	
	private String task;
	private String ident;
	private Student student;

	/**
	 * 
	 * @param ident
	 * @param taskName
	 * @param date
	 */
	public StudentTaskImpl( String student, String task, Date date ) {
		this( student, task, MODE_PENDING, date );
	}

	/**
	 * 
	 * @param ident
	 * @param taskName
	 * @param mode
	 * @param date
	 */
	public StudentTaskImpl( String student, String task, int mode, Date date ) {
		this.ident = student;
		this.task = task;
		this.mode = mode;
		this.handinDate = date;
	}

	/**
	 * 
	 * @param student
	 * @param task
	 * @param date
	 */
	public StudentTaskImpl( Student student, String task, Date date ) {
		this( student.getIdent(), task, date);
		this.student = student;
	}
	
	/**
	 * 
	 * @param student
	 * @param task
	 * @param mode
	 * @param date
	 */
	public StudentTaskImpl( Student student, String task, int mode, Date date ) {
		this( student.getIdent(), task, mode, date);
		this.student = student;
	}
	
	public String getIdent() {
		return ident;
	}

	@Override
	public String getTaskName() {
		return task;
	}

	@Override
	public Date getHandInDate() {
		return handinDate;
	}

	@Override
	public void handIn() {
		if ( handinDate != null )
			throw new IllegalStateException( "Student " + student + " has already handed in task " + task );

		handinDate = new Date();
	}
	
	@Override
	public void handIn( int mode ) {
		switch ( mode ) {
		case MODE_HANDIN:
			handIn();
			break;

		case MODE_PENDING:
			handinDate = null;
			break;

		default:
			break;
		}
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
	public Student getStudent() {
		return student;
	}
	
	@Override
	public void setStudent( Student std ) {
		this.student = std;
	}

	@Override
	public String getTask() {
		return task;
	}
}
