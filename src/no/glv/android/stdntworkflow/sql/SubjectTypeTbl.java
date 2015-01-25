package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.SubjectType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

class SubjectTypeTbl implements BaseColumns {

	private static final String TAG = SubjectTypeTbl.class.getSimpleName();

	public static final String TBL_NAME = "sbjcttp";

	public static final String COL_ID = "_id";
	public static final int COL_ID_ID = 0;

	/** String - name of subject/type */
	public static final String COL_NAME = "name";
	public static final int COL_NAME_ID = 1;

	/** String - description */
	public static final String COL_DESC = "desc";
	public static final int COL_DESC_ID = 2;

	/** INTEGER */
	public static final String COL_TYPE = "type";
	public static final int COL_TYPE_ID = 3;

	private SubjectTypeTbl() {
	}

	/**
	 * Called as part of initiation of the entire DATABASE.
	 * 
	 * DO NOT CLOSE THE SQLiteDatabase
	 * 
	 * @param db
	 *            Do not close!
	 */
	static void CreateTable( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_NAME + " TEXT NOT NULL, "
				+ COL_DESC + " TEXT, "
				+ COL_TYPE + " INTEGER)";

		Log.v( TAG, "Executing SQL: " + sql );
		db.execSQL( sql );
	}

	public static void DropTable( SQLiteDatabase db ) {
		String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

		Log.v( TAG, "Executing SQL: " + sql );
		db.execSQL( sql );
	}

	/**
	 * 
	 * @param stdClass
	 * @param db
	 * @return
	 */
	public static List<SubjectType> LoadSubjectTypes( SQLiteDatabase db ) {
		List<SubjectType> list = new ArrayList<SubjectType>();

		String sql = "SELECT * FROM " + TBL_NAME;
		Log.d( TAG, "Executing SQL: " + sql );

		try {
			Cursor cursor = db.rawQuery( sql, null );
			cursor.moveToFirst();
			while ( !cursor.isAfterLast() ) {
				list.add( CreateFromCursor( cursor ) );
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch ( Exception e ) {
			Log.e( TAG, "Error getting phonerecord", e );
		}

		db.close();

		return list;
	}

	private static SubjectType CreateFromCursor( Cursor cursor ) {
		SubjectTypeBean st = new SubjectTypeBean();

		st._id = cursor.getInt( COL_ID_ID );
		st.setName( cursor.getString( COL_NAME_ID ) );
		st.setDescription( cursor.getString( COL_DESC_ID ) );
		st.setType( cursor.getInt( COL_TYPE_ID ) );

		return st;
	}

	/**
	 * 
	 * @param std
	 * @param db
	 *            Is closed after use
	 */
	public static long Insert( SubjectType st, SQLiteDatabase db ) {
		return Insert( st, db, true );
	}

	public static long Insert( List<SubjectType> list, SQLiteDatabase db ) {
		int count = 0;
		for ( SubjectType st : list ) {
			count += (int) Insert( st, db, false );
		}

		db.close();
		return count;
	}

	/**
	 * 
	 * @param st
	 * @param db
	 * @param close
	 * @return
	 */
	private static long Insert( SubjectType st, SQLiteDatabase db, boolean close ) {
		ContentValues stValues = STValues( st );

		long retVal = db.insert( TBL_NAME, null, stValues );
		( (SubjectTypeBean)st)._id = (int)retVal;
		
		if ( close )
			db.close();

		return retVal;
	}

	/**
	 * 
	 * @param st
	 * @param db
	 *            Is closed after use
	 * 
	 * @return 1 if successful, 0 otherwise
	 */
	public static int Update( SubjectType st, SQLiteDatabase db ) {
		String sqlFiler = COL_NAME + " = ?";
		ContentValues cv = STValues( st );

		int retVal = db.update( TBL_NAME, cv, sqlFiler, new String[] { String.valueOf( st.getID() ) } );
		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param ident
	 * @param db
	 */
	public static int Delete( int id, SQLiteDatabase db ) {
		String sqlFilter = COL_NAME + " = ?";
		int retVal = db.delete( TBL_NAME, sqlFilter, new String[] { String.valueOf( id ) } );
		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param st
	 * @return
	 */
	private static ContentValues STValues( SubjectType st ) {
		ContentValues cv = new ContentValues();

		int id = st.getID();
		if ( id > 0 )
			cv.put( COL_ID, id );
		cv.put( COL_NAME, st.getName() );
		cv.put( COL_DESC, st.getDescription() );
		cv.put( COL_TYPE, st.getType() );

		return cv;
	}
}
