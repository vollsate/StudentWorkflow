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

    private int ID;
    private String mName;
    private String mDesc;
    private Date mExpirationDate;
    private int mType;

    private boolean mModified;
    private TreeMap<String, StudentTask> mModifiedStudents;

    private int mCount;

    /** The map containing the students who has finished the task */
    private TreeMap<String, StudentTask> studentsMap;

    /** List of students who has not yet handed in their assignment */
    private TreeMap<String, StudentTask> studentsMapPending;

    private List<String> mClasses;

    private List<OnStudentRemovedListener> stdRemListeners;

    private List<OnStudentHandInListener> stdHandInListeners;

    private List<OnStudentAddListener> stdAddListeners;

    private List<StudentTask> removedStudents;

    private List<StudentTask> addedStudents;

    private List<String> addedClasses;

    private List<String> removedClasses;

    /**
     * Package protected constructor.
     */
    TaskImpl() {
	mClasses = new ArrayList<String>();
	studentsMap = new TreeMap<String, StudentTask>();
	studentsMapPending = new TreeMap<String, StudentTask>();

	mModified = false;
	mModifiedStudents = new TreeMap<String, StudentTask>();
	removedStudents = new LinkedList<StudentTask>();
	addedClasses = new LinkedList<String>();
	removedClasses = new LinkedList<String>();

	stdRemListeners = new LinkedList<Task.OnStudentRemovedListener>();
	stdHandInListeners = new LinkedList<Task.OnStudentHandInListener>();
	stdAddListeners = new LinkedList<Task.OnStudentAddListener>();
    }

    @Override
    public void addOnStudentHandIndListener( OnStudentHandInListener listener ) {
	if ( stdHandInListeners.contains( listener ) ) return;

	stdHandInListeners.add( listener );
    }

    @Override
    public void removeOnStudentHandInListener( OnStudentHandInListener listener ) {
	if ( !stdHandInListeners.contains( listener ) ) return;

	stdHandInListeners.remove( listener );
    }

    @Override
    public void addOnStudentRemovedListener( OnStudentRemovedListener listener ) {
	if ( stdRemListeners.contains( listener ) ) return;

	stdRemListeners.add( listener );
    }

    @Override
    public void removeOnStudentRemovedListener( OnStudentRemovedListener listener ) {
	if ( !stdRemListeners.contains( listener ) ) return;

	stdRemListeners.remove( listener );
    }

    @Override
    public void addOnStudentAddListener( OnStudentAddListener listener ) {
	if ( stdAddListeners.contains( listener ) ) return;

	stdAddListeners.add( listener );
    }

    @Override
    public void removeOnStudentAddListener( OnStudentAddListener listener ) {
	if ( !stdAddListeners.contains( listener ) ) return;

	stdAddListeners.remove( listener );
    }

    public void notifyStudentRemoved( Student std ) {
	Iterator<OnStudentRemovedListener> it = stdRemListeners.iterator();
	while ( it.hasNext() )
	    it.next().onStudentRemove( std );
    }

    public void notifyStudentAdd( Student std ) {
	Iterator<OnStudentAddListener> it = stdAddListeners.iterator();
	while ( it.hasNext() )
	    it.next().onStudentAdd( std );
    }

    /**
     * 
     */
    public void notifyChange( int mode ) {
	Iterator<StudentTask> it = null;
	
	if ( removedStudents != null ) {
	    it = removedStudents.iterator();
	    while ( it.hasNext() ) {
		notifyStudentRemoved( it.next().getStudent() );
	    }
	}

	if ( addedStudents != null ) {
	    it = addedStudents.iterator();
	    while ( it.hasNext() ) {
		notifyStudentAdd( it.next().getStudent() );
	    }
	}
    }

    @Override
    public int getID() {
	return ID;
    }

    @Override
    public void setID( int id ) {
	this.ID = id;
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
	while ( it.hasNext() )
	    it.next().onStudentHandIn( std );
    }

    @Override
    public List<String> getClasses() {
	return new ArrayList<String>( mClasses );
    }

    @Override
    public void addClass( StudentClass stdClass ) {
	addClassName( stdClass.getName() );
	Iterator<Student> it = stdClass.iterator();
	while ( it.hasNext() ) {
	    addStudent( it.next() );
	}

    }

    @Override
    public void addClassName( String stdClass ) {
	if ( mClasses.contains( stdClass ) ) return;

	mClasses.add( stdClass );
	addedClasses.add( stdClass );
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
    
    @Override
    public List<String> getAddedClasses() {
        return addedClasses;
    }
    
    @Override
    public List<String> getRemovedClasses() {
        return removedClasses;
    }

    /**
     * Will only add to students pending
     * 
     * @return true if the students gets added successfully, false if not added correctly or if the student has already
     *         handed in the assignment
     */
    @Override
    public boolean addStudent( Student std ) {
	if ( std == null ) return false;
	if ( hasStudent( std.getIdent() ) ) return false;

	StudentTask stdTask = new StudentTaskImpl( std, mName, null );

	if ( removedStudents.contains( std.getIdent() ) ) removedStudents.remove( std.getIdent() );

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

	if ( stdTask.getMode() == HANDIN_DATE ) studentsMap.put( stdTask.getIdent(), stdTask );
	else
	    studentsMapPending.put( stdTask.getIdent(), stdTask );

	if ( addedStudents == null ) addedStudents = new LinkedList<StudentTask>();
	addedStudents.add( stdTask );
	
	notifyStudentAdd( stdTask.getStudent() );

	mCount++;
	return true;
    }

    @Override
    public List<StudentTask> getAddedStudents() {
	return addedStudents;
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
	if ( mModifiedStudents != null ) mModifiedStudents.clear();
	if ( removedStudents != null ) removedStudents.clear();
	if ( addedStudents != null ) addedStudents.clear();
	
	addedClasses.clear();
	removedClasses.clear();

	mModified = false;
    }

    @Override
    public List<StudentTask> getUpdatedStudents() {
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
