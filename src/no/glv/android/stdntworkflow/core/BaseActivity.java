/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * @author GleVoll
 *
 */
public class BaseActivity extends ActionBarActivity {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN, Locale.getDefault() );
	
	private Bundle bundle;
	
	/**
	 * 
	 */
	public BaseActivity() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public Student getStudentByFirstName() {
		Bundle bundle = getIntent().getExtras();

		String sName = bundle.getString( Student.EXTRA_STUDENTNAME );
		String sClass = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		StudentClass stdClass = DataHandler.GetInstance().getStudentClass( sClass );
		Student bean = stdClass.getStudentByFirstName( sName );

		return bean;
	}

	
	public static List<String> GetTasks( Context ctx ) {
		return DataHandler.GetInstance().getTaskNames();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		if (bundle == null ) bundle = new Bundle();
		return bundle;
	}
	

	
	/**
	 * 
	 * @return
	 */
	protected StudentClass getStudentClassExtra() {
		Bundle bundle = getIntent().getExtras();
		String className = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		return DataHandler.GetInstance().getStudentClass( className );
	}
	
	/**
	 * 
	 * @return
	 */
	public Student getStudentByIdentExtra() {
		Bundle bundle = getIntent().getExtras();

		String sName = bundle.getString( Student.EXTRA_IDENT );
		String sClass = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		StudentClass stdClass = DataHandler.GetInstance().getStudentClass( sClass );
		Student bean = stdClass.getStudentByIdent( sName );

		return bean;
	}
	
	
	/**
	 * 
	 * @param studentClass
	 * @param intent
	 */
	public static void PutStudentClassExtra( String stdClass, Intent intent ) {
		intent.putExtra( StudentClass.EXTRA_STUDENTCLASS, stdClass );

	}

	public void putStudentNameExtra( Student std, Intent intent ) {
		intent.putExtra( Student.EXTRA_STUDENTNAME, std.getFirstName() );

	}

	public void putStudentIdentExtra( Student std, Intent intent ) {
		intent.putExtra( Student.EXTRA_IDENT, std.getIdent() );

	}

	
	/**
	 * 
	 * @param std
	 * @param stdClass
	 * @param intent
	 */
	public void putIdentExtra( Student std, Intent intent ) {
		PutStudentClassExtra( std.getStudentClass(), intent );
		putStudentIdentExtra( std, intent );
	}
	
	/**
	 * 
	 * @return
	 */
	public static String GetDateAsString() {
		return GetDateAsString( new Date() );
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String GetDateAsString(Date date) {
		return sdf.format( date );
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String GetDateAsString(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set( year, month, day );
		
		return sdf.format( cal.getTime() );
	}
	
	public static Date GetDateFromString( String date ) {
		try {
			return sdf.parse( date );			
		}
		catch ( ParseException e ) {
			// TODO: handle exception
			return null;
		}
	}
	
}
