package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
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
	
	/** Weather or not student has delivered (IN | PENDING | EXPIRED | LATE ) */
	public static final String COL_MODE = "mode";
	
	
	
	
	public static final int MODE_HANDIN = 0;
	public static final int MODE_PENDING = 2;
	public static final int MODE_EXPIRED = 4;
	public static final int MODE_LATE = 8;
	

	
	private StudentInTaskTbl() {
	}
	
	
	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" 
				+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT "
				+ COL_MODE + " TEXT NOT NULL "
				+ COL_IDENT + " TEXT, "
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
		String sql = "SELECT * FROM " + TBL_NAME;
		
		Cursor cursor = db.rawQuery( sql, null );
		List<StudentTask> list = new ArrayList<StudentTask>();

		if ( cursor.moveToFirst() ) {
			do {
				list.add( CreateFromCursor( cursor ) );
				cursor.moveToNext();
			}
			while ( ! cursor.isAfterLast() );
		}
		
		cursor.close();
		return list;
	}
	
	private static StudentTask CreateFromCursor( Cursor cursor ) {
		String task = cursor.getString( 0 );
		String ident = cursor.getString( 1 );
		StudentTaskImpl impl = new StudentTaskImpl( ident, task );	

		return impl;
	}
	
	
	
	public static ContentValues TaksValues( Task task ) {
		ContentValues cv = new ContentValues();
		
		return cv;
	}

}
