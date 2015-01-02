package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class StudentClassTbl {

	private static final String TAG = StudentClassTbl.class.getSimpleName();

	public static final String TBL_NAME = "stdclass";

	public static final String COL_NAME = "name";

	private StudentClassTbl() {
	}

	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" + COL_NAME + " TEXT PRIMARY KEY)";

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
	public static List<StudentClass> LoadStudentClasses( SQLiteDatabase db ) {
		List<StudentClass> list = new ArrayList<StudentClass>();

		String sql = "SELECT * FROM " + TBL_NAME;
		Log.v( TAG, "Executing SQL: " + sql );

		Cursor cursor = db.rawQuery( sql, null );
		Log.d( TAG, "Count studentclasses: " + cursor.getCount() );
		
		if ( cursor.moveToFirst() ) {
			do {
				list.add( CreateFromCursor( cursor ) );
				cursor.moveToNext();
			}
			while ( !cursor.isAfterLast() );
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private static StudentClass CreateFromCursor( Cursor cursor ) {
		StudentClassImpl bean = new StudentClassImpl( cursor.getString( 0 ) );

		return bean;
	}
	
	/**
	 * 
	 * @param stdClass
	 * @param db
	 */
	public static void InsertStudentClass( StudentClass stdClass, SQLiteDatabase db ) {
		ContentValues cv = StudentClassValues( stdClass );
		
		long retVal = db.insert( TBL_NAME, null, cv );
		Log.d( TAG, "Retval from InsertStudentClass: " + retVal );

		db.close();
	}

	/**
	 * 
	 * @param stdClass
	 * @return
	 */
	private static ContentValues StudentClassValues( StudentClass stdClass ) {
		ContentValues cv = new ContentValues();
		
		cv.put( COL_NAME, stdClass.getName() );

		return cv;
	}

}
