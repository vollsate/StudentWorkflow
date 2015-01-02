package no.glv.android.stdntworkflow.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.util.Log;

public class StudentClassHandler {
	
	private static final String TAG = StudentClassHandler.class.getSimpleName();
	
	private static StudentClassHandler instance;
	
	private HashMap<String, StudentClass> classes;
	
	private List<OnStudentClassChangeListener> listeners;
	

	/**
	 * 
	 */
	StudentClassHandler() {
		classes = new HashMap<String, StudentClass>( 3 );
		listeners = new ArrayList<StudentClassHandler.OnStudentClassChangeListener>(3);
	}
	
	
	public Student getStudentById( String ident ) {
		Student std = null;
		
		Iterator<String> it = classes.keySet().iterator();
		while (it.hasNext()) {
			StudentClass stdClass = classes.get( it.next() );
			std = stdClass.getStudentByIdent( ident );
			
			if (std != null ) break;
		}
		
		return std;
	}
	
	
	/**
	 * 
	 * @param listener
	 */
	public void setOnStudentClassChangeListener( OnStudentClassChangeListener listener ) {
		listeners.add( listener );
	}
	
	
	private void notifyListeners(StudentClass stdClass ) {
		Iterator<OnStudentClassChangeListener> it = listeners.iterator();
		
		while (it.hasNext() ) it.next().onStudentClassChange( stdClass );
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
		notifyListeners( stdClass );
	}
	
	
	public StudentClass removeStudentClass( String className ) {
		if ( ! classes.containsKey( className ) ) return null;
		
		StudentClass stClass = classes.remove( className );
		notifyListeners( stClass );
		
		return stClass;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public StudentClass getStudentClass( String name ) {
		return ( StudentClass ) classes.get( name );
	}
	
	
	/**
	 * 
	 * @param stdClass
	 * @return
	 */
	public List<String> getStudentIdentsInClass( StudentClass stdClass ) {
		ArrayList<String> list = new ArrayList<String>();
	
		Iterator<Student> it = stdClass.iterator();
		while ( it.hasNext() ) {
			list.add( it.next().getIdent() );
		}
		
		return list;
	}
	


	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnStudentClassChangeListener {
		
		public void onStudentClassChange( StudentClass stdClass );
	}
	
}

