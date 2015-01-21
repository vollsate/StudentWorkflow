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

import no.glv.android.stdntworkflow.R;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * @author GleVoll
 *
 */
public class BaseActivity extends Activity {

    private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN, Locale.getDefault() );

    public BaseActivity() {
	super();
    }

    /**
     * 
     * @param mails
     * @return
     */
    public static Intent createMailIntent( String[] mails, Context ctx ) {
	Intent intent = new Intent( Intent.ACTION_SEND );
	intent.setType( "message/rfc822" );
	intent.putExtra( Intent.EXTRA_EMAIL, mails );
	intent.putExtra( Intent.EXTRA_SUBJECT, ctx.getResources().getString( R.string.stdlist_mail_subject ) );
	intent.putExtra( Intent.EXTRA_TEXT, ctx.getResources().getString( R.string.stdlist_mail_body ) );

	return intent;
    }

    public static Intent createCallIntent( Phone p ) {
	String tel = "tel:" + p.getNumber();
	
	Intent intent = new Intent( Intent.ACTION_CALL );
	intent.setData( Uri.parse( tel ) );
	
	return intent;
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
    public static StudentClass GetStudentClassExtra( Intent intent ) {
	Bundle bundle = intent.getExtras();
	String className = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

	return DataHandler.GetInstance().getStudentClass( className );
    }

    /**
     * 
     * @return
     */
    public static Student GetStudentByIdentExtra( Intent intent ) {
	Bundle bundle = intent.getExtras();

	String sName = bundle.getString( Student.EXTRA_IDENT );
	String sClass = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

	StudentClass stdClass = DataHandler.GetInstance().getStudentClass( sClass );
	Student bean = stdClass.getStudentByIdent( sName );

	return bean;
    }

    public Student getStudentByIdentExtra() {
	return GetStudentByIdentExtra( getIntent() );
    }

    /**
     * 
     * @param studentClass
     * @param intent
     */
    public static void PutStudentClassExtra( String stdClass, Intent intent ) {
	intent.putExtra( StudentClass.EXTRA_STUDENTCLASS, stdClass );

    }

    public static void putStudentNameExtra( Student std, Intent intent ) {
	intent.putExtra( Student.EXTRA_STUDENTNAME, std.getFirstName() );

    }

    public static void putStudentIdentExtra( Student std, Intent intent ) {
	putStudentIdentExtra( std.getIdent(), intent );

    }

    public static void putStudentIdentExtra( String std, Intent intent ) {
	intent.putExtra( Student.EXTRA_IDENT, std );
    }

    public static void PutTaskNameExtra( String taskName, Intent intent ) {
	if ( intent == null ) return;

	intent.putExtra( Task.EXTRA_TASKNAME, taskName );
    }

    public static String GetTaskNameExtra( Intent intent ) {
	if ( intent == null ) return null;

	return intent.getStringExtra( Task.EXTRA_TASKNAME );
    }

    /**
     * 
     * @param std
     * @param stdClass
     * @param intent
     */
    public static void putIdentExtra( Student std, Intent intent ) {
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
    public static String GetDateAsString( Date date ) {
	return sdf.format( date );
    }

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String GetDateAsString( int year, int month, int day ) {
	Calendar cal = Calendar.getInstance();
	cal.set( year, month, day );

	return sdf.format( cal.getTime() );
    }

    /**
     * 
     * 
     * @param date
     * @return The Date instance or null if some error occurs.
     */
    public static Date GetDateFromString( String date ) {
	try {
	    return sdf.parse( date );
	}
	catch ( ParseException e ) {
	    // TODO: handle exception
	    return null;
	}
    }

    public static CheckBox GetCheckBox( View rootView, int id ) {
	return (CheckBox) rootView.findViewById( id );
    }

    public static ImageView GetImageView( View rootView, int id ) {
	return (ImageView) rootView.findViewById( id );
    }

}
