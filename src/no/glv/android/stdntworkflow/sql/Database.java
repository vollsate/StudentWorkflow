package no.glv.android.stdntworkflow.sql;

import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final String TAG = Database.class.getSimpleName();

	public static final int DB_VERSION = 1;

	public static final String DB_NAME = "stdwrkflw";

	private static Database instance;

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static Database GetInstance( Context ctx ) {
		if ( instance == null ) instance = new Database( ctx );

		return instance;
	}

	public Database( Context context ) {
		// public Database( Context context, String name, CursorFactory factory,
		// int version, DatabaseErrorHandler errorHandler )
		super( context, DB_NAME, null, DB_VERSION, null );
	}

	/**
	 * 
	 */
	public void runCreate() {
		onUpgrade( getWritableDatabase(), 1, 1 );
		onCreate( getWritableDatabase() );
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {
		Log.d( TAG, "Creating tables .. " );
		StudentTbl.CreateTableSQL( db );
		StudentClassTbl.CreateTable( db );
		TaskTbl.CreateTable( db );
		StudentInTaskTbl.CreateTable( db );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( TAG, "Dropping tables" );
		StudentTbl.DropTable( db );
		StudentClassTbl.DropTable( db );
		TaskTbl.DropTable( db );
		StudentInTaskTbl.DropTable( db );
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
	 * @param stdClass
	 * @return
	 */
	public List<Student> loadStudentsFromClass( String stdClass ) {
		return StudentTbl.LoadStudentFromClass( stdClass, getReadableDatabase() );
	}

	/**
	 * 
	 * @param student
	 */
	public void writeStudent( Student student ) {
		Log.d( TAG, "Writing student: " + student.getIdent() );
		StudentTbl.InsertStudent( student, getWritableDatabase() );
	}

	/**
	 * 
	 * @param std
	 * @param oldIdent
	 */
	public void updateStudent( Student std, String oldIdent ) {
		if ( ! std.getIdent().equals( oldIdent ) )
			removeStudent( oldIdent );

		Log.d( TAG, "Updating student: " + std.getIdent() );
		StudentTbl.UpdateStudent( std, getWritableDatabase() );
	}

	/**
	 * 
	 * @param ident
	 */
	public void removeStudent( String ident ) {
		Log.d( TAG, "Deleting student: " + ident );
		StudentTbl.DeleteStudent( ident, getWritableDatabase() );
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
	 * 
	 * @return
	 */
	public Task createNewTask() {
		return new TaskImpl();
	}
	
	/**
	 * 
	 * @param task
	 */
	public boolean writeTask( Task task ) {
		Log.d( TAG, "Inserting new task: " + task.getName() );
		boolean retVal = true;
		try {
			TaskTbl.InsertTask( task, getWritableDatabase() );			
			StudentInTaskTbl.InsertStudentTask( task, getWritableDatabase() );	
		}
		catch ( Exception e ) {
			Log.e( TAG, "Failure in adding task: " + task.getName(), e );
			retVal = false;
		}
		
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	public List<Task> loadTasks() {
		return TaskTbl.loadAllTasks( getReadableDatabase() );
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
	public List<StudentClass> loadStudentClasses() {
		try {
			SQLiteDatabase db = getReadableDatabase();		
			return StudentClassTbl.LoadStudentClasses( db );			
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "Cannot load studentClasses", e );
			throw e;
		}

	}

	/**
	 * 
	 * @param stdClass
	 */
	public void insertStudentClass( StudentClass stdClass ) {
		List<Student> list = stdClass.getStudents();
		Iterator<Student> it = list.iterator();

		while ( it.hasNext() ) {
			Student std = it.next();
			std.setStudentClass( stdClass.getName() );
			writeStudent( std );
		}

		StudentClassTbl.InsertStudentClass( stdClass, getWritableDatabase() );
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	//
	// STUDENT IN TASK
	//
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	
}
