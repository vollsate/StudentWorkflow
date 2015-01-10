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

public class StudentInTaskTbl {

	private static final String TAG = StudentInTaskTbl.class.getSimpleName();

	public static final String TBL_NAME = "stdntsk";

	public static final String COL_TASK = "task";
	public static final String COL_IDENT = "ident";
	public static final String COL_DATE = "date";

	/** Weather or not student has delivered (IN | PENDING | EXPIRED | LATE ) */
	public static final String COL_MODE = "mode";


	private StudentInTaskTbl() {
	}

	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" 
				+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " 
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
	public static List<StudentTask> LoadAll( SQLiteDatabase db, Task task ) {
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_TASK + "=?";
		
		Cursor cursor = db.rawQuery( sql, new String[]{ task.getName() } );
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
			throw new IllegalStateException( "Too many StudentTask: ident=" + ident + ", task=" + task);
		
		cursor.moveToFirst();
		return  CreateFromCursor( cursor );
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private static StudentTask CreateFromCursor( Cursor cursor ) {
		String task = cursor.getString( 1 );
		String ident = cursor.getString( 2 );
		long dateL = cursor.getLong( 3 );
		int mode = cursor.getInt( 4 );
		Date date = null;
		if ( dateL > 0 ) date = new Date( dateL );
		
		StudentTaskImpl impl = new StudentTaskImpl( ident, task, mode, date );

		return impl;
	}
	
	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static long Insert( StudentTask task, SQLiteDatabase db ) {
		long retVal = InsertOneST( task, db );
		db.close();
		return retVal;
	}
	
	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	private static long InsertOneST( StudentTask task, SQLiteDatabase db ) {
		return db.insert( TBL_NAME, null, StudentInTaskValues( task ) );		
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
			
			StudentTaskImpl impl = new StudentTaskImpl( st.getIdent(), task.getName(), StudentTask.MODE_PENDING, null );
			InsertOneST( impl, db );
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
		retVal = db.update( TBL_NAME, StudentInTaskValues( stdTask ), whereClause, new String[] { stdTask.getIdent(), stdTask.getTaskName() } );
		
		return retVal;
	}

	/**
	 * 
	 * @param stdTask
	 * @param db
	 * @return
	 */
	public static long Delete( StudentTask stdTask, SQLiteDatabase db ) {
		long retVal = 0;
		
		String whereClause = COL_IDENT + "=? AND " + COL_TASK + "=?";
		retVal = db.delete( TBL_NAME, whereClause, new String[] { stdTask.getIdent(), stdTask.getTaskName() } );
		
		return retVal;
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	private static ContentValues StudentInTaskValues( StudentTask task ) {
		ContentValues cv = new ContentValues();
		
		cv.put( COL_TASK, task.getTaskName() );
		cv.put( COL_IDENT, task.getIdent() );
		
		Date date = task.getHandInDate();
		long dateL = 0;
		if ( date != null ) dateL = date.getTime();
		cv.put( COL_DATE, dateL );

		cv.put( COL_MODE, task.getMode() );

		return cv;
	}

}
