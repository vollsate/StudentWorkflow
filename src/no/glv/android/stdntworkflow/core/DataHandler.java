package no.glv.android.stdntworkflow.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import no.glv.android.stdntworkflow.R;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.SubjectType;
import no.glv.android.stdntworkflow.intrfc.Task;
import no.glv.android.stdntworkflow.intrfc.Task.OnTaskChangeListener;
import no.glv.android.stdntworkflow.sql.DBUtils;
import no.glv.android.stdntworkflow.sql.Database;
import no.glv.android.stdntworkflow.sql.ParentBean;
import no.glv.android.stdntworkflow.sql.PhoneBean;
import no.glv.android.stdntworkflow.sql.StudentBean;
import no.glv.android.stdntworkflow.sql.StudentClassImpl;
import no.glv.android.stdntworkflow.sql.StudentTaskImpl;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * This class is the hub of the application. Every operation on any date MUST be
 * done through this class.
 * 
 * <p>
 * The SQL package is very important. The {@link Database} is the hub for
 * communicating with the database. The SQL package also implements the
 * interface package <code>(.intrfc)</code>.
 * 
 * <p>
 * Any changes to the data structure is logged and maintained by this class.
 * That means that listeners are the way to keep up with any changes. Take note
 * that the {@link Task} class also provide a listener interface:
 * {@link OnTaskChangeListener}.
 * 
 * <p>
 * 
 * 
 * @author GleVoll
 *
 */
public class DataHandler {

	private static final String TAG = DataHandler.class.getSimpleName();

	public static final int MODE_RESETDB = Integer.MAX_VALUE;

	private static final String STUDENT_IN_TASK_FILENAME = "stdntsk.glv";
	private static final String STUDENT_IN_TASK_SEP = "=";
	private static final String STUDENT_PROPERTY_SEP = ";";
	private static final String STUDENT_IN_TASK_DELIM = ",";

	private static String STDCLASS_FILE_SUFFIX = ".csv";

	/** A map of all the tasks the students are involved in */
	private static HashMap<String, StudentTaskImpl> studentInTasks;

	private static boolean islocalStudentClassesLoaded = false;

	private final Database db;
	private final SettingsManager sManager;

	private Application mApp;

	/** All the loaded classes from the database */
	private TreeMap<String, StudentClass> stdClasses;

	/** All the loaded tasks from the database */
	private TreeMap<String, Task> tasks;

	private TreeMap<String, SubjectType> mTaskSubjects;
	private TreeMap<String, SubjectType> mTaskTypes;

	/** Singleton instance */
	private static DataHandler instance;
	private static boolean isInitiated = false;

	// Listeners
	private HashMap<String, OnTasksChangedListener> taskChangeListeners;
	private HashMap<String, OnStudentClassChangeListener> stdClassChangeListeners;
	private HashMap<String, OnStudentChangedListener> stdChangeListeners;

	/**
	 * 
	 * @return
	 * @throws IllegalStateException if {@link #Init(Application)} has not been
	 *             called first!
	 */
	public static final DataHandler GetInstance() {
		if ( !isInitiated )
			throw new IllegalStateException( "DataHandler not inititated" );

		return instance;
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// INIT
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Will initiate the DataHandler. If already initiated, the method will
	 * return quietly.
	 * 
	 * The Context parameter is used to initialize the database. The
	 * {@link Database} will only allow one instance, so this needs to work
	 * properly or an {@link IllegalStateException} is thrown.
	 * 
	 * <p>
	 * Any open tasks will be loaded, and every known {@link StudentClass} will
	 * be loaded.
	 * 
	 * @param app
	 * @return The singleton instance.
	 */
	public static final DataHandler Init( Application app ) {
		if ( isInitiated )
			return instance;

		if ( instance == null )
			instance = new DataHandler( new Database( app ), app );

		instance.loadStudentClasses();
		instance.loadTasks();
		instance.loadSubjectTypes();

		isInitiated = true;
		return instance;
	}

	/**
	 * 
	 * @param db
	 */
	private DataHandler( Database db, Application app ) {
		this.db = db;

		mApp = app;
		sManager = new SettingsManager( app );
		initiateMaps();
		initiateListeners();
	}

	/**
	 * This will reset (delete and recreate) the database. MUST be called with
	 * caution.
	 */
	public void resetDB() {
		Log.d( TAG, "resetting database" );

		db.runCreate();
		initiateMaps();
		initSubjectTypes();

		notifyStudentClassChange( null, OnStudentClassChangeListener.MODE_DEL );
		notifyTaskChange( null, OnTasksChangedListener.MODE_DEL );
	}

	/**
	 * Will initiate all the listener maps.
	 */
	private void initiateListeners() {
		stdClassChangeListeners = new HashMap<String, DataHandler.OnStudentClassChangeListener>( 2 );
		stdChangeListeners = new HashMap<String, DataHandler.OnStudentChangedListener>( 2 );
		taskChangeListeners = new HashMap<String, DataHandler.OnTasksChangedListener>( 2 );
	}

	/**
	 * Will initiate the Maps used to contain the DB. Called initially from the
	 * constructor.
	 * 
	 * If {@link resetDB} is called, all the Maps will be reset by an invocation
	 * of this method.
	 */
	private void initiateMaps() {
		stdClasses = new TreeMap<String, StudentClass>();
		tasks = new TreeMap<String, Task>();

		mTaskSubjects = new TreeMap<String, SubjectType>();
		mTaskTypes = new TreeMap<String, SubjectType>();
	}

	/**
	 * Loads the tasks from the DB and and initialize every task with it's
	 * corresponding {@link StudentTask} instance.
	 * 
	 * The loadStudentClasses() metod MUST be called first.
	 * 
	 * <blockquote>
	 * <ul>
	 * <li>Load every task
	 * <li>Load every corresponding StudentTask
	 * <li>Fill the StudentTask with the Student instance
	 * </ul>
	 * </blockquote>
	 */
	private void loadTasks() {
		if ( stdClasses.size() == 0 )
			return;

		List<Task> list = db.loadTasks();
		Iterator<Task> it = list.iterator();

		while ( it.hasNext() ) {
			Task task = it.next();
			List<StudentTask> stdTasks = db.loadStudentsInTask( task );
			// Make sure the StudentTask is properly set up.
			setUpStudentTask( task, stdTasks );

			task.addStudentTasks( stdTasks );
			task.markAsCommitted();
			tasks.put( task.getName(), task );
		}
	}

	/**
	 * Fills the {@link StudentTask} with the corresponding {@link Student}
	 * instance and the corresponding {@link Task} data.
	 * 
	 * The complete list will be sorted by the default listing: ident ascending.
	 * 
	 * @param task The task the StudentTask instance is connected to.
	 * @param stdTasks
	 */
	private void setUpStudentTask( Task task, List<StudentTask> stdTasks ) {
		Iterator<StudentTask> it = stdTasks.iterator();
		while ( it.hasNext() ) {
			StudentTask stdTask = it.next();
			stdTask.setStudent( getStudentById( stdTask.getIdent() ) );

			String stdClass = stdTask.getStudent().getStudentClass();
			task.addClassName( stdClass );
		}

		// Sort the list
		int sortType = getSettingsManager().getStudentClassSortType();
		Collections.sort( stdTasks, new DataComparator.StudentTaskComparator( sortType ) );
	}

	/**
	 * Loads all the {@link StudentClass} found in the DB. All the instances is
	 * filled with the corresponding {@link Student} instance.
	 */
	private void loadStudentClasses() {
		List<StudentClass> list = db.loadStudentClasses();
		Iterator<StudentClass> it = list.iterator();
		while ( it.hasNext() ) {
			StudentClass stdClass = it.next();
			populateStudentClass( stdClass );
			stdClasses.put( stdClass.getName(), stdClass );
		}

	}

	/**
	 * Fills the {@link StudentClass} instance with the students, and populate
	 * the the student with the corresponding {@link Parent} instances and
	 * {@link Phone} instance.
	 * 
	 * <p>
	 * 
	 * @param stdClass
	 */
	private void populateStudentClass( StudentClass stdClass ) {
		List<Student> stList = db.loadStudentsFromClass( stdClass.getName() );
		Collections.sort( stList, new DataComparator.StudentComparator() );

		stdClass.addAll( stList );

		Iterator<Student> stds = stdClass.getStudents().iterator();
		while ( stds.hasNext() ) {
			Student student = stds.next();
			populateStudent( student );
		}
	}

	/**
	 * Populates the {@link Student} instance with the parents and the phone
	 * data
	 * 
	 * @param student The student to populate
	 */
	private void populateStudent( Student student ) {
		student.addParents( db.loadParents( student.getIdent() ) );

		Iterator<Parent> parIt = student.getParents().iterator();
		while ( parIt.hasNext() ) {
			Parent parent = parIt.next();
			parent.addPhones( db.loadPhone( parent.getStudentID(), parent.getID() ) );
		}
	}

	/**
	 * Creates a new empty {@link Task} instance.
	 * 
	 * @return
	 */
	public Task createTask() {
		return db.createNewTask();
	}

	/**
	 * 
	 * @return
	 */
	public SettingsManager getSettingsManager() {
		return sManager;
	}

	/**
	 * Lists the entire Database to the logCat.
	 */
	public void listDB() {
		Log.v( TAG, db.loadAllStudentTask().toString() );
	}

	@SuppressWarnings("rawtypes")
	public Map<String, List> getAllData() {
		HashMap<String, List> entireDB = new HashMap<String, List>();

		entireDB.put( "parents", db.loadParents( null ) );

		return entireDB;
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// STUDENT
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param ident
	 * @return
	 */
	public Student getStudentById( String ident ) {
		Student std = null;

		Iterator<String> it = stdClasses.keySet().iterator();
		while ( it.hasNext() ) {
			std = getStudentById( it.next(), ident );
			if ( std != null )
				break;
		}

		return std;
	}

	/**
	 * 
	 * @param stdClassName
	 * @param ident
	 * @return
	 */
	public Student getStudentById( String stdClassName, String ident ) {
		Student std = null;

		StudentClass stdClass = stdClasses.get( stdClassName );
		std = stdClass.getStudentByIdent( ident );

		return std;
	}

	/**
	 * 
	 * @param std
	 * @param oldIdent
	 * 
	 * @return true if successful
	 */
	public boolean updateStudent( Student std, String oldIdent ) {
		int retVal = 0;
		try {
			retVal = db.updateStudent( std, oldIdent );
			notifyStudentUpdate( std );
		}
		catch ( Exception e ) {
			Log.e( TAG, "Failed to update student: " + std.getIdent(), e );
		}

		return retVal > 0;
	}

	/**
	 * 
	 * @param std
	 */
	private void notifyStudentChagnge( Student std, int mode ) {
		Iterator<OnStudentChangedListener> it = stdChangeListeners.values().iterator();
		while ( it.hasNext() ) {
			it.next().onStudentChange( std, mode );
		}
	}

	/**
	 * 
	 * @param std
	 */
	private void notifyStudentUpdate( Student std ) {
		notifyStudentChagnge( std, OnStudentChangedListener.MODE_UPD );
	}

	/**
	 * 
	 * @param std
	 */
	private void notifyStudentDelete( Student std ) {
		notifyStudentChagnge( std, OnStudentChangedListener.MODE_DEL );
	}

	/**
	 * 
	 * @param std
	 */
	private void notifyStudenAdd( Student std ) {
		notifyStudentChagnge( std, OnStudentChangedListener.MODE_ADD );
	}

	/**
	 * 
	 * @param listener
	 */
	public void addOnStudentChangeListener( OnStudentChangedListener listener ) {
		String name = listener.getClass().getName();

		if ( stdChangeListeners.containsKey( name ) )
			stdChangeListeners.remove( name );

		stdChangeListeners.put( name, listener );
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// TASK
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Will create the default {@link SubjectType} the system knows. These are
	 * located in an XML file in the <tt>values</tt> folder.
	 */
	private void initSubjectTypes() {
		// Get the default arrays
		String[] subjects = mApp.getResources().getStringArray( R.array.task_subjects );
		String[] types = mApp.getResources().getStringArray( R.array.task_types );
		String defDesc = mApp.getResources().getString( R.string.task_st_subjects_defaultDesc );
		LinkedList<SubjectType> list = new LinkedList<SubjectType>();

		for ( String s : subjects ) {
			SubjectType st = db.createSubjectType();
			st.setDescription( defDesc );
			st.setName( s );
			st.setType( SubjectType.TYPE_SUBJECT );

			list.add( st );
		}

		for ( String s : types ) {
			SubjectType st = db.createSubjectType();
			st.setDescription( defDesc );
			st.setName( s );
			st.setType( SubjectType.TYPE_TYPE );

			list.add( st );
		}

		try {
			db.insertSubjectTypes( list );
		}
		catch ( Exception e ) {
			Log.e( TAG, "Error initiating SubjectTypes", e );
		}
	}

	/**
	 * Loads all the {@link SubjectType} instances found in the database. These
	 * types are stored in memory by the application.
	 */
	private void loadSubjectTypes() {
		List<SubjectType> list = db.loadSubjectTypes();
		for ( SubjectType st : list ) {
			if ( st.getType() == SubjectType.TYPE_SUBJECT )
				mTaskSubjects.put( st.getName(), st );
			else
				mTaskTypes.put( st.getName(), st );
		}
	}

	/**
	 * Gets a reference to all installed SubjectType.TYPE_SUBJECT
	 * 
	 * @return
	 */
	public Collection<SubjectType> getSubjects() {
		return mTaskSubjects.values();
	}

	/**
	 * 
	 * @return
	 */
	public Collection<String> getSubjectNames() {
		return mTaskSubjects.keySet();
	}

	public Collection<String> getTypeNames() {
		return mTaskTypes.keySet();
	}

	public SubjectType getSubjectType( int id ) {
		for ( SubjectType st : mTaskSubjects.values() ) {
			if ( st.getID() == id )
				return st;
		}

		for ( SubjectType st : mTaskTypes.values() ) {
			if ( st.getID() == id )
				return st;
		}

		return null;
	}

	/**
	 * Converts the name of an {@link SubjectType} to the ID it is stored with
	 * in the Database.
	 * 
	 * @param subject Name of the {@link SubjectType} to look for.
	 * @return -1 if the subject is not found.
	 */
	public int convertSubjectToID( String subject ) {
		return convertSubjectTypeToID( mTaskSubjects, subject );
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public int convertTypeToID( String type ) {
		return convertSubjectTypeToID( mTaskTypes, type );
	}

	/**
	 * 
	 * @param map
	 * @param name
	 * @return
	 */
	private int convertSubjectTypeToID( Map<String, SubjectType> map, String name ) {
		if ( !map.containsKey( name ) )
			return -1;

		SubjectType st = map.get( name );
		return st.getID();
	}

	/**
	 * 
	 * @return A List of all the Tasks loaded
	 */
	public List<String> getTaskNames() {
		return new ArrayList<String>( tasks.keySet() );
	}

	/**
	 * Finds any tasks that matches the flag. The flag must be one of the states
	 * a {@link Task} may be in: <tt>TASK_STATE_OPEN</tt>,
	 * <tt>TASK_STATE_CLOSED</tt> or <tt>TASK_STATE_EXPIRED</tt> or any
	 * combination of the them.
	 * 
	 * @return A List of every task in the system where the tasks state matches
	 *         the flag
	 */
	public List<String> getTaskNames( int flag ) {
		List<String> tasks = new LinkedList<String>();

		for ( Task t : this.tasks.values() ) {
			int state = t.getState();
			if ( ( state & flag ) == Task.TASK_STATE_OPEN ) {
				tasks.add( t.getName() );
			}
			if ( ( state & flag ) == Task.TASK_STATE_CLOSED ) {
				tasks.add( t.getName() );
			}
			if ( ( state & flag ) == Task.TASK_STATE_EXPIRED ) {
				tasks.add( t.getName() );
			}
		}

		return tasks;
	}

	/**
	 * 
	 * @return
	 */
	public List<Task> getTasks() {
		return new ArrayList<Task>( tasks.values() );
	}

	/**
	 * 
	 * @param name Name of {@link Task} to find.
	 * @return The actual task, or NULL if not found
	 */
	public Task getTask( String name ) {
		return tasks.get( name );
	}

	/**
	 * Adds the task to the database. The task will be filled with instances of
	 * {@link StudentTask} objects linking the {@link Student} to the
	 * {@link Task}.
	 * 
	 * @param task
	 */
	public DataHandler addTask( Task task ) {
		if ( task == null )
			throw new NullPointerException( "Task to add cannot be NULL!" );

		if ( task.getName() == null )
			throw new NullPointerException( "Name of task cannot be NULL!" );

		if ( tasks.containsKey( task.getName() ) )
			throw new IllegalArgumentException( "Task " + task.getName()
					+ " already exists" );

		if ( db.insertTask( task ) ) {
			List<StudentTask> stds = db.loadStudentsInTask( task );
			setUpStudentTask( task, stds );
			task.addStudentTasks( stds );

			tasks.put( task.getName(), task );
			notifyTaskAdd( task );
		}

		return this;
	}

	/**
	 * 
	 * @param stdTask
	 */
	public void handIn( StudentTask stdTask ) {
		Task task = tasks.get( stdTask.getTaskName() );
		if ( task.handIn( stdTask.getIdent() ) )
			db.updateStudentTask( stdTask );
	}

	public void handIn( StudentTask stdTask, int mode ) {
		switch ( mode ) {
			case StudentTask.MODE_HANDIN:
				handIn( stdTask );
				break;

			case StudentTask.MODE_PENDING:
				stdTask.handIn( mode );
				break;

			default:
				break;
		}
	}

	/**
	 * 
	 * @param task
	 * @param oldName
	 */
	public boolean updateTask( Task task, String oldName ) {
		Log.d( TAG, "Updating task: " + oldName );
		if ( !db.updateTask( task, oldName ) )
			return false;

		if ( !task.getName().equals( oldName ) ) {
			List<StudentTask> stdTasks = task.getStudentsInTask();
			Iterator<StudentTask> it = stdTasks.iterator();
			while ( it.hasNext() ) {
				it.next().setTaskName( task.getName() );
			}

			db.updateStudentTasks( stdTasks );
		}

		tasks.remove( oldName );
		tasks.put( task.getName(), task );

		notifyTaskUpdate( task );
		return true;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteTask( String name ) {
		Task task = getTask( name );

		if ( db.deleteTask( name ) ) {
			db.deleteStudentTasks( task.getStudentsInTask() );

			tasks.remove( name );
			notifyTaskDelete( task );
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean closeTask( String name ) {
		return closeTask( getTask( name ) );
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	public boolean closeTask( Task task ) {
		task.setState( Task.TASK_STATE_CLOSED );

		boolean succes = db.updateTask( task, task.getName() );
		notifyTaskChange( task, OnChangeListener.MODE_CLS );

		return succes;
	}

	/**
	 * Commits a {@link Task} to the DB.
	 * 
	 * @param task
	 */
	public void commitTask( Task task ) {
		db.insertTask( task );
	}

	/**
	 * 
	 * @param task
	 */
	public void commitStudentsTasks( Task task ) {
		List<StudentTask> list = task.getUpdatedStudents();
		int change = 0;

		if ( list != null && !list.isEmpty() ) {
			db.updateStudentTasks( list );
			change = OnTaskChangeListener.MODE_STD_UPD;
		}

		list = task.getRemovedStudents();
		if ( list != null && !list.isEmpty() ) {
			db.deleteStudentTasks( list );
			change = OnTaskChangeListener.MODE_STD_DEL;
		}

		list = task.getAddedStudents();
		if ( list != null && !list.isEmpty() ) {
			db.insertStudentTasks( list );
			change = OnTaskChangeListener.MODE_STD_ADD;
		}

		if ( change > 0 )
			task.notifyChange( change );
		task.markAsCommitted();
	}

	/**
     * 
     */
	public void commitTasks() {
		Iterator<Task> it = tasks.values().iterator();

		while ( it.hasNext() ) {
			Task task = it.next();
			commitTask( task );
		}
	}

	/**
	 * 
	 * @param newTask
	 */
	private void notifyTaskChange( Task newTask, int mode ) {
		if ( taskChangeListeners.isEmpty() )
			return;

		Iterator<OnTasksChangedListener> it = taskChangeListeners.values().iterator();
		while ( it.hasNext() )
			it.next().onTaskChange( newTask, mode );
	}

	/**
	 * 
	 * @param newTask
	 */
	private void notifyTaskAdd( Task newTask ) {
		notifyTaskChange( newTask, OnTasksChangedListener.MODE_ADD );
	}

	/**
	 * 
	 * @param newTask
	 */
	private void notifyTaskDelete( Task oldTask ) {
		notifyTaskChange( oldTask, OnTasksChangedListener.MODE_DEL );
	}

	/**
	 * 
	 * @param task
	 */
	private void notifyTaskUpdate( Task task ) {
		notifyTaskChange( task, OnTasksChangedListener.MODE_UPD );
	}

	/**
	 * 
	 * @param listener
	 */
	public void addOnTaskChangeListener( OnTasksChangedListener listener ) {
		String name = listener.getClass().getName();

		if ( taskChangeListeners.containsKey( name ) )
			taskChangeListeners.remove( name );

		taskChangeListeners.put( name, listener );
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// STUDENTCLASS
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Checks to see if a {@link StudentClass} is deletable. If the Studentclass
	 * is involved in a specific, and open Task, the StudentClass cannot be
	 * deleted.
	 * 
	 * @param stdClass The {@link StudentClass} to check.
	 * @return true if deletable
	 */
	public boolean isStudentClassDeletable( StudentClass stdClass ) {
		boolean deletable = true;

		Iterator<Task> it = tasks.values().iterator();
		while ( it.hasNext() ) {
			Task t = it.next();
			if ( t.getClasses().contains( stdClass.getName() ) ) {
				deletable = false;
			}
		}

		return deletable;
	}

	/**
	 * Returns a list of strings containing the names of all the tasks the
	 * {@link StudentClass} is involved in.
	 * 
	 * @param stdClass
	 * @return A list of names, or an empty list
	 */
	public List<String> getStudentClassInvolvedInTask( StudentClass stdClass ) {
		LinkedList<String> list = new LinkedList<String>();

		Iterator<Task> it = tasks.values().iterator();
		while ( it.hasNext() ) {
			Task t = it.next();
			if ( t.getClasses().contains( stdClass.getName() ) ) {
				list.add( t.getName() );
			}
		}

		return list;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getStudentClassNames() {
		return new ArrayList<String>( stdClasses.keySet() );
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public StudentClass getStudentClass( String name ) {
		return stdClasses.get( name );
	}

	/**
	 * 
	 * @param stdClass
	 */
	public void addStudentClass( StudentClass stdClass ) {
		db.insertStudentClass( stdClass );
		stdClasses.put( stdClass.getName(), stdClass );

		notifyStudentClassAdd( stdClass );
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteStudentClass( String name ) {
		if ( !stdClasses.containsKey( name ) )
			return false;

		if ( stdClassHasTasks( name ) )
			return false;

		StudentClass stdcClass = stdClasses.remove( name );
		db.deleteStdClass( stdcClass );

		notifyStudentClassDel( stdcClass );
		return true;
	}

	public boolean stdClassHasTasks( String stdClassName ) {
		Iterator<Task> it = tasks.values().iterator();
		while ( it.hasNext() ) {
			Task task = it.next();
			if ( task.getClasses().contains( stdClassName ) )
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param stdClass
	 */
	private void notifyStudentClassChange( StudentClass stdClass, int mode ) {
		if ( stdClassChangeListeners.isEmpty() )
			return;

		Iterator<OnStudentClassChangeListener> it = stdClassChangeListeners.values().iterator();
		while ( it.hasNext() )
			it.next().onStudentClassUpdate( stdClass, mode );
	}

	/**
	 * 
	 * @param stdClass
	 */
	private void notifyStudentClassAdd( StudentClass stdClass ) {
		notifyStudentClassChange( stdClass, OnStudentClassChangeListener.MODE_ADD );
	}

	private void notifyStudentClassDel( StudentClass stdClass ) {
		notifyStudentClassChange( stdClass, OnStudentClassChangeListener.MODE_DEL );
	}

	private void notifyStudentClassUpdate( StudentClass stdClass ) {
		notifyStudentClassChange( stdClass, OnStudentClassChangeListener.MODE_UPD );
	}

	/**
	 * 
	 * @param listener
	 */
	public void addOnStudentClassChangeListener( OnStudentClassChangeListener listener ) {
		String name = listener.getClass().getName();

		if ( stdClassChangeListeners.containsKey( name ) )
			stdClassChangeListeners.remove( name );

		stdClassChangeListeners.put( name, listener );
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public List<String> getFilesFromDownloadDir() {
		List<String> list = new ArrayList<String>();

		File externalDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS );
		File[] files = externalDir.listFiles();
		for ( int i = 0; i < files.length; i++ ) {
			File f = files[i];
			if ( f.isFile() && f.getName().endsWith( STDCLASS_FILE_SUFFIX ) )
				list.add( f.getName() );
		}

		return list;
	}

	/**
	 * Loads a CSV file with semicolon separated entries. The file MUST have an
	 * header line, and the headers MUST be in this order: - Klasse - Født -
	 * Fullt navn
	 * 
	 * @return A ready formatted StudentClass instance
	 * @throws IOException if any I/O error occurs
	 */
	public static StudentClass LoadStudentClassFromDownloadDir( Context ctx, String fileName ) throws IOException {
		FileInputStream fis;
		BufferedReader buff = null;

		File externalDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS );
		String fName = externalDir.getAbsolutePath() + "/" + fileName;
		String stdClassName = fileName.substring( 0, fileName.length() - 4 );
		StudentClass stdClass = new StudentClassImpl( stdClassName );

		try {
			fis = new FileInputStream( new File( fName ) );
		}
		catch ( FileNotFoundException fnfEx ) {
			Log.e( TAG, "LoadStudentClass(): File not found: " + fName, fnfEx );
			throw fnfEx;
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "LoadStudentClass(): Unknown error: " + fName, e );
			throw e;
		}

		ArrayList<Student> list = new ArrayList<Student>();

		try {
			buff = new BufferedReader( new InputStreamReader( fis, "CP1252" ) );
			// Read the first header
			buff.readLine();

			String stdLine;
			while ( ( stdLine = buff.readLine() ) != null ) {
				Student newStudent = CreateStudentFromString( stdLine, stdClassName, "dd.MM.yyyy" );
				list.add( newStudent );
			}
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "LoadStudentClass(): Error loading file: " + fName, ioe );
			throw ioe;
		}

		try {
			buff.close();
			fis.close();
		}
		catch ( IOException e ) {
			Log.e( TAG, "LoadStudentClass(): Error closing file: " + fName, e );
		}

		Log.v( TAG, list.toString() );
		stdClass.addAll( list );
		return stdClass;

	}

	/**
	 * 
	 * @param stdString A new Student implementation from a semicolon separated
	 *            String.
	 * @return
	 */
	private static Student CreateStudentFromString( String stdString, String className, String datePattern ) {
		StudentBean bean = new StudentBean( className );

		String[] params = stdString.split( STUDENT_PROPERTY_SEP );
		if ( params.length == 11 )
			return CreateOldStudentFromstring( bean, params, datePattern );

		int index = 0;

		bean.lName = params[index++];
		bean.fName = params[index++];
		bean.birth = DBUtils.ConvertStringToDate( params[index++], datePattern );
		bean.adress = params[index++];
		bean.mIdent = CreateStudentIdent( bean );

		bean.addParent( CreateParent( params, index, bean.getIdent(), Parent.PRIMARY ) );

		index = 10;
		bean.addParent( CreateParent( params, index, bean.getIdent(), Parent.SECUNDARY ) );

		return bean;

	}

	/**
	 * 
	 * @param params
	 * @param start
	 * @param id
	 * @param type
	 * @return
	 */
	private static Parent CreateParent( String[] params, int start, String id, int type ) {
		Parent parent = new ParentBean( null, type );
		parent.setStudentID( id );
		parent.setLastName( params[start++] );
		parent.setFirstName( params[start++] );

		if ( params.length >= 13 )
			parent.addPhone( CreatePhone( params[start++], Phone.MOBIL, id ) );
		if ( params.length >= 14 )
			parent.addPhone( CreatePhone( params[start++], Phone.WORK, id ) );
		if ( params.length >= 15 )
			parent.addPhone( CreatePhone( params[start++], Phone.HOME, id ) );

		if ( params.length >= 16 )
			parent.setMail( params[start++] );

		return parent;
	}

	/**
	 * 
	 * @param param
	 * @param type
	 * @param id
	 * @return
	 */
	private static Phone CreatePhone( String param, int type, String id ) {
		if ( param == null )
			return null;
		if ( !( param.length() > 0 ) )
			return null;

		PhoneBean phone = new PhoneBean( type );
		phone.setNumber( Long.parseLong( param ) );
		phone.setStudentID( id );

		return phone;
	}

	/**
	 * 
	 * @param bean
	 * @param params
	 * @param datePattern
	 * @return
	 */
	private static Student CreateOldStudentFromstring( StudentBean bean, String[] params, String datePattern ) {
		Parent parent = null;
		Phone phone = null;
		String[] subParams;
		int index = 0;

		bean.grade = params[index++];
		bean.birth = DBUtils.ConvertStringToDate( params[index++], datePattern );
		bean.setFullName( params[index++] );
		bean.adress = params[index++];
		bean.postalCode = params[index++];

		bean.mIdent = CreateStudentIdent( bean );

		parent = new ParentBean( bean.getIdent(), Parent.PRIMARY );
		subParams = params[index++].split( "," );
		parent.setLastName( subParams[0].trim() );
		parent.setFirstName( subParams[1].trim() );

		phone = new PhoneBean( Phone.MOBIL );
		phone.setStudentID( bean.getIdent() );
		phone.setParentID( parent.getID() );
		phone.setNumber( Long.parseLong( params[index++] ) );
		parent.addPhone( phone );
		parent.setMail( params[index++] );
		bean.addParent( parent );

		parent = new ParentBean( bean.getIdent(), Parent.SECUNDARY );
		subParams = params[index++].split( "," );
		parent.setLastName( subParams[0].trim() );
		parent.setFirstName( subParams[1].trim() );

		phone = new PhoneBean( Phone.MOBIL );
		phone.setParentID( parent.getID() );
		phone.setStudentID( bean.getIdent() );
		try {
			phone.setNumber( Long.parseLong( params[index++] ) );
		}
		catch ( Exception e ) {
			phone.setNumber( 0 );
		}
		parent.addPhone( phone );
		parent.setMail( params[index++] );

		bean.addParent( parent );

		return bean;
	}

	/**
	 * 
	 * @param bean
	 * @return A new Ident
	 */
	private static String CreateStudentIdent( Student bean ) {
		String ident = null;

		String fName = bean.getFirstName().substring( 0, 3 );
		String lName = bean.getLastName().substring( 0, 4 );

		String year = bean.getBirth();
		year = year.substring( year.length() - 2, year.length() );

		ident = year + fName + lName;
		ident = ident.replace( 'æ', 'e' );
		ident = ident.replace( 'ø', 'o' );
		ident = ident.replace( 'å', 'a' );

		Log.d( TAG, "Creating ident: " + ident );
		return ident.toLowerCase( Locale.getDefault() );
	}

	/**
	 * 
	 * @param std
	 * @return
	 */
	private static String StudentToDataString( Student std ) {
		StringBuffer sb = new StringBuffer();

		sb.append( std.getGrade() ).append( STUDENT_PROPERTY_SEP );
		sb.append( ( (StudentBean) std ).birhtToString() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getLastName() ).append( ", " ).append( std.getFirstName() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getAdress() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getPostalCode() ).append( STUDENT_PROPERTY_SEP );
		/*
		 * sb.append( std.getParent1Name() ).append( STUDENT_PROPERTY_SEP );
		 * sb.append( std.getParent1Phone() ).append( STUDENT_PROPERTY_SEP );
		 * sb.append( std.getParent1Mail() ).append( STUDENT_PROPERTY_SEP );
		 * sb.append( std.getParent2Name() ).append( STUDENT_PROPERTY_SEP );
		 * sb.append( std.getParent2Phone() ).append( STUDENT_PROPERTY_SEP );
		 * sb.append( std.getParent2Mail() );
		 */
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	private static String WriteDataHeader() {
		StringBuffer sb = new StringBuffer();

		sb.append( "Klasse" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Født" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Fullt navn" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Adresse" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Postnr" ).append( STUDENT_PROPERTY_SEP );

		sb.append( "Foresatt 1 navn" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Foresatt 1 mobil" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Foresatt 1 e-post" ).append( STUDENT_PROPERTY_SEP );

		sb.append( "Foresatt 2 navn" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Foresatt 2 mobil" ).append( STUDENT_PROPERTY_SEP );
		sb.append( "Foresatt 2 e-post" ).append( STUDENT_PROPERTY_SEP );

		return sb.toString();
	}

	/**
	 * Loads every locally stored StudentClass. Every classfile MUST have the
	 * suffix STDCLASS_FILE_SUFFIX
	 * 
	 * @param ctx The Context used to access the local filesystem
	 */
	public static void LoadLocalStudentClasses( Context ctx ) {
		if ( islocalStudentClassesLoaded )
			return;

		File[] files = ctx.getFilesDir().listFiles();
		String stdClassName, classFile = null;

		try {
			for ( int i = 0; i < files.length; i++ ) {
				classFile = files[i].getName();
				if ( classFile.endsWith( STDCLASS_FILE_SUFFIX ) ) {
					stdClassName = classFile.substring( 0, classFile.length() - STDCLASS_FILE_SUFFIX.length() );

					StudentClass stdClass = new StudentClassImpl( stdClassName );
					List<Student> list = Database.GetInstance( ctx ).loadStudentsFromClass( stdClassName );

					if ( !list.isEmpty() ) {
						stdClass.addAll( list );
					}
					else {
						FileInputStream fis = ctx.openFileInput( classFile );
						BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );

						String readLine;
						while ( ( readLine = reader.readLine() ) != null ) {
							Student student = CreateStudentFromString( readLine, stdClassName, BaseValues.DATE_PATTERN );
							stdClass.add( student );
						}
					}

					GetInstance().addStudentClass( stdClass );
				}
			}
		}
		catch ( Exception e ) {
			Log.e( TAG, "Cannot load localStudentClass: " + classFile, e );
		}

		islocalStudentClassesLoaded = true;
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// STUDENTS IN TASKS
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param activity
	 */
	public static void LoadStudentInTasks( Activity activity ) {
		if ( studentInTasks == null )
			studentInTasks = new HashMap<String, StudentTaskImpl>();

		FileInputStream fis;
		BufferedReader buff;

		try {
			fis = activity.openFileInput( STUDENT_IN_TASK_FILENAME );
			buff = new BufferedReader( new InputStreamReader( fis ) );

			String readLine = null;
			while ( ( readLine = buff.readLine() ) != null ) {
				StudentTaskImpl std = CreateStudentInTasksFromString( readLine );
				studentInTasks.put( std.getIdent(), std );
			}

			fis.close();
		}
		catch ( FileNotFoundException e ) {
			Log.e( TAG, "Cannot load " + STUDENT_IN_TASK_FILENAME, e );
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "Failure reading data from " + STUDENT_IN_TASK_FILENAME, ioe );
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param stds
	 * @param append
	 */
	public static void WriteStudentInTasks( Context ctx, HashMap<String, StudentTaskImpl> stds, boolean append ) {
		if ( studentInTasks == null )
			return;

		FileOutputStream fos;
		BufferedWriter buff;

		try {
			fos = ctx.openFileOutput( STUDENT_IN_TASK_FILENAME, Context.MODE_PRIVATE );
			buff = new BufferedWriter( new OutputStreamWriter( fos ) );

			Iterator<String> it = studentInTasks.keySet().iterator();
			while ( it.hasNext() ) {
				StudentTaskImpl std = studentInTasks.get( it.next() );
				String data = CreateStringFromStudentsInTasks( std );

				buff.write( data );
				buff.newLine();
			}
		}
		catch ( FileNotFoundException e ) {
			Log.e( TAG, "Cannot write " + STUDENT_IN_TASK_FILENAME, e );
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "Failure writing data to " + STUDENT_IN_TASK_FILENAME, ioe );
		}
	}

	/**
	 * 
	 * @param std
	 * @return
	 */
	private static String CreateStringFromStudentsInTasks( StudentTaskImpl std ) {
		StringBuffer sb = new StringBuffer();
		/*
		 * sb.append( std.getIdent() ).append( STUDENT_IN_TASK_SEP ); int size =
		 * std.getEngagedTasks().size() - 1; for ( int i = 0; i < size; i++ ) {
		 * sb.append( std.getEngagedTasks().get( i ) ).append(
		 * STUDENT_IN_TASK_DELIM ); } sb.append( std.getEngagedTasks().get( size
		 * ) );
		 */
		return sb.toString();
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private static StudentTaskImpl CreateStudentInTasksFromString( String data ) {
		String[] params = data.split( STUDENT_IN_TASK_SEP );
		String ident = params[0].trim();

		params = params[1].split( STUDENT_IN_TASK_DELIM );
		List<String> tasks = new ArrayList<String>( params.length );
		for ( int i = 0; i < params.length; i++ ) {
			tasks.add( params[i] );
		}

		return null; // new StudentTaskImpl( ident, tasks );
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// UTIL METHODS
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Checks if external storage is available for read and write
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if external storage is available to at least read
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) || Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
			return true;
		}
		return false;
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// LISTENER
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	public static interface OnChangeListener {
		public static final int MODE_ADD = 1;
		public static final int MODE_DEL = 2;
		public static final int MODE_UPD = 4;

		public static final int MODE_CLS = 8;
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnTasksChangedListener extends OnChangeListener {

		public void onTaskChange( Task newTask, int mode );
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnStudentChangedListener extends OnChangeListener {

		public void onStudentChange( Student std, int mode );
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnStudentClassChangeListener extends OnChangeListener {

		public void onStudentClassUpdate( StudentClass stdClass, int mode );
	}

}
