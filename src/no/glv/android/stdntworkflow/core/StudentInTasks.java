package no.glv.android.stdntworkflow.core;

import java.util.List;

/**
 * Keeps track of every student registered and every task they are currently
 * engaged in.
 * 
 * @author GleVoll
 *
 */
public class StudentInTasks {
	
	private String ident;
	
	private List<String> tasks;

	public StudentInTasks(String ident, List<String> tasks) {
		this.ident = ident;
		this.tasks = tasks;
	}
	
	
	public String getIdent() {
		return ident;
	}
	
	
	/**
	 * 
	 * 
	 * @param ident The ident of a Student
	 * @return A List of every task the student has not yet handed in
	 */
	public List<String> getEngagedTasks() {
		return tasks;
	}

}
