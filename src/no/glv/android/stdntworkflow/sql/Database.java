package no.glv.android.stdntworkflow.sql;

import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.core.DBException;
import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.SubjectType;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The main entry point into the database. This will keep only one instance, and
 * throw an exception is somebody tries to instantiate the database more than
 * once.
 * 
 * @author glevoll
 *
 */
public class Database extends SQLiteOpenHelper {

	private static final String TAG = Database.class.getSimpleName();

	public static final int DB_VERSION = 1;

	public static final String DB_NAME = "stdwrkflw";

	/**
	 * The singleton instance used to prevent more than one instance
	 */
	private static Database instance;

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static Database GetInstance( Context ctx ) {
		if ( instance == null )
			instance = new Database( ctx );

		return instance;
	}

	/**
	 * 
	 * @param context The context used to create the database.
	 * @throws IllegalStateException is database already instantiated
	 */
	public Database( Context context ) {
		super( context, DB_NAME, null, DB_VERSION, null );

		if ( instance != null )
			throw new IllegalStateException();
		if ( instance == null )
			instance = this;
	}

	/**
	 * 
	 */
	void deleteSubjectTypes() {
		SubjectTypeTbl.DropTable( getWritableDatabase() );
		SubjectTypeTbl.CreateTable( getWritableDatabase() );
	}

	/**
     * 
     */
	public void runCreate() {
		SQLiteDatabase db = getWritableDatabase();

		onUpgrade( db, 1, 1 );
		onCreate( db );

		db.close();
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {
		Log.d( TAG, "Creating tables .. " );
		StudentTbl.CreateTableSQL( db );
		StudentClassTbl.CreateTable( db );
		TaskTbl.CreateTable( db );
		StudentTaskTbl.CreateTable( db );
		PhoneTbl.CreateTable( db );
		ParentTbl.CreateTableSQL( db );
		SubjectTypeTbl.CreateTable( db );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.d( TAG, "Dropping tables .." );
		StudentTbl.DropTable( db );
		StudentClassTbl.DropTable( db );
		TaskTbl.DropTable( db );
		StudentTaskTbl.DropTable( db );
		PhoneTbl.DropTable( db );
		ParentTbl.DropTable( db );
		SubjectTypeTbl.DropTable( db );
	}

	/**
	 * 
	 */
	public void cleanupDB() {
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

	public List<Parent> loadParents( String id ) {
		return ParentTbl.LoadParent( id, getReadableDatabase() );
	}

	public List<Phone> loadPhone( String stdID, String parentID ) {
		return PhoneTbl.LoadParentPhone( stdID, parentID, getReadableDatabase() );
	}

	/**
	 * 
	 * @param stdClass
	 * @return
	 */
	public List<Student> loadStudentsFromClass( String stdClass ) {
		return StudentTbl.LoadStudentFromClass( stdClass, getReadableDatabase() );
	}

	/**
	 * Will insert the {@link Student} in the StudentTbl and every
	 * {@link Parent} and {@link Phone} related to the student.
	 * 
	 * @param student
	 */
	public boolean insertStudent( Student student ) {
		Log.d( TAG, "Inserting student: " + student.getIdent() );
		boolean success = true;

		StudentTbl.Insert( student, getWritableDatabase() );

		Iterator<Parent> it = student.getParents().iterator();
		while ( it.hasNext() ) {
			insertParent( it.next() );
		}

		return success;
	}

	/**
	 * 
	 * @param parent
	 */
	public void insertParent( Parent parent ) {
		ParentTbl.InsertParent( parent, getWritableDatabase() );

		if ( parent.getPhoneNumbers() == null )
			return;

		Iterator<Phone> pIt = parent.getPhoneNumbers().iterator();
		while ( pIt.hasNext() ) {
			Phone p = pIt.next();
			p.setParentID( parent.getID() );
			PhoneTbl.Insert( p, getWritableDatabase() );
		}
	}

	/**
	 * 
	 * @param std
	 * @param oldIdent
	 */
	public int updateStudent( Student std, String oldIdent ) {
		if ( !std.getIdent().equals( oldIdent ) )
			removeStudent( oldIdent );

		Log.d( TAG, "Updating student: " + std.getIdent() );
		return StudentTbl.Update( std, getWritableDatabase() );
	}

	/**
	 * 
	 * @param ident
	 */
	public int removeStudent( String ident ) {
		Log.d( TAG, "Deleting student: " + ident );
		return StudentTbl.Delete( ident, getWritableDatabase() );
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

	public SubjectType createSubjectType() {
		return new SubjectTypeBean();
	}

	public List<SubjectType> loadSubjectTypes() {
		return SubjectTypeTbl.LoadAll( getReadableDatabase() );
	}

	/**
	 * 
	 * @param st
	 * @return
	 * @throws SQLException if the {@link SubjectType} cannot be inserted
	 */
	public boolean insertSubjectType( SubjectType st ) {
		long retVal = SubjectTypeTbl.Insert( st, getWritableDatabase() );

		if ( retVal == -1 )
			throw new SQLException( "Error inserting SubjectType: " + st.toString() );

		return retVal >= 1;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public boolean insertSubjectTypes( List<SubjectType> list ) {
		int rows = (int) SubjectTypeTbl.Insert( list, getWritableDatabase() );

		if ( rows < list.size() - 1 )
			throw new SQLException( "Error inserting all rows. Data unstable!" );

		return true;
	}

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
	public boolean insertTask( Task task ) {
		Log.d( TAG, "Inserting new task: " + task.getName() );
		boolean retVal = true;
		try {
			int id = TaskTbl.InsertTask( task, getWritableDatabase() );
			( (TaskImpl) task ).setID( id );

			StudentTaskTbl.InsertAll( task, getWritableDatabase() );
		}
		catch ( Exception e ) {
			Log.e( TAG, "Failure in adding task: " + task.getName(), e );
			retVal = false;
		}

		return retVal;
	}

	/**
	 * 
	 * @param stdTask
	 * @return
	 */
	public boolean updateStudentTask( StudentTask stdTask ) {
		if ( StudentTaskTbl.Update( stdTask, getWritableDatabase() ) != 1 )
			return false;

		return true;
	}

	/**
	 * Loads all the tasks in the database. Does not differentiate between
	 * states.
	 * 
	 * @return
	 */
	public List<Task> loadTasks() {
		return TaskTbl.loadAllTasks( getReadableDatabase() );
	}

	/**
	 * 
	 * @param task
	 * @param oldName
	 * @return
	 */
	public boolean updateTask( Task task ) {
		int rows = 0;

		try {
			rows = TaskTbl.updateTask( task, getWritableDatabase() );
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "Cannot update task: " + task.getName(), e );
			e.printStackTrace();
		}

		Log.d( TAG, "Updated " + rows + " rows" );

		return rows > 0;
	}

	/**
	 * 
	 * @param stdTasks
	 */
	public void updateStudentTasks( List<StudentTask> stdTasks ) {
		Iterator<StudentTask> it = stdTasks.iterator();

		while ( it.hasNext() ) {
			StudentTask stdTask = it.next();
			StudentTaskTbl.Update( stdTask, getWritableDatabase() );
		}
	}

	/**
	 * 
	 * @param stdTasks
	 */
	public void deleteStudentTasks( List<StudentTask> stdTasks ) {
		Iterator<StudentTask> it = stdTasks.iterator();

		while ( it.hasNext() ) {
			StudentTask stdTask = it.next();
			int rows = StudentTaskTbl.Delete( stdTask, getWritableDatabase() );
			if ( rows == 0 || rows > 1 ) {
				if ( rows == 0 )
					throw new DBException( "Unable to delete StudentTask: " + stdTask.toSimpleString() );

				if ( rows > 1 )
					throw new DBException( "More than one row deleted when deleting StudentTask: "
							+ stdTask.toSimpleString() );
			}
		}
	}

	/**
	 * 
	 * @param stdTasks
	 */
	public void insertStudentTasks( List<StudentTask> stdTasks ) {
		Iterator<StudentTask> it = stdTasks.iterator();

		while ( it.hasNext() ) {
			StudentTask stdTask = it.next();
			int rows = StudentTaskTbl.Insert( stdTask, getWritableDatabase() );
			if ( rows == 0 ) {
				if ( rows == 0 )
					throw new DBException( "Unable to insert StudentTask: " + stdTask.toSimpleString() );
			}
		}
	}

	/**
	 * 
	 * @param taskName
	 * @return
	 */
	public boolean deleteTask( Task task ) {
		int rows = 0;

		try {
			rows = TaskTbl.DeleteTask( task, getWritableDatabase() );
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "Cannot update task: " + task.getName(), e );
			e.printStackTrace();
		}

		Log.d( TAG, "Detelted " + rows + " rows" );

		return rows > 0;
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
		StudentClassTbl.InsertStudentClass( stdClass, getWritableDatabase() );

		List<Student> list = stdClass.getStudents();
		Iterator<Student> it = list.iterator();

		while ( it.hasNext() ) {
			Student std = it.next();
			std.setStudentClass( stdClass.getName() );
			insertStudent( std );
		}
	}

	/**
	 * 
	 * @param stdClass
	 * @return
	 */
	public long deleteStdClass( StudentClass stdClass ) {
		List<Student> list = stdClass.getStudents();
		for ( int i = 0; i < list.size(); i++ ) {
			Student std = list.get( i );
			StudentTbl.Delete( std.getIdent(), getWritableDatabase() );
		}

		return StudentClassTbl.Delete( stdClass.getName(), getWritableDatabase() );
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

	/**
	 * 
	 * @param task
	 * @return
	 */
	public List<StudentTask> loadStudentsInTask( Task task ) {
		return StudentTaskTbl.LoadAllInTask( getReadableDatabase(), task );
	}

	/**
	 * 
	 * @return
	 */
	public List<StudentTask> loadAllStudentTask() {
		return StudentTaskTbl.LoadAll( getReadableDatabase() );
	}

}
