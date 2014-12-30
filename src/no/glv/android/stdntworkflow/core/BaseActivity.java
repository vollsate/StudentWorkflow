/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * @author GleVoll
 *
 */
public class BaseActivity extends ActionBarActivity {

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

		StudentClass stdClass = StudentClassHandler.GetInstance().getStudentClass( sClass );
		Student bean = stdClass.getStudentByFirstName( sName );

		return bean;
	}

	protected StudentClass getStudentClass() {
		Bundle bundle = getIntent().getExtras();
		String className = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		return StudentClassHandler.GetInstance().getStudentClass( className );
	}

	/**
	 * 
	 * @return
	 */
	public Student getStudentByIdent() {
		Bundle bundle = getIntent().getExtras();

		String sName = bundle.getString( Student.EXTRA_IDENT );
		String sClass = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );

		StudentClass stdClass = StudentClassHandler.GetInstance().getStudentClass( sClass );
		Student bean = stdClass.getStudentByIdent( sName );

		return bean;
	}

	
	/**
	 * 
	 * @param studentClass
	 * @param intent
	 */
	public void putStudentClassExtra( String stdClass, Intent intent ) {
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
		putStudentClassExtra( std.getStudentClass(), intent );
		putStudentIdentExtra( std, intent );
	}
	
}
