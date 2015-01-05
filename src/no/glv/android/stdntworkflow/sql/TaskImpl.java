package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;

public class TaskImpl implements Task {

	private String mName;
	private String mDesc;
	private Date mExpirationDate;
	private int mType;

	private HashMap<String, StudentTask> studentsMap;

	/** List of students who has not yet handed in their assignment */
	private HashMap<String, StudentTask> studentsMapPending;

	private List<String> mClasses;

	/**
	 * Package protected constructor.
	 */
	TaskImpl() {
		mClasses = new ArrayList<String>();
		studentsMap = new HashMap<String, StudentTask>();
		studentsMapPending = new HashMap<String, StudentTask>();
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Date getDate() {
		return mExpirationDate;
	}

	@Override
	public int getType() {
		return mType;
	}

	@Override
	public void setType( int type ) {
		this.mType = type;
	}

	@Override
	public List<String> getStudentsHandedIn() {
		return new ArrayList<String>( studentsMap.keySet() );
	}

	@Override
	public int getStudentsHandedInCount() {
		return studentsMap.size();
	}

	@Override
	public List<String> getClasses() {
		return new ArrayList<String>( mClasses );
	}

	@Override
	public void addClass( StudentClass stdClass ) {
		mClasses.add( stdClass.getName() );

		Iterator<Student> it = stdClass.iterator();
		while ( it.hasNext() ) {
			addStudent( it.next() );
		}

	}

	@Override
	public void setName( String name ) {
		mName = name;
	}

	/**
	 * Removes both the class and every student linked to this class.
	 * 
	 * Will not remove students who has already handed in their assignment
	 */
	@Override
	public void removeClass( StudentClass stdClass ) {
		if ( !mClasses.contains( stdClass.getName() ) ) return;

		mClasses.remove( stdClass.getName() );
		List<Student> list = stdClass.getStudents();
		Iterator<Student> it = list.iterator();
		while ( it.hasNext() ) {
			removeStudent( it.next().getIdent() );
		}

	}

	@Override
	public boolean removeStudent( String ident ) {
		if ( !studentsMapPending.containsKey( ident ) ) return false;

		studentsMapPending.remove( ident );
		return true;
	}

	/**
	 * Will only add to students pending
	 * 
	 * @return true if the students gets added successfully, false if not added
	 *         correctly or if the student has already handed in the assigment
	 */
	@Override
	public boolean addStudent( Student std ) {
		if ( std == null ) return false;
		if (hasStudent( std.getIdent() ) ) return false;
		
		return addStudentTask( new StudentTaskImpl( std.getIdent(), mName, 0, null ) );
	}
	
	/**
	 * 
	 * @param std
	 * @return
	 */
	public boolean addStudentTask( StudentTask std )  {
		if ( std == null ) return false;
		if (hasStudent( std.getIdent() ) ) return false;
		
		studentsMapPending.put( std.getIdent(), std );
		return true;
	}

	@Override
	public boolean hasClass( String className ) {
		return mClasses.contains( className );
	}

	/**
	 * 
	 * @param list
	 * @param ident
	 * @return true if 
	 */
	boolean hasStudent( List<StudentTask> list, String ident ) {
		Iterator<StudentTask> it = list.iterator();
		while ( it.hasNext() ) {
			if ( it.next().getIdent().equals( ident ) ) return true;
		}

		return false;
	}

	@Override
	public boolean hasStudent( String ident ) {
		if ( studentsMap.containsKey( ident ) ) return true;
		if ( studentsMapPending.containsKey( ident ) ) return true;

		return false;
	}

	@Override
	public List<String> getStudentNames() {
		List<String> stds = new ArrayList<String>();

		stds.addAll( studentsMap.keySet() );
		stds.addAll( studentsMapPending.keySet() );

		return stds;
	}

	@Override
	public String getDesciption() {
		return mDesc;
	}

	@Override
	public void setDescription( String desc ) {
		mDesc = desc;
	}

	@Override
	public List<String> getStudentsPending() {
		return new ArrayList<String>( studentsMapPending.keySet() );
	}

	@Override
	public void setDate( Date date ) {
		this.mExpirationDate = date;
	}

	@Override
	public List<StudentTask> getStudentsInTask() {
		List<StudentTask> list = new ArrayList<StudentTask>();
		
		list.addAll( studentsMap.values() );
		list.addAll( studentsMapPending.values() );
		
		return list;
	}

	@Override
	public void addStudents( List<Student> students ) {
		Iterator<Student> it = students.iterator();
		while ( it.hasNext() ) {
			addStudent( it.next() );
		}
	}
	
	@Override
	public void addStudentTasks( List<StudentTask> students ) {
		Iterator<StudentTask> it = students.iterator();
		while (it.hasNext()) {
			addStudentTask( it.next() );
		}
	}

}
