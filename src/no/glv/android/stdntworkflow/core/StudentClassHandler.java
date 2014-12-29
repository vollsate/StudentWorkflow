package no.glv.android.stdntworkflow.core;

import java.util.HashMap;

import android.util.Log;

public class StudentClassHandler {
	
	private static final String TAG = StudentClassHandler.class.getSimpleName();
	
	private static StudentClassHandler instance;
	
	private HashMap<String, StudentClass> classes;
	

	/**
	 * 
	 */
	private StudentClassHandler() {
		classes = new HashMap<String, StudentClass>( 3 );
	}
	
	/**
	 * 
	 * @return
	 */
	public static StudentClassHandler GetInstance() {
		if (instance == null)
			instance = new StudentClassHandler();
		
		return instance;
	}

	
	/**
	 * 
	 * @param stdClass
	 */
	public void addStudentClass( StudentClass stdClass ) {
		if (stdClass == null ) {
			Log.e( TAG, "addStudentClass(): stdClass cannot be null" );
			return;
		}
		
		classes.put( stdClass.getName(), stdClass );
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public StudentClass getStudentClass( String name ) {
		return ( StudentClass ) classes.get( name );
	}
}

