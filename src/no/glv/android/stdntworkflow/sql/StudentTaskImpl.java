package no.glv.android.stdntworkflow.sql;

import java.util.Date;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentTask;

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

    private String mTask;
    private String mIdent;
    private Student student;

    /**
     * 
     * @param mIdent
     * @param taskName
     * @param date
     */
    public StudentTaskImpl( String student, String task, Date date ) {
	this( student, task, MODE_PENDING, date );
    }

    /**
     * 
     * @param mIdent
     * @param taskName
     * @param mode
     * @param date
     */
    public StudentTaskImpl( String student, String task, int mode, Date date ) {
	this.mIdent = student;
	this.mTask = task;
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
	this( student.getIdent(), task, date );
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
	this( student.getIdent(), task, mode, date );
	this.student = student;
    }

    public String getIdent() {
	return mIdent;
    }

    @Override
    public String getTaskName() {
	return mTask;
    }

    @Override
    public Date getHandInDate() {
	return handinDate;
    }

    @Override
    public void handIn() {
	handIn( MODE_HANDIN );
    }

    @Override
    public void handIn( int mode ) {
	this.mode = mode;

	switch ( mode ) {
	case MODE_HANDIN:
	    if ( handinDate == null ) handinDate = new Date();
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
	return ( handinDate != null );
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
    public void setTaskName( String name ) {
	this.mTask = name;
    }
}
