/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import java.util.Date;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * @author GleVoll
 *
 */
public class BaseActivity extends Activity {

	private DataHandler dataHandler;

	public BaseActivity() {
		super();
	}
	
	public BaseActivity getBaseActivity() {
		return (BaseActivity) this;
	}
	
	public DataHandler getDataHandler() {
		if ( dataHandler == null ) {
			dataHandler = DataHandler.GetInstance();
		}
		
		return dataHandler;
	}

	/**
	 * 
	 * @return
	 */
	public static StudentClass GetStudentClassExtra( Intent intent ) {
		Bundle bundle = intent.getExtras();
		String className = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		return DataHandler.GetInstance().getStudentClass( className );
	}

	/**
	 * Gets a Student instance from the intent as EXTRA parameters. Use the
	 * {@link BaseActivity} <tt>put</tt> methods to put the EXTRA parametres in
	 * the intent.
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

	/**
	 * 
	 * @param std
	 * @param intent
	 */
	public static void PutStudentIdentExtra( Student std, Intent intent ) {
		PutStudentIdentExtra( std.getIdent(), intent );

	}

	/**
	 * 
	 * @param std
	 * @param intent
	 */
	public static void PutStudentIdentExtra( String std, Intent intent ) {
		intent.putExtra( Student.EXTRA_IDENT, std );
	}

	/**
	 * 
	 * @param taskName
	 * @param intent
	 */
	public static void PutTaskNameExtra( Integer taskName, Intent intent ) {
		if ( intent == null )
			return;

		intent.putExtra( Task.EXTRA_TASKNAME, taskName.intValue() );
	}

	/**
	 * 
	 * @param intent
	 * @return
	 */
	public static Integer GetTaskNameExtra( Intent intent ) {
		if ( intent == null )
			return null;

		return intent.getIntExtra( Task.EXTRA_TASKNAME, 0 );
	}

	/**
	 * 
	 * @param std
	 * @param stdClass
	 * @param intent
	 */
	public static void PutIdentExtra( Student std, Intent intent ) {
		PutStudentClassExtra( std.getStudentClass(), intent );
		PutStudentIdentExtra( std, intent );
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
	public static String GetDateAsString( Date date ) {
		return Utils.GetDateAsString( date );
	}
	/**
	 * 
	 * 
	 * @param date
	 * @return The Date instance or null if some error occurs.
	 */
	public Date getDateFromString( String date ) {
		return Utils.GetDateFromString( date );
	}

	/**
	 * 
	 * @param rootView
	 * @param id
	 * @return
	 */
	public static CheckBox GetCheckBox( View rootView, int id ) {
		return (CheckBox) rootView.findViewById( id );
	}

	/**
	 * 
	 * @param rootView
	 * @param id
	 * @return
	 */
	public static ImageView GetImageView( View rootView, int id ) {
		return (ImageView) rootView.findViewById( id );
	}

}
