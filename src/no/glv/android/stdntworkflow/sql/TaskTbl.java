package no.glv.android.stdntworkflow.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskTbl implements BaseValues {

	private static final String TAG = TaskTbl.class.getSimpleName();

	public static final String TBL_NAME = "tasks";

	public static final String COL_NAME = "name";
	public static final String COL_DESC = "desc";
	public static final String COL_DATE = "date";
	public static final String COL_TYPE = "type";

	private TaskTbl() {
	}

	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" 
				+ COL_NAME + " TEXT PRIMARY KEY UNIQUE, "
				+ COL_DESC + " TEXT, "
				+ COL_DATE + " LONG NOT NULL, " 
				+ COL_TYPE + " INTEGER)";

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
	public static List<Task> loadAllTasks( SQLiteDatabase db ) {
		String sql = "SELECT * FROM " + TBL_NAME;
		List<Task> list = new ArrayList<Task>();
		Cursor cursor = db.rawQuery( sql, null );
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
	 * @param cursor
	 * @return
	 */
	private static Task CreateFromCursor( Cursor cursor ) {
		Task task = new TaskImpl();

		task.setName( cursor.getString( 0 ) );
		task.setDescription( cursor.getString( 1 ) );
		task.setDate( new Date( cursor.getLong( 2 ) ) );
		task.setType( cursor.getInt( 3 ) );

		return task;
	}
	
	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static long InsertTask( Task task, SQLiteDatabase db ) {
		ContentValues cv = TaksValues( task );
		return db.insertOrThrow( TBL_NAME, null, cv );
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	public static ContentValues TaksValues( Task task ) {
		ContentValues cv = new ContentValues();
		
		cv.put( COL_NAME, task.getName() );
		cv.put( COL_DESC, task.getDesciption() );
		cv.put( COL_DATE, ConcertToString( task.getDate() ) );
		cv.put( COL_TYPE, task.getType() );

		return cv;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private static String ConcertToString( Date date ) {
		SimpleDateFormat sdf = new SimpleDateFormat( DATE_PATTERN, Locale.getDefault() );
		return sdf.format( date );
	}
}
