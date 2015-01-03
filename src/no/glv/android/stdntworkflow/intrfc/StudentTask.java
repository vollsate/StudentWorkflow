package no.glv.android.stdntworkflow.intrfc;

import java.util.Date;

/**
 * 
 * @author GleVoll
 *
 */
public interface StudentTask {
	
	public static final int MODE_HANDIN = 0;
	public static final int MODE_PENDING = 2;
	public static final int MODE_EXPIRED = 4;
	public static final int MODE_LATE = 8;

	public String getIdent();
	
	public String getTaskName();
	
	public Date getHandInDate();
	
	public boolean isHandedIn();
	
	public void handIn();
	
	public int getMode();
	
	public void setTask( Task task );
	public Task getTask();
	
	public void setStudent( Student student );
	public Student getStudent();
	

}