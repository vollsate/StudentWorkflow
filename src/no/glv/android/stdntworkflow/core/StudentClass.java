package no.glv.android.stdntworkflow.core;

import java.util.ArrayList;

/**
 * This class holds all the student classes in the system.
 * 
 * @author GleVoll
 *
 */
public interface StudentClass  {
	
	public static final String EXTRA_STUDENTCLASS = BaseValues.EXTRA_BASEPARAM + StudentClass.class.getSimpleName();
	
	
	public String getName();
	
	public int getSize();
	
	public Student getStudentByFirstName(String name);
	
	public void add( Student std );
	
	public void addAll( ArrayList<Student> list );
	
	public void addAll (Student[] stds );
	
	public Student[] toList();

}
