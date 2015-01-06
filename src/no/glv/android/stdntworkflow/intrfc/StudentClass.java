package no.glv.android.stdntworkflow.intrfc;

import java.util.Iterator;
import java.util.List;

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

	public Student getStudentByIdent(String name);
	
	public void add( Student std );
	
	public void addAll( List<Student> list );
	
	public void addAll (Student[] stds );
	
	public Student[] toArray();
	
	public Iterator<Student> iterator();
	
	public List<Student> getStudents();

}
