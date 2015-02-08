package no.glv.android.stdntworkflow.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import no.glv.android.stdntworkflow.R;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Phone;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Utils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN, Locale.getDefault() );

	/**
	 * 
	 * @param resource
	 * @param spinner
	 * @param data
	 * @param selected
	 */
	public static void SetupSpinner( Spinner spinner, ArrayList<String> data, String selected, Context ctx ) {
		// Set the proper SubjectTypes to the spinners
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( ctx, android.R.layout.simple_spinner_dropdown_item,
				data );
		spinner.setAdapter( adapter );
		spinner.setSelection( data.indexOf( selected ) );
	}

	/**
	 * 
	 * @param mails
	 * @return
	 */
	public static Intent CreateMailIntent( String[] mails, Context ctx ) {
		Intent intent = new Intent( Intent.ACTION_SEND );
		intent.setType( "message/rfc822" );
		intent.putExtra( Intent.EXTRA_EMAIL, mails );
		intent.putExtra( Intent.EXTRA_SUBJECT, ctx.getResources().getString( R.string.stdlist_mail_subject ) );
		intent.putExtra( Intent.EXTRA_TEXT, ctx.getResources().getString( R.string.stdlist_mail_body ) );

		return intent;
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public static Intent CreateCallIntent( Phone p ) {
		String tel = "tel:" + p.getNumber();

		Intent intent = new Intent( Intent.ACTION_CALL );
		intent.setData( Uri.parse( tel ) );

		return intent;
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


}
