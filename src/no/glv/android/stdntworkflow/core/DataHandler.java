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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import no.glv.android.stdntworkflow.sql.Database;
import no.glv.android.stdntworkflow.sql.StudentBean;
import no.glv.android.stdntworkflow.sql.StudentClassImpl;
import no.glv.android.stdntworkflow.sql.StudentTaskImpl;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author GleVoll
 *
 */
public class DataHandler {

	private static final String TAG = DataHandler.class.getSimpleName();

	private static final String STUDENT_IN_TASK_FILENAME = "stdntsk.glv";
	private static final String STUDENT_IN_TASK_SEP = "=";
	private static final String STUDENT_PROPERTY_SEP = ";";
	private static final String STUDENT_IN_TASK_DELIM = ",";

	private static String STDCLASS_FILE_SUFFIX = ".csv";
	private static String TASKS_FILE_SUFFIX = ".tsk";

	/** A map of all the tasks the students are involved in */
	private static HashMap<String, StudentTaskImpl> studentInTasks;

	private static boolean islocalStudentClassesLoaded = false;

	private Database db;

	private Map<String, StudentClass> stdClasses;
	private Map<String, Task> tasks;

	private static DataHandler instance;
	private static boolean isInitiated = false;

	// Listeners
	private List<OnTaskChangedListener> taskChangeListeners;
	private List<OnStudentClassChangeListener> stdClassChangeListeners;
	private List<OnStudentChangedListener> stdChangeListeners;

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             if Init has not been called first!
	 */
	public static final DataHandler GetInstance() {
		if ( !isInitiated ) throw new IllegalStateException( "DataHandler not inititated" );

		return instance;
	}

	// --------------------------------------------------------------------------------------------------------
	// INIT
	// --------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param db
	 *            The database to use
	 */
	public static final DataHandler Init( Context ctx ) {
		if ( instance == null ) instance = new DataHandler( new Database( ctx ) );

		instance.loadTasks();
		instance.loadStudentClasses();

		isInitiated = true;
		return instance;
	}

	/**
	 * 
	 * @param db
	 */
	private DataHandler( Database db ) {
		this.db = db;

		stdClassChangeListeners = new ArrayList<DataHandler.OnStudentClassChangeListener>( 2 );
		stdChangeListeners = new ArrayList<DataHandler.OnStudentChangedListener>( 2 );
		taskChangeListeners = new ArrayList<DataHandler.OnTaskChangedListener>( 2 );
	}

	/**
	 * 
	 */
	private void loadTasks() {
		tasks = new HashMap<String, Task>();

		List<Task> list = db.loadTasks();
		Iterator<Task> it = list.iterator();

		while ( it.hasNext() ) {
			Task task = it.next();
			tasks.put( task.getName(), task );
		}

	}

	/**
	 * 
	 */
	private void loadStudentClasses() {
		stdClasses = new HashMap<String, StudentClass>();

		List<StudentClass> list = db.loadStudentClasses();
		Iterator<StudentClass> it = list.iterator();
		while ( it.hasNext() ) {
			StudentClass stdClass = it.next();
			stdClasses.put( stdClass.getName(), stdClass );
			stdClass.addAll( db.loadStudentsFromClass( stdClass.getName() ) );
		}

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
	public Student findStudentById( String ident ) {
		Student std = null;

		Iterator<String> it = stdClasses.keySet().iterator();
		while ( it.hasNext() ) {
			std = findStudentById( it.next(), ident );
			if ( std != null ) break;
		}

		return std;
	}

	/**
	 * 
	 * @param stdClassName
	 * @param ident
	 * @return
	 */
	public Student findStudentById( String stdClassName, String ident ) {
		Student std = null;

		StudentClass stdClass = stdClasses.get( stdClassName );
		std = stdClass.getStudentByIdent( ident );

		return std;
	}

	/**
	 * 
	 * @param std
	 * @param oldIdent
	 */
	public void updateStudent( Student std, String oldIdent ) {
		try {
			db.updateStudent( std, oldIdent );
			notifyStudentUpdate( std );
		}
		catch ( Exception e ) {
			Log.e( TAG, "Failed to update student: " + std.getIdent(), e );
		}
	}

	/**
	 * 
	 * @param std
	 */
	private void notifyStudentChagnge( Student std, int mode ) {
		Iterator<OnStudentChangedListener> it = stdChangeListeners.iterator();
		while ( it.hasNext() ) {
			it.next().onStudenChange( std, mode );
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
	 * @param listener
	 */
	public void addStudentChangesListener( OnStudentChangedListener listener ) {
		stdChangeListeners.add( listener );
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// TASKS
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @return A List of all the Tasks loaded
	 */
	public List<String> getTaskNames() {
		return new ArrayList<String>( tasks.keySet() );
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
	 * @param task
	 */
	public DataHandler addTask( Task task ) {
		if ( task == null ) throw new NullPointerException( "Task to add cannot be NULL!" );

		if ( task.getName() == null ) throw new NullPointerException( "Name of task cannot be NULL!" );

		if ( tasks.containsKey( task.getName() ) )
			throw new IllegalArgumentException( "Task " + task.getName() + " already exists" );

		tasks.put( task.getName(), task );
		notifyTaskAdd( task );

		return this;
	}

	/**
	 * 
	 */
	public void commitTasks() {
		Iterator<Task> it = tasks.values().iterator();

		while ( it.hasNext() ) {
			Task task = it.next();
			db.writeTask( task );
		}
	}

	/**
	 * 
	 * @param newTask
	 */
	private void notifyTaskChange( Task newTask, int mode ) {
		if ( taskChangeListeners.isEmpty() ) return;

		Iterator<OnTaskChangedListener> it = taskChangeListeners.iterator();
		while ( it.hasNext() )
			it.next().onTaskChange( newTask, mode );
	}

	/**
	 * 
	 * @param newTask
	 */
	private void notifyTaskAdd( Task newTask ) {
		notifyTaskChange( newTask, OnTaskChangedListener.MODE_ADD );
	}

	/**
	 * 
	 * @param listener
	 */
	public void addOnTaskChangeListener( OnTaskChangedListener listener ) {
		taskChangeListeners.add( listener );
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
		stdClasses.put( stdClass.getName(), stdClass );

		db.insertStudentClass( stdClass );
		notifyStudentClassAdd( stdClass );
	}

	/**
	 * 
	 * @param stdClass
	 */
	private void notifyStudentClassChange( StudentClass stdClass, int mode ) {
		if ( stdChangeListeners.isEmpty() ) return;

		Iterator<OnStudentClassChangeListener> it = stdClassChangeListeners.iterator();
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

	/**
	 * 
	 * @param listener
	 */
	public void addStudentClassChangeListener( OnStudentClassChangeListener listener ) {
		stdClassChangeListeners.add( listener );
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
			if ( f.isFile() && f.getName().endsWith( STDCLASS_FILE_SUFFIX )) list.add( f.getName() );
		}

		return list;
	}

	/**
	 * Loads a CSV file with semicolon separated entries. The file MUST have an
	 * header line, and the headers MUST be in this order: - Klasse - Født -
	 * Fullt navn
	 * 
	 * TODO: Finish writing
	 * 
	 * @return A ready formatted StudentClass instance
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
			while ( (stdLine = buff.readLine()) != null ) {
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
	 * @param stdString
	 *            A new Student implementation from a semicolon separated
	 *            String.
	 * @return
	 */
	private static Student CreateStudentFromString( String stdString, String className, String datePattern ) {
		StudentBean bean = new StudentBean( className );

		String[] params = stdString.split( STUDENT_PROPERTY_SEP );
		for ( int i = 0; i < params.length; i++ ) {
			String param = params[i];

			switch ( i ) {
			case 0:
				bean.grade = param;
				break;

			case 1:
				try {
					bean.birth = new SimpleDateFormat( datePattern, Locale.getDefault() ).parse( param );

				}
				catch ( Exception e ) {
					Log.e( TAG, "Cannot convert String to date: " + param, e );
				}
				break;

			case 2:
				bean.setFullName( param );
				break;

			case 3:
				bean.adress = param;
				break;

			case 4:
				bean.postalCode = param;
				break;

			case 5:
				bean.parent1Name = param;
				break;

			case 6:
				bean.parent1Phone = param;
				break;

			case 7:
				bean.parent1Mail = param;
				break;

			case 8:
				bean.parent2Name = param;
				break;

			case 9:
				bean.parent2Phone = param;
				break;

			case 10:
				bean.parent2Mail = param;
				break;

			default:
				break;
			}
		}

		bean.mIdent = CreateStudentIdent( bean );

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
		String lName = bean.getLastname().substring( 0, 4 );

		String year = bean.getBirth();
		year = year.substring( 2, 4 );

		ident = year + fName + lName;
		ident = ident.replace( 'æ', 'e' );
		ident = ident.replace( 'ø', 'o' );
		ident = ident.replace( 'å', 'a' );

		Log.d( TAG, "Creating ident: " + ident );
		return ident.toLowerCase();
	}

	/**
	 * 
	 * @param std
	 * @return
	 */
	private static String StudentToDataString( Student std ) {
		StringBuffer sb = new StringBuffer();

		sb.append( std.getGrade() ).append( STUDENT_PROPERTY_SEP );
		sb.append( ((StudentBean) std).birhtToString() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getLastname() ).append( ", " ).append( std.getFirstName() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getAdress() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getPostalCode() ).append( STUDENT_PROPERTY_SEP );

		sb.append( std.getParent1Name() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getParent1Phone() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getParent1Mail() ).append( STUDENT_PROPERTY_SEP );

		sb.append( std.getParent2Name() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getParent2Phone() ).append( STUDENT_PROPERTY_SEP );
		sb.append( std.getParent2Mail() );

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
	 * @param ctx
	 *            The Context used to access the local filesystem
	 */
	public static void LoadLocalStudentClasses( Context ctx ) {
		if ( islocalStudentClassesLoaded ) return;

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
						while ( (readLine = reader.readLine()) != null ) {
							Student student = CreateStudentFromString( readLine, stdClassName, BaseValues.DATE_PATTERN );
							stdClass.add( student );
						}
					}

					StudentClassHandler.GetInstance().addStudentClass( stdClass );
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
		if ( studentInTasks == null ) studentInTasks = new HashMap<String, StudentTaskImpl>();

		FileInputStream fis;
		BufferedReader buff;

		try {
			fis = activity.openFileInput( STUDENT_IN_TASK_FILENAME );
			buff = new BufferedReader( new InputStreamReader( fis ) );

			String readLine = null;
			while ( (readLine = buff.readLine()) != null ) {
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
		if ( studentInTasks == null ) return;

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
		 * STUDENT_IN_TASK_DELIM ); }
		 * 
		 * sb.append( std.getEngagedTasks().get( size ) );
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
		public static final int MODE_UPD = 3;
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnTaskChangedListener extends OnChangeListener {

		public void onTaskChange( Task newTask, int mode );
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnStudentChangedListener extends OnChangeListener {

		public void onStudenChange( Student std, int mode );
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
