package no.glv.android.stdntworkflow.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

public class TaskHandler {
	
	private static TaskHandler instance;
	
	private HashMap<String, Task> tasks;

	private TaskHandler() {
		tasks = new HashMap<String, Task>();
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Task getTask(String name) {
		return tasks.get( name );
	}
	
	
	public Task createTask() {
		return new TaskImpl();
	}
	
	public List<String> getTasks() {
		List<String> list = new ArrayList<String>();
		list.addAll( tasks.keySet() );
		
		return list;
	}
	
	
	
	/**
	 * 
	 * @param task
	 */
	public TaskHandler addTask(Task task ) {
		if ( task == null ) 
			throw new NullPointerException( "Task to add cannot be NULL!" );
		
		if ( task.getName() == null ) 
			throw new NullPointerException( "Name of task cannot be NULL!" );
		
		if ( tasks.containsKey( task.getName() ))
			throw new IllegalArgumentException( "Task " + task.getName() + " already exists" );
		
		tasks.put( task.getName(), task );
		return this;
	}
	
	public void commit(Activity activity) {
		Iterator<Task> it = tasks.values().iterator();
		while ( it.hasNext() ) {
			Task task = it.next();
			LoadDataHandler.WriteTask( activity, task );
		}
	}
	

	/**
	 * 
	 * @return
	 */
	public static TaskHandler GetInstance() {
		if (instance == null) instance = new TaskHandler();
		
		return instance;
	}

}
