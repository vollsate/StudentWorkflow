package no.glv.android.stdntworkflow.sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtils implements BaseValues {
	
	private static final String TAG = DBUtils.class.getSimpleName();
	private static final SimpleDateFormat sdf = new SimpleDateFormat( DATE_PATTERN, Locale.getDefault() );

	private DBUtils() {
	}
	
	/**
	 * 
	 * @param sql
	 * @param db Is NOT closed!
	 * @return
	 */
	public static final boolean ExecuteSQL( String sql, SQLiteDatabase db ) {
		Log.v( TAG, "Executing SQL: " + sql );
		try {
			db.execSQL( sql );
		}
		catch ( RuntimeException e ) {
			Log.e( TAG, "Error executing SQL: " + sql, e );
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String ConvertToString( Date date ) {
		return sdf.format( date );
	}
	
	public static Date ConvertStringToDate( String date, String pattern ) {
		SimpleDateFormat simpleDF = sdf;
		if ( pattern != null ) simpleDF = new SimpleDateFormat( pattern, Locale.getDefault() );
		
		try {
			return simpleDF.parse( date );
		}
		catch ( ParseException e ) {
			Log.e( TAG, "Error parsing date: " + date, e );
			return null;
		}
	}
	
}
