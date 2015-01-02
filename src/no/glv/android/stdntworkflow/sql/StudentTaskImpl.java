package no.glv.android.stdntworkflow.sql;

import java.util.Date;

import no.glv.android.stdntworkflow.intrfc.StudentTask;

/**
 * Keeps track of every student registered and every task they are currently
 * engaged in.
 * 
 * @author GleVoll
 *
 */
public class StudentTaskImpl implements StudentTask {
	
	private String ident;
	
	private String task;

	public StudentTaskImpl(String ident, String task) {
		this.ident = ident;
		this.task = task;
	}
	
	
	public String getIdent() {
		return ident;
	}
	
	
	@Override
	public String getTask() {
		return task ;
	}


	@Override
	public Date getHandInDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
