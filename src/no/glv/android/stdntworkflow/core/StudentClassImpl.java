/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author GleVoll
 *
 */
public class StudentClassImpl implements StudentClass {

	private String mName;

	private ArrayList<Student> students;

	/**
	 * 
	 */
	public StudentClassImpl( String name ) {
		students = new ArrayList<Student>();

		mName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.StudentClass#getName()
	 */
	@Override
	public String getName() {
		return mName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.StudentClass#getSize()
	 */
	@Override
	public int getSize() {
		return students.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.glv.android.stdntworkflow.core.StudentClass#getStudentByFirstName()
	 */
	@Override
	public Student getStudentByFirstName( String name ) {
		Student bean = null;
		Iterator<Student> it = students.iterator();

		while ( it.hasNext() ) {
			bean = it.next();
			if ( bean.getFirstName().equals( name ) ) break;
			else bean = null;
		}

		return bean;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Student getStudentByIdent( String name ) {
		Student bean = null;
		Iterator<Student> it = students.iterator();

		while ( it.hasNext() ) {
			bean = it.next();
			if ( bean.getIdent().equals( name ) ) 
				break;
			else
				bean = null;
		}

		return bean;
	}

	@Override
	public void add( Student std ) {
		students.add( std );
	}

	@Override
	public void addAll( ArrayList<Student> list ) {
		students.addAll( list );
	}

	@Override
	public void addAll( Student[] stds ) {
		for ( int i = 0; i < stds.length; i++ ) {
			students.add( stds[i] );
		}
	}

	@Override
	public Student[] toList() {
		Student[] beans = new Student[students.size()];

		for ( int i = 0; i < students.size(); i++ ) {
			beans[i] = students.get( i );
		}

		return beans;
	}

	@Override
	public Iterator<Student> iterator() {
		return students.iterator();
	}

	@Override
	public List<Student> getStudents() {
		List<Student> list = new ArrayList<Student>( students.size() );
		
		for ( int i = 0; i < students.size(); i++ ) {
			list.add( students.get( i )  );
		}
		
		return list;
	}

}
