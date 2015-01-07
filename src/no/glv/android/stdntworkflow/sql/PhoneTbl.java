package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Phone;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

class PhoneTbl implements BaseColumns {
	
	private static final String TAG = PhoneTbl.class.getSimpleName();

	public static final String TBL_NAME = "phone";

	public static final String COL_ID = "_id";
	public static final int COL_ID_ID = 0;

	/** String (student ident) */
	public static final String COL_STDID = "stdid";
	public static final int COL_STDID_ID = 1;

	public static final String COL_PARENTID = "parentid";
	public static final int COL_PARENTID_ID = 2;

	/** INTEGER */
	public static final String COL_PHONE = "phone";
	public static final int COL_PHONE_ID = 3;

	/** INTEGER */	 
	public static final String COL_TYPE = "type";
	public static final int COL_TYPE_ID = 4;

	private  PhoneTbl() {
	}

	
	/**
	 * Called as part of initiation of the entire DATABASE.
	 * 
	 *  DO NOT CLOSE THE SQLiteDatabase
	 * 
	 * @param db Do not close!
	 */
	static void CreateTableSQL( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" 
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ COL_STDID + " TEXT NOT NULL, " 
				+ COL_PARENTID + " TEXT NOT NULL, " 
				+ COL_PHONE + " LONG, "
				+ COL_TYPE + " INTEGER)";
		
		Log.v( TAG, "Executing SQL: " + sql );		
		db.execSQL( sql );
	}
	
	
	public static void DropTable( SQLiteDatabase db ) {
		String sql = "DROP TABLE IF EXISTS " +  TBL_NAME; 
		
		Log.v( TAG, "Executing SQL: " + sql );
		db.execSQL( sql );
	}
	
	
	/**
	 * 
	 * @param stdClass
	 * @param db
	 * @return
	 */
	public static List<Phone> LoadParentPhone (String stdID, String parentID, SQLiteDatabase db ) {
		List<Phone> list = new ArrayList<Phone>();
		
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_STDID + " = ? AND " + COL_PARENTID + "=?";
		Log.d( TAG, "Executing SQL: " + sql );
		
		try {
			//Cursor cursor = db.query( TBL_NAME, new String[] { COL_STDID, COL_PARNETID }, null, new String[] { stdID, parentID }, null, null, null, null );
			Cursor cursor = db.rawQuery( sql, new String[] { stdID, parentID } );
			cursor.moveToFirst();
			while ( ! cursor.isAfterLast() ) {
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
	
	
	private static Phone CreateFromCursor( Cursor cursor ) {
		Phone phone = new PhoneBean( cursor.getInt( COL_TYPE_ID ) );
		
		phone.setStudentID( cursor.getString( COL_STDID_ID ) );
		phone.setParentID( cursor.getString( COL_PARENTID_ID ) );
		phone.setNumber( cursor.getLong( COL_PHONE_ID ) );
		
		return phone;
	}
	
	/**
	 * 
	 * @param std
	 * @param db Is closed after use
	 */
	public static long InsertPhone( Phone phone, SQLiteDatabase db ) {
		ContentValues phoneValues = PhoneValues( phone );
		
		long retVal = db.insert( TBL_NAME, null, phoneValues );
		db.close();
		
		return retVal;
	}
	
	/**
	 * 
	 * @param phone
	 * @param db Is closed after use
	 * 
	 * @return 1 if successful, 0 otherwise
	 */
	public static int UpdatePhone( Phone phone, SQLiteDatabase db ) {
		String sqlFiler = COL_STDID + " = ?";
		ContentValues cv = PhoneValues( phone );
		
		int retVal = db.update( TBL_NAME, cv, sqlFiler, new String[] { phone.getStudentID() } );
		db.close();
		
		return retVal;
	}
	
	/**
	 * 
	 * @param ident
	 * @param db
	 */
	public static int DeletePhone( String ident, SQLiteDatabase db ) {
		String sqlFilter = COL_STDID + " = ?";
		int retVal = db.delete( TBL_NAME, sqlFilter, new String[] { ident } );
		db.close();
		
		return retVal;
	}
	
	
	/**
	 * 
	 * @param phone
	 * @return
	 */
	private static ContentValues PhoneValues( Phone phone ) {
		ContentValues cv = new ContentValues();
		
		cv.put( COL_STDID, phone.getStudentID() );
		cv.put( COL_PARENTID, phone.getParentID() );
		cv.put( COL_PHONE, phone.getNumber() );
		cv.put( COL_TYPE, phone.getType() );
		
		return cv;
	}
}
