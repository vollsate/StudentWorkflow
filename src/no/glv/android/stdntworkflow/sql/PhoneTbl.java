package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.Student;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

class PhoneTbl implements BaseColumns {
	
	private static final String TAG = PhoneTbl.class.getSimpleName();

	public static final String TBL_NAME = "phone";

	/** String (student ident) */
	public static final String COL_ID = "_id";
	public static final int COL_ID_ID = 0;

	/** INTEGER */
	public static final String COL_PHONE = "phone";
	public static final int COL_PHONE_ID = 1;

	/** INTEGER */	 
	public static final String COL_TYPE = "type";
	public static final int COL_TYPE_ID = 2;

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
				+ COL_ID + " STRING PRIMARY KEY UNIQUE, " 
				+ COL_PHONE + " TEXT, "
				+ COL_TYPE + " LONG)";
		
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
	public static List<Phone> LoadParentPhone (String parentID, SQLiteDatabase db ) {
		List<Phone> list = new ArrayList<Phone>();
		
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_ID + " = ?";
		Cursor cursor = db.rawQuery( sql, new String[] { parentID } );
		cursor.moveToFirst();
		while ( ! cursor.isAfterLast() ) {
			list.add( CreateFromCursor( cursor ) );
			cursor.moveToNext();
		}
		
		cursor.close();
		db.close();
		
		return list;
	}
	
	
	private static Phone CreateFromCursor( Cursor cursor ) {
		Phone phone = new PhoneBean( cursor.getString( COL_TYPE_ID ), cursor.getInt( COL_TYPE_ID ) );
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
	 * @return 1 if successfull, 0 otherwise
	 */
	public static int UpdatePhone( Phone phone, SQLiteDatabase db ) {
		String sqlFiler = COL_ID + " = ?";
		ContentValues cv = PhoneValues( phone );
		
		int retVal = db.update( TBL_NAME, cv, sqlFiler, new String[] { phone.getID() } );
		db.close();
		
		return retVal;
	}
	
	/**
	 * 
	 * @param ident
	 * @param db
	 */
	public static int DeletePhone( String ident, SQLiteDatabase db ) {
		String sqlFilter = COL_ID + " = ?";
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
		
		cv.put( COL_ID, phone.getID() );
		cv.put( COL_PHONE, phone.getNumber() );
		cv.put( COL_TYPE, phone.getType() );
		
		return cv;
	}
}
