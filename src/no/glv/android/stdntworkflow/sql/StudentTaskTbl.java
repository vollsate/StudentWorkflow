package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class StudentTaskTbl {

	private static final String TAG = StudentTaskTbl.class.getSimpleName();

	public static final String TBL_NAME = "stdntsk";

	public static final String COL_ID = "_ID";
	public static final String COL_TASK = "task";
	public static final String COL_IDENT = "ident";
	public static final String COL_DATE = "date";

	/** Weather or not student has delivered (IN | PENDING | EXPIRED | LATE ) */
	public static final String COL_MODE = "mode";

	private StudentTaskTbl() {
	}

	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_TASK + " TEXT NOT NULL, "
				+ COL_IDENT + " TEXT NOT NULL, "
				+ COL_DATE + " LONG, "
				+ COL_MODE + " LONG NOT NULL)";

		Log.v( TAG, "Executing SQL: " + sql );
		db.execSQL( sql );
	}

	/**
	 * 
	 * @param db
	 */
	public static void DropTable( SQLiteDatabase db ) {
		String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

		Log.v( TAG, "Executing SQL: " + sql );
		db.execSQL( sql );
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public static List<StudentTask> LoadAllInTask( SQLiteDatabase db, Task task ) {
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_TASK + "=?";

		Cursor cursor = db.rawQuery( sql, new String[] { task.getName() } );
		List<StudentTask> list = new ArrayList<StudentTask>();
		cursor.moveToFirst();

		while ( !cursor.isAfterLast() ) {
			list.add( CreateFromCursor( cursor ) );
			cursor.moveToNext();
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public static List<StudentTask> LoadAll( SQLiteDatabase db ) {
		String sql = "SELECT * FROM " + TBL_NAME;

		Cursor cursor = db.rawQuery( sql, null );
		List<StudentTask> list = new ArrayList<StudentTask>();
		cursor.moveToFirst();

		while ( !cursor.isAfterLast() ) {
			list.add( CreateFromCursor( cursor ) );
			cursor.moveToNext();
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param ident
	 * @param task
	 * @param db
	 * @return
	 */
	public static StudentTask Load( String ident, String task, SQLiteDatabase db ) {
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_TASK + "=? AND " + COL_IDENT + "=?";
		Cursor cursor = db.rawQuery( sql, new String[] { task, ident } );

		if ( cursor.getCount() > 1 )
			throw new IllegalStateException( "Too many StudentTask: ident=" + ident
					+ ", task=" + task );

		cursor.moveToFirst();
		return CreateFromCursor( cursor );
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private static StudentTask CreateFromCursor( Cursor cursor ) {
		int id = cursor.getInt( 0 );
		String task = cursor.getString( 1 );
		String ident = cursor.getString( 2 );
		long dateL = cursor.getLong( 3 );
		int mode = cursor.getInt( 4 );
		Date date = null;
		if ( dateL > 0 )
			date = new Date( dateL );

		StudentTaskImpl impl = new StudentTaskImpl( ident, task, mode, date );
		impl.setID( id );

		return impl;
	}

	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static int Insert( StudentTask task, SQLiteDatabase db ) {
		int retVal = InsertOneST( task, db );
		db.close();
		return retVal;
	}

	/**
	 * 
	 * @param task
	 * @param db
	 *            Does NOT close the connection
	 * @return
	 */
	private static int InsertOneST( StudentTask task, SQLiteDatabase db ) {
		long rowNum = db.insert( TBL_NAME, null, StudentTaskValues( task ) );

		task.setID( (int) rowNum );
		return (int) rowNum;
	}

	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static void InsertAll( Task task, SQLiteDatabase db ) {
		Iterator<StudentTask> it = task.getStudentsInTask().iterator();
		while ( it.hasNext() ) {
			StudentTask st = it.next();
			st.setTaskName( task.getName() );
			InsertOneST( st, db );
		}

		db.close();
	}

	/**
	 * 
	 * @param stdTask
	 * @param db
	 * @return
	 */
	public static long Update( StudentTask stdTask, SQLiteDatabase db ) {
		long retVal = 0;

		String whereClause = COL_IDENT + "=? AND " + COL_TASK + "=?";
		retVal = db.update( TBL_NAME, StudentTaskValues( stdTask ), whereClause, new String[] { stdTask.getIdent(),
				stdTask.getTaskName() } );

		return retVal;
	}

	/**
	 * 
	 * @param stdTask
	 * @param db
	 * @return
	 */
	public static int Delete( StudentTask stdTask, SQLiteDatabase db ) {
		int retVal = 0;

		String whereClause = COL_ID + "=?";
		Log.d( TAG, whereClause );

		retVal = (int) db.delete( TBL_NAME, whereClause, new String[] { String.valueOf( stdTask.getID() ) } );

		return retVal;
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	private static ContentValues StudentTaskValues( StudentTask task ) {
		ContentValues cv = new ContentValues();

		cv.put( COL_TASK, task.getTaskName() );
		cv.put( COL_IDENT, task.getIdent() );

		Date date = task.getHandInDate();
		long dateL = 0;
		if ( date != null )
			dateL = date.getTime();
		cv.put( COL_DATE, dateL );

		cv.put( COL_MODE, task.getMode() );

		return cv;
	}

}
