package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * A static class that works towards a SQLite database. Takes care of loading,
 * inserting, deleting and updating tasks.
 * 
 * @author GleVoll
 *
 */
public class TaskTbl {

    /** Name of the TABLE in the SQLite database */
    public static final String TBL_NAME = "tasks";

    public static final String COL_NAME = "name";
    public static final String COL_DESC = "desc";
    public static final String COL_DATE = "date";
    public static final String COL_STATE = "type";

    private TaskTbl() {
    }

    /**
     * 
     * @param db
     *            Is NOT closed after use!
     */
    public static boolean CreateTable( SQLiteDatabase db ) {
	String sql = "CREATE TABLE " + TBL_NAME + "(" + COL_NAME + " TEXT PRIMARY KEY UNIQUE, " + COL_DESC + " TEXT, "
		+ COL_DATE + " LONG NOT NULL, " + COL_STATE + " INTEGER)";

	return DBUtils.ExecuteSQL( sql, db );
    }

    /**
     * 
     * @param db
     *            Is NOT closed after use!
     */
    public static boolean DropTable( SQLiteDatabase db ) {
	return DBUtils.ExecuteSQL( "DROP TABLE IF EXISTS " + TBL_NAME, db );
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
	task.setState( cursor.getInt( 3 ) );

	return task;
    }

    /**
     * 
     * @param task
     * @param db
     *            Is closed after use
     * @return
     */
    public static long InsertTask( Task task, SQLiteDatabase db ) {
	long retVal = -1;

	ContentValues cv = TaksValues( task );
	retVal = db.insertOrThrow( TBL_NAME, null, cv );

	db.close();
	return retVal;
    }

    /**
     * 
     * @param task
     * @return
     */
    private static ContentValues TaksValues( Task task ) {
	ContentValues cv = new ContentValues();

	cv.put( COL_NAME, task.getName() );
	cv.put( COL_DESC, task.getDesciption() );
	cv.put( COL_DATE, task.getDate().getTime() );
	cv.put( COL_STATE, task.getState() );

	return cv;
    }

    /**
     * Updates a specific task
     * 
     * @param task
     *            The {@link Task} to update, with all the new values
     * @param oldName
     *            The name of the old task. If null, the current name is used.
     * @param db
     *            Is closed after use
     * @return 0 if no update, otherwise 1
     */
    public static int updateTask( Task task, String oldName, SQLiteDatabase db ) {
	int retVal = 0;

	ContentValues cv = TaksValues( task );
	String whereClause = COL_NAME + "=?";
	if ( oldName == null ) oldName = task.getName();

	retVal = db.update( TBL_NAME, cv, whereClause, new String[] { oldName } );

	db.close();

	return retVal;
    }

    /**
     * 
     * @param taskName
     * @param db
     *            Is closed after use
     * 
     * @return 1 if task deleted, 0 otherwise
     */
    public static int DeleteTask( String taskName, SQLiteDatabase db ) {
	String whereClause = COL_NAME + "=?";
	int retVal = db.delete( TBL_NAME, whereClause, new String[] { taskName } );

	db.close();
	return retVal;
    }

}
