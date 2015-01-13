package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;

public class TaskImpl implements Task {

	/** TaskImpl.java */
	private static final long serialVersionUID = -5893005256995867042L;
	
	private String mName;
	private String mDesc;
	private Date mExpirationDate;
	private int mType;
	
	private boolean mModified;
	private TreeMap<String, StudentTask> mModifiedStudents;
	
	private int mCount;

	private TreeMap<String, StudentTask> studentsMap;

	/** List of students who has not yet handed in their assignment */
	private TreeMap<String, StudentTask> studentsMapPending;

	private List<String> mClasses;
	
	private List<OnStudentRemovedListener> stdRemListeners;
	
	private List<OnStudentHandInListener> stdHandInListeners;
	
	private List<StudentTask> removedStudents;

	/**
	 * Package protected constructor.
	 */
	TaskImpl() {
		mClasses = new ArrayList<String>();
		studentsMap = new TreeMap<String, StudentTask>();
		studentsMapPending = new TreeMap<String, StudentTask>();
		
		mModified = false;
		mModifiedStudents = new TreeMap<String, StudentTask>();
		
		stdRemListeners = new LinkedList<Task.OnStudentRemovedListener>();
		stdHandInListeners = new LinkedList<Task.OnStudentHandInListener>();
	}
	
	@Override
	public void addOnStudentHandIndListener( OnStudentHandInListener listener ) {
		if ( stdHandInListeners.contains( listener ) ) return;
		
		stdHandInListeners.add( listener );
	}
	
	@Override
	public void addOnStudentRemovedListener( OnStudentRemovedListener listener ) {
		if ( stdRemListeners.contains( listener ) ) return;
		
		stdRemListeners.add( listener );
	}

	@Override
	public void notifyStudentRemoved(Student std ) {
		Iterator<OnStudentRemovedListener> it = stdRemListeners.iterator();
		while ( it.hasNext() ) 
			it.next().onStudentRemove( std );
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
	public boolean handIn( String ident ) {
		return handIn( ident, HANDIN_DATE );
	}

	@Override
	public boolean handIn( String ident, int mode ) {
		StudentTask stdTask = null;
		
		switch ( mode ) {
		case HANDIN_DATE:
			stdTask = studentsMapPending.remove( ident );
			if ( stdTask == null ) return false;

			stdTask.handIn();
			studentsMap.put( ident, stdTask );
			break;

		case HANDIN_CANCEL:
			stdTask = studentsMap.remove( ident );
			if ( stdTask == null ) return false;

			stdTask.handIn( StudentTask.MODE_PENDING );
			studentsMapPending.put( ident, stdTask );
			break;

		default:
			return false;
		}
		
		mModifiedStudents.put( stdTask.getIdent(), stdTask );
		mModified = true;
		
		notifyHandInListeners( stdTask.getStudent() );
		return true;
	}
	
	/**
	 * 
	 * @param std
	 */
	private void notifyHandInListeners( Student std ) {
		Iterator<OnStudentHandInListener> it = stdHandInListeners.iterator();
		while ( it.hasNext() ) it.next().onStudentHandIn( std );
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

		if ( removedStudents == null ) removedStudents = new LinkedList<StudentTask>();
		removedStudents.add( studentsMapPending.remove( ident ) );
		
		mCount--;
		return true;
	}
	
	@Override
	public List<StudentTask> getRemovedStudents() {
		return removedStudents;
	}

	/**
	 * Will only add to students pending
	 * 
	 * @return true if the students gets added successfully, false if not added
	 *         correctly or if the student has already handed in the assignment
	 */
	@Override
	public boolean addStudent( Student std ) {
		if ( std == null ) return false;
		if ( hasStudent( std.getIdent() ) ) return false;

		StudentTask stdTask = new StudentTaskImpl( std, mName, null );
		return addStudentTask( stdTask );
	}

	/**
	 * 
	 * @param stdTask
	 * @return
	 */
	public boolean addStudentTask( StudentTask stdTask ) {
		if ( stdTask == null ) return false;
		if ( hasStudent( stdTask.getIdent() ) ) return false;
		
		if ( stdTask.getMode() == HANDIN_DATE )
			studentsMap.put( stdTask.getIdent(), stdTask );
		else
			studentsMapPending.put( stdTask.getIdent(), stdTask );
		
		mCount++;
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
	
	public int getStudentsPendingCount() {
		return studentsMapPending.size();
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
		while ( it.hasNext() ) {
			addStudentTask( it.next() );
		}
	}

	@Override
	public boolean isStudentsModified() {
		return mModified;
	}

	@Override
	public void markAsUpdated() {
		if (mModifiedStudents != null )
			mModifiedStudents.clear();
		
		if ( removedStudents != null )
			removedStudents.clear();
		
		mModified = false;
	}

	@Override
	public List<StudentTask> getStudentsToUpdate() {
		return new LinkedList<StudentTask>( mModifiedStudents.values() );
	}

	@Override
	public boolean isExpired() {
		Date today = Calendar.getInstance( Locale.getDefault() ).getTime();
		
		return mExpirationDate.before( today );
	}

	@Override
	public int getStudentCount() {
		return mCount;
	}

}
