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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author GleVoll
 *
 */
public class LoadDataHandler {

	private static final String TAG = LoadDataHandler.class.getSimpleName();

	private static final String STUDENT_IN_TASK_FILENAME = "stdntsk.glv";
	private static final String STUDENT_IN_TASK_SEP = "=";
	private static final String STUDENT_PROPERTY_SEP = ";";
	private static final String STUDENT_IN_TASK_DELIM = ",";

	private static String CLASSNAME = "7B - 2013";
	private static String FILENAME = CLASSNAME + ".csv";
	private static String STDCLASS_FILE_SUFFIX = ".glv";

	private static String TASKS_FILE_SUFFIX = ".tsk";

	private static List<String> localStudentClasses;

	/** A map of all the tasks the students are involved in */
	private static HashMap<String, StudentInTasks> studentInTasks;

	private static boolean islocalStudentClassesLoaded = false;

	private LoadDataHandler() {
	}
	
	
	
	public static List<String> GetTasksAsString() {
		return new ArrayList<String>();
	}
	
	
	public static void LoadTasks( Context ctx) {
		File[] files = ctx.getFilesDir().listFiles();
		String stdClassName, classFile = null;

		try {
			for ( int i = 0; i < files.length; i++ ) {
				classFile = files[i].getName();
				
				if ( classFile.endsWith( TASKS_FILE_SUFFIX )) {
					stdClassName = classFile.substring( 0, classFile.length() - STDCLASS_FILE_SUFFIX.length() );
	
					FileInputStream fis = ctx.openFileInput( classFile );
					BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
	
					String readLine;
					int count = 0;
					Task task = TaskHandler.GetInstance().createTask();
					while ( (readLine = reader.readLine()) != null ) {
						String[] params = readLine.split( STUDENT_IN_TASK_SEP );

						if ( Task.PROP_NAME.equals( params[0] )) 
							task.setName( params[1] );
					
						if ( Task.PROP_TYPE.equals( params[0] )) 
							task.setType( Integer.parseInt( params[1] ) );
					
						if ( Task.PROP_DESC.equals( params[0] )) 
							task.setDescription( params[1] );
					
						if ( Task.PROP_DATE.equals( params[0] )) 
							task.setDate( new Date( Long.parseLong( params[1] ) ) );
					
						if ( Task.PROP_STDNT.equals( params[0] )) {
							String[] students = params[1].split( STUDENT_IN_TASK_DELIM );
							for ( int j = 0; j < students.length; j++ ) {
								task.addStudent( students[i] );	
							}
						}

						if ( Task.PROP_STDNT_PEND.equals( params[0] )) {
							String[] students = params[1].split( STUDENT_IN_TASK_DELIM );
							for ( int j = 0; j < students.length; j++ ) {
								task.addStudent( students[i] );	
							}
						}

						if ( Task.PROP_CLASS.equals( params[0] )) {
							String[] classes = params[1].split( STUDENT_IN_TASK_DELIM );
							for ( int j = 0; j < classes.length; j++ ) {
								task.addClass( classes[i] );	
							}
						}
						
					}	

					TaskHandler.GetInstance().addTask( task );
				}
			}
		}
		catch ( Exception e ) {
			Log.e( TAG, "Cannot load localStudentClass: " + classFile, e );
		}
	}
	

	/**
	 * 
	 * @param ctx
	 * @param task
	 * @return
	 */
	public static boolean WriteTask( Context ctx, Task task ) {
		FileOutputStream fos;
		BufferedWriter buff = null;
		
		String taskStr = ConvertTaskToString( task );
		String fileName = task.getName() + TASKS_FILE_SUFFIX;
		
		try {
			fos = ctx.openFileOutput( fileName, Context.MODE_PRIVATE );
			buff = new BufferedWriter( new OutputStreamWriter( fos ) );
			
//			buff.write( taskStr );
			
			buff.close();
			fos.close();
		}
		catch ( FileNotFoundException e ) {
			Log.e( TAG, "Cannot open Task: " + fileName, e );
			return false;
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "Cannot write Task: " + fileName, ioe );
			return false;
		}
		
		
		return true;
	}
	
	
	private static String ConvertTaskToString( Task task ) {
		StringBuffer sb = new StringBuffer();
		
		sb.append( Task.PROP_NAME ).append( STUDENT_IN_TASK_SEP ).append( task.getName() ).append( "\n" );
		sb.append( Task.PROP_TYPE ).append( STUDENT_IN_TASK_SEP ).append( task.getType() ).append( "\n" );
		sb.append( Task.PROP_DESC ).append( STUDENT_IN_TASK_SEP ).append( task.getDesciption() ).append( "\n" );
		sb.append( Task.PROP_DATE ).append( STUDENT_IN_TASK_SEP ).append( task.getDate().getTime() ).append( "\n" );
		
		// Add all students whom have handed in their assignment
		AppendProperties( sb, task.getStudents(), Task.PROP_STDNT );

		// Add all students whom have NOT handed in their assignment
		AppendProperties( sb, task.getStudentsPending(), Task.PROP_STDNT_PEND );

		// Add the classes
		AppendProperties( sb, task.getClasses(), Task.PROP_CLASS );
		
		// Return formatted string
		return sb.toString();
	}
	
	private static void AppendProperties( StringBuffer sb, List<String> list, String prop ) {
		sb.append( prop ).append( STUDENT_IN_TASK_SEP );
		
		if ( list.isEmpty() ) return;
		
		int size = list.size() - 1;
		for ( int i = 0; i < size ; i++ ) {
			sb.append( list.get( i ) ).append( STUDENT_IN_TASK_DELIM );
		}
		sb.append( list.get( size ) );		
	}
	

	/**
	 * Loads a CSV file with semicolon separated entries. The file MUST have an
	 * header line, and the headers MUST be in this order:
	 * 		- Klasse
	 * 		- Født
	 * 		- Fullt navn
	 * 
	 * TODO: Finish writing
	 * 
	 * @return A ready formatted StudentClass instance
	 */
	public static StudentClass LoadStudentClassFromDownloadDir( Context ctx, String fileName ) {
		FileInputStream fis;
		BufferedReader buff = null;
		
		File externalDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS );
		String fName = externalDir.getAbsolutePath() + "/" + FILENAME;
		String stdClassName = CLASSNAME;
		StudentClass stdClass = new StudentClassImpl( stdClassName );

		try {
			fis = new FileInputStream( new File( fName ) );
		}
		catch ( FileNotFoundException fnfEx ) {
			Log.e( TAG, "LoadStudentClass(): File not found: " + fName, fnfEx );
			return null;
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "LoadStudentClass(): Unknown error: " + fName, e );
			return null;
		}

		ArrayList<Student> list = new ArrayList<Student>();

		try {
			buff = new BufferedReader( new InputStreamReader( fis ) );
			// Read the first header
			buff.readLine();

			String stdLine;
			while ( (stdLine = buff.readLine()) != null )
				list.add( CreateStudentFromString( stdLine, stdClassName, "dd.MM.yyyy" ) );
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "LoadStudentClass(): Error loading file: " + fName, ioe );
		}
		finally {
			try {
				buff.close();
				fis.close();
			}
			catch ( IOException e ) {
				Log.e( TAG, "LoadStudentClass(): Error closing file: " + fName, e );
			}
		}

		Log.v( TAG, list.toString() );
		stdClass.addAll( list );
		return stdClass;

	}

	/**
	 * 
	 * @param stdString A new Student implementation from a semicolon separated String.
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

		Log.d( TAG, "Creating ident: " + ident );
		return ident;
	}

	/**
	 * Writes a StudentClass to local file.
	 * 
	 * @param students
	 * @param ctx
	 * @param fileName
	 * @return true if success
	 */
	public static boolean WriteLocalStudentClass( StudentClass stdClass, Context ctx ) {
		FileOutputStream fos;
		BufferedWriter bw;
		String fileName = stdClass.getName() + STDCLASS_FILE_SUFFIX;

		try {
			fos = ctx.openFileOutput( fileName, Context.MODE_PRIVATE );
			bw = new BufferedWriter( new OutputStreamWriter( fos ) );
		}
		catch ( FileNotFoundException e ) {
			Log.e( TAG, "WriteStudentClass: File noe found: " + fileName, e );
			return false;
		}

		Iterator<Student> it = stdClass.iterator();
		while ( it.hasNext() ) {
			Student std = it.next();
			String stdString = StudentToDataString( std );
			try {
				bw.write( stdString );
				bw.newLine();
			}
			catch ( IOException e ) {
				Log.e( TAG, "WriteStudentClass: Cannot write to file " + fileName, e );
				return false;
			}
		}

		try {
			bw.flush();
			bw.close();
			fos.close();
		}
		catch ( IOException e2 ) {
			Log.e( TAG, "Error closing FileOutputStream", e2 );
		}

		Log.d( TAG, "Written new localStudentClass: " + stdClass.getName() );
		return true;
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

		sb.append( "Klasse" ).append(STUDENT_PROPERTY_SEP );
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
	 * 
	 * @param activity
	 * @return
	 */
	public static List<String> GetLocalStudentClasses( Activity activity ) {
		if ( localStudentClasses != null ) return localStudentClasses;

		ArrayList<String> list = new ArrayList<String>();

		File[] files = activity.getFilesDir().listFiles();
		for ( int i = 0; i < files.length; i++ ) {
			list.add( files[i].getName() );
		}

		localStudentClasses = list;
		return list;
	}

	
	/**
	 * Loads every locally stored StudentClass. Every classfile MUST have the suffix STDCLASS_FILE_SUFFIX
	 * 
	 * @param ctx The Context used to access the local filesystem
	 */
	public static void LoadLocalStudentClasses( Context ctx ) {
		if ( islocalStudentClassesLoaded ) return;

		File[] files = ctx.getFilesDir().listFiles();
		String stdClassName, classFile = null;

		try {
			for ( int i = 0; i < files.length; i++ ) {
				classFile = files[i].getName();
				if (classFile.endsWith( STDCLASS_FILE_SUFFIX ) ) {
					stdClassName = classFile.substring( 0, classFile.length() - STDCLASS_FILE_SUFFIX.length() );
					StudentClass stdClass = new StudentClassImpl( stdClassName );
	
					FileInputStream fis = ctx.openFileInput( classFile );
					BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
	
					String readLine;
					while ( (readLine = reader.readLine()) != null ) {
						Student student = CreateStudentFromString( readLine, stdClassName, BaseValues.DATE_PATTERN );
						stdClass.add( student );
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

	/**
	 * 
	 * @param activity
	 */
	public static void LoadStudentInTasks( Activity activity ) {
		if ( studentInTasks == null ) studentInTasks = new HashMap<String, StudentInTasks>();

		FileInputStream fis;
		BufferedReader buff;

		try {
			fis = activity.openFileInput( STUDENT_IN_TASK_FILENAME );
			buff = new BufferedReader( new InputStreamReader( fis ) );

			String readLine = null;
			while ( (readLine = buff.readLine()) != null ) {
				StudentInTasks std = CreateStudentInTasksFromString( readLine );
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
	public static void WriteStudentInTasks( Context ctx, HashMap<String, StudentInTasks> stds, boolean append ) {
		if ( studentInTasks == null ) return;

		FileOutputStream fos;
		BufferedWriter buff;

		try {
			fos = ctx.openFileOutput( STUDENT_IN_TASK_FILENAME, Context.MODE_PRIVATE );
			buff = new BufferedWriter( new OutputStreamWriter( fos ) );

			Iterator<String> it = studentInTasks.keySet().iterator();
			while ( it.hasNext() ) {
				StudentInTasks std = studentInTasks.get( it.next() );
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
	private static String CreateStringFromStudentsInTasks( StudentInTasks std ) {
		StringBuffer sb = new StringBuffer();

		sb.append( std.getIdent() ).append( STUDENT_IN_TASK_SEP );
		int size = std.getEngagedTasks().size() - 1;
		for ( int i = 0; i < size; i++ ) {
			sb.append( std.getEngagedTasks().get( i ) ).append( STUDENT_IN_TASK_DELIM );
		}

		sb.append( std.getEngagedTasks().get( size ) );

		return sb.toString();
	}

	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private static StudentInTasks CreateStudentInTasksFromString( String data ) {
		String[] params = data.split( STUDENT_IN_TASK_SEP );
		String ident = params[0].trim();

		params = params[1].split( STUDENT_IN_TASK_DELIM );
		List<String> tasks = new ArrayList<String>( params.length );
		for ( int i = 0; i < params.length; i++ ) {
			tasks.add( params[i] );
		}

		return new StudentInTasks( ident, tasks );
	}

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

}
