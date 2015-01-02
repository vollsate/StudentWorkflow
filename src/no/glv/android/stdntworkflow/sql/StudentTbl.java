package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

class StudentTbl implements BaseColumns {
	
	private static final String TAG = StudentTbl.class.getSimpleName();

	public static final String TBL_NAME = "student";

	public static final String COL_IDENT = "ident";
	public static final String COL_CLASS = "class";
	public static final String COL_GRADE = "grade";
	public static final String COL_FNAME = "fname";
	public static final String COL_LNAME = "lname";
	public static final String COL_BIRTH = "birth";
	public static final String COL_ADR = "adr";
	public static final String COL_POSTALCODE = "pcode";

	public static final String COL_P1NAME = "p1name";
	public static final String COL_P1MAIL = "p1mail";
	public static final String COL_P1PHONE = "p1phone";

	public static final String COL_P2NAME = "p2name";
	public static final String COL_P2MAIL = "p2mail";
	public static final String COL_P2PHONE = "p2phone";

	private  StudentTbl() {
	}

	
	/**
	 * 
	 * @param db
	 */
	public static void CreateTableSQL( SQLiteDatabase db ) {
		String sql = "CREATE TABLE " + TBL_NAME + "(" + COL_IDENT + " TEXT PRIMARY KEY UNIQUE, " 
				+ COL_CLASS + " TEXT, " 
				+ COL_GRADE + " TEXT, "
				+ COL_FNAME + " TEXT, " 
				+ COL_LNAME + " TEXT, " 
				+ COL_BIRTH + " TEXT, "
				+ COL_ADR + " TEXT, " 
				+ COL_POSTALCODE + " TEXT, "
				+ COL_P1NAME + " TEXT, " 
				+ COL_P1MAIL + " TEXT, " 
				+ COL_P1PHONE + " TEXT, "
				+ COL_P2NAME + " TEXT, " 
				+ COL_P2MAIL + " TEXT, " 
				+ COL_P2PHONE + " TEXT)";
		
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
	public static List<Student> LoadStudentFromClass (String stdClass, SQLiteDatabase db ) {
		List<Student> list = new ArrayList<Student>();
		
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_CLASS + " = ?";
		Cursor cursor = db.rawQuery( sql, new String[] { stdClass} );
		cursor.moveToFirst();
		do {
			list.add( CreateFromCursor( cursor ) );
			cursor.moveToNext();
		}
		while ( ! cursor.isAfterLast());			
		
		return list;
	}
	
	
	private static Student CreateFromCursor( Cursor cursor ) {
		StudentBean bean = new StudentBean( null );
		
		bean.setIdent( cursor.getString( 0 ) );
		bean.setStudentClass( cursor.getString( 1 ) );
		bean.setGrade( cursor.getString( 2 ) );
		bean.setFirstName( cursor.getString( 3 ) );
		bean.setLastName( cursor.getString( 4 ) );
		bean.setBirth( cursor.getString( 5 ) );
		bean.setAdress( cursor.getString( 6 ) );
		bean.setPostalCode( cursor.getString( 7 ) );
		bean.setParent1Name( cursor.getString( 8 ) );
		bean.setParent1Mail( cursor.getString( 9 ) );
		bean.setParent1Phone( cursor.getString( 10 ) );
		bean.setParent2Name( cursor.getString( 11 ) );
		bean.setParent2Mail( cursor.getString( 12 ) );
		bean.setParent2Phone( cursor.getString( 13 ) );
		
		return bean;
	}
	
	/**
	 * 
	 * @param std
	 * @param db
	 */
	public static void InsertStudent( Student std, SQLiteDatabase db ) {
		ContentValues stdValues = StudentValues( std );
		
		long retVal = db.insert( StudentTbl.TBL_NAME, null, stdValues );
		Log.d( TAG, "Retval from InsertStudent: " + retVal );
		
		db.close();
	}
	
	/**
	 * 
	 * @param std
	 * @param db
	 */
	public static void UpdateStudent( Student std, SQLiteDatabase db ) {
		String sqlFiler = COL_IDENT + " = " + std.getIdent();
		ContentValues cv = StudentValues( std );
		
		db.update( TBL_NAME, cv, sqlFiler, null );
		db.close();
	}
	
	/**
	 * 
	 * @param ident
	 * @param db
	 */
	public static void DeleteStudent( String ident, SQLiteDatabase db ) {
		String sqlFilter = COL_IDENT + " = " + ident;
		
		db.delete( TBL_NAME, sqlFilter, null );
	}
	
	
	/**
	 * 
	 * @param std
	 * @return
	 */
	private static ContentValues StudentValues( Student std ) {
		ContentValues cv = new ContentValues();
		
		cv.put( COL_IDENT, std.getIdent() );
		cv.put( COL_CLASS, std.getStudentClass() );
		cv.put( COL_GRADE, std.getGrade() );
		cv.put( COL_FNAME, std.getFirstName() );
		cv.put( COL_LNAME, std.getLastname() );
		cv.put( COL_BIRTH, std.getBirth() );
		cv.put( COL_ADR, std.getAdress() );
		cv.put( COL_POSTALCODE, std.getPostalCode() );
		cv.put( COL_P1NAME, std.getParent1Name() );
		cv.put( COL_P1MAIL, std.getParent1Mail() );
		cv.put( COL_P1PHONE, std.getParent1Phone() );
		cv.put( COL_P2NAME, std.getParent2Name() );
		cv.put( COL_P2MAIL, std.getParent2Mail() );
		cv.put( COL_P2PHONE, std.getParent2Phone() );
		
		return cv;
	}
}
