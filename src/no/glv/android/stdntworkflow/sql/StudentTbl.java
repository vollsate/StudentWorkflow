package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

class StudentTbl implements BaseColumns {

    public static final String TBL_NAME = "student";

    public static final String COL_IDENT = "ident";
    public static final int COL_IDENT_ID = 0;

    public static final String COL_CLASS = "class";
    public static final int COL_CLASS_ID = 1;

    public static final String COL_GRADE = "grade";
    public static final int COL_GRADE_ID = 2;

    public static final String COL_FNAME = "fname";
    public static final int COL_FNAME_ID = 3;

    public static final String COL_LNAME = "lname";
    public static final int COL_LNAME_ID = 4;

    public static final String COL_BIRTH = "birth";
    public static final int COL_BIRTH_ID = 5;

    public static final String COL_ADR = "adr";
    public static final int COL_ADR_ID = 6;

    public static final String COL_POSTALCODE = "pcode";
    public static final int COL_POSTALCODE_ID = 7;

    public static final String COL_PHONE = "phone";
    public static final int COL_PHONE_ID = 8;

    private StudentTbl() {
    }

    /**
     * Called as part of initiation of the entire DATABASE.
     * 
     * DO NOT CLOSE THE SQLiteDatabase
     * 
     * @param db Do not close!
     */
    static void CreateTableSQL( SQLiteDatabase db ) {
	String sql = "CREATE TABLE " + TBL_NAME + "("
		+ COL_IDENT + " TEXT PRIMARY KEY UNIQUE, "
		+ COL_CLASS + " TEXT, "
		+ COL_GRADE + " TEXT, "
		+ COL_FNAME + " TEXT, "
		+ COL_LNAME + " TEXT, "
		+ COL_BIRTH + " TEXT, "
		+ COL_ADR + " TEXT, "
		+ COL_POSTALCODE + " TEXT, "
		+ COL_PHONE + " TEXT)";

	DBUtils.ExecuteSQL( sql, db );
    }

    public static void DropTable( SQLiteDatabase db ) {
	String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

	DBUtils.ExecuteSQL( sql, db );
    }

    /**
     * 
     * @param stdClass
     * @param db
     * @return
     */
    public static List<Student> LoadStudentFromClass( String stdClass, SQLiteDatabase db ) {
	List<Student> list = new ArrayList<Student>();

	String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_CLASS + " = ?";
	Cursor cursor = db.rawQuery( sql, new String[] { stdClass } );
	cursor.moveToFirst();
	while ( !cursor.isAfterLast() ) {
	    list.add( CreateFromCursor( cursor ) );
	    cursor.moveToNext();
	}

	cursor.close();
	db.close();

	return list;
    }

    private static Student CreateFromCursor( Cursor cursor ) {
	StudentBean bean = new StudentBean( null );

	bean.setIdent( cursor.getString( COL_IDENT_ID ) );
	bean.setStudentClass( cursor.getString( COL_CLASS_ID ) );
	bean.setGrade( cursor.getString( COL_GRADE_ID ) );
	bean.setFirstName( cursor.getString( COL_FNAME_ID ) );
	bean.setLastName( cursor.getString( COL_LNAME_ID ) );
	bean.setBirth( cursor.getString( COL_BIRTH_ID ) );
	bean.setAdress( cursor.getString( COL_ADR_ID ) );
	bean.setPostalCode( cursor.getString( COL_POSTALCODE_ID ) );
	bean.setPhone( cursor.getString( COL_PHONE_ID ) );

	return bean;
    }

    /**
     * 
     * @param std
     * @param db
     */
    public static long Insert( Student std, SQLiteDatabase db ) {
	ContentValues stdValues = StudentValues( std );

	long retVal = db.insert( TBL_NAME, null, stdValues );
	db.close();

	return retVal;
    }

    /**
     * 
     * @param std
     * @param db Is closed after use
     * 
     * @return 1 if successful, 0 otherwise
     */
    public static int Update( Student std, SQLiteDatabase db ) {
	String sqlFiler = COL_IDENT + " = ?";
	ContentValues cv = StudentValues( std );

	int retVal = db.update( TBL_NAME, cv, sqlFiler, new String[] { std.getIdent() } );
	db.close();

	return retVal;
    }

    /**
     * 
     * @param ident
     * @param db
     */
    public static int Delete( String ident, SQLiteDatabase db ) {
	String sqlFilter = COL_IDENT + " = ?";
	int retVal = db.delete( TBL_NAME, sqlFilter, new String[] { ident } );
	db.close();

	return retVal;
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
	cv.put( COL_LNAME, std.getLastName() );
	cv.put( COL_BIRTH, std.getBirth() );
	cv.put( COL_ADR, std.getAdress() );
	cv.put( COL_POSTALCODE, std.getPostalCode() );
	cv.put( COL_PHONE, std.getPhone() );

	return cv;
    }
}
