package no.glv.android.stdntworkflow.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class LoadDataHandler {
	
	private static final String TAG = LoadDataHandler.class.getSimpleName();
	
	private static String CLASSNAME = "7B - 2013";
	private static String FILENAME = CLASSNAME + ".csv";

	private LoadDataHandler() {
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static StudentClass LoadStudentClassFromDownloadDir( Activity activity, String fileName ) {
		FileInputStream fis;
		File externalDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS );
		String fName = externalDir.getAbsolutePath() + "/" + FILENAME;
		String stdClassName = CLASSNAME;
		StudentClass stdClass = new StudentClassImpl( stdClassName );
		
		try {
			fis = new FileInputStream( new File(fName) ); 
		}
		catch ( FileNotFoundException fnfEx ) {
			Log.e( TAG, "LoadStudentClass(): File not found: " + fName, fnfEx );
			return null;
		}
		catch ( Exception e ) {
			Log.e( TAG, "LoadStudentClass(): Unknown error: " + fName, e );
			return null;
		}
		
		ArrayList<Student> list = new ArrayList<Student>();
		boolean readOnce = false;

		try {
			BufferedReader buff = new BufferedReader( new InputStreamReader( fis ) );
			if ( !readOnce ) {
				buff.readLine();
				readOnce = true;
			}
			
			String stdLine;
			while ( (stdLine = buff.readLine()) != null )
				list.add( CreateStudentFromString( stdLine, stdClassName ) );
		}
		catch ( IOException ioe ) {
			Log.e( TAG, "LoadStudentClass(): Error loading file: " + fName, ioe );
		}
		finally {
			try {
				fis.close();
			}
			catch ( IOException e ) {
				Log.e( TAG, "LoadStudentClass(): Error closing file: " + fName, e );
			}
		}
		
		Log.v( TAG, list.toString() );
		stdClass.addAll( list );
		return stdClass;
		
	}
	
	
	/**
	 * 
	 * @param stdString
	 * @return
	 */
	private static Student CreateStudentFromString( String stdString, String className ) {
		StudentBean bean = new StudentBean( className );
		
		String[] params = stdString.split( ";" );
		for ( int i = 0; i < params.length; i++ ) {
			String param = params[i];
			
			switch ( i ) {
			case 0:
				bean.grade = param;
				break;

			case 1:
				try {
					bean.birth = new SimpleDateFormat( "dd.MM.yyyy", Locale.getDefault() ).parse( param );

				}
				catch ( Exception e ) {
					Log.e( TAG, "Cannot convert String to date: " + param, e );
				}
				break;

			case 2:
				bean.setFullName( param );
				break;

			case 3:
				bean.adress = param;
				break;

			case 4:
				bean.postalCode = param;
				break;

			case 5:
				bean.parent1Name = param;
				break;

			case 6:
				bean.parent1Phone = param;
				break;

			case 7:
				bean.parent1Mail = param;
				break;

			case 8:
				bean.parent2Name = param;
				break;

			case 9:
				bean.parent2Phone = param;
				break;

			case 10:
				bean.parent2Mail = param;
				break;

			default:
				break;
			}
		}
		
		
		return bean;
	}
	
	
	
	/**
	 * 
	 * @param students
	 * @param activity
	 * @param fileName
	 * @return
	 */
	public static boolean WriteStudentClass(Student[] students, Activity activity, String fileName ) {
		FileOutputStream fos;
		try {
			fos = activity.openFileOutput( fileName, Context.MODE_PRIVATE );
		}
		catch ( FileNotFoundException e ) {
			Log.e( TAG, "WriteStudentClass: File noe found: " + fileName, e );
			return false;
		}
		
		for ( int i = 0; i < students.length; i++ ) {
			Student std = students[i];
			String stdString = StudentToDataString( std );
			try {
				fos.write( stdString.getBytes() );				
			}
			catch ( IOException e ) {
				Log.e( TAG, "WriteStudentClass: Cannot write to file " + fileName, e );
			}
			finally {
				try {
					fos.close();	
				}
				catch ( IOException e2 ) {
					Log.e( TAG, "Error closing FileOutputStream", e2 );
				}
			}
		}
		
		
		return false;
	}
	
	
	/**
	 * 
	 * @param std
	 * @return
	 */
	private static String StudentToDataString(Student std) {
		StringBuffer sb = new StringBuffer();
		
		sb.append( "Klasse=" ).append( std.getGrade() ).append( ";" );
		sb.append( "Født=" ).append( ((StudentBean) std).birhtToString() ).append( ";" );
		sb.append( "Fullt navn=" ).append( std.getLastname() ).append( ", " ).append( std.getFirstName() ).append( ";" );
		sb.append( "Adresse=" ).append( std.getAdress() ).append( ";" );
		sb.append( "Postnr=" ).append( std.getPostalCode() ).append( ";" );
		
		sb.append( "Foresatt 1 navn=" ).append( std.getParent1Name() ).append( ";" );
		sb.append( "Foresatt 1 mobil=" ).append( std.getParent1Phone() ).append( ";" );
		sb.append( "Foresatt 1 e-post=" ).append( std.getParent1Mail() ).append( ";" );

		sb.append( "Foresatt 2 navn=" ).append( std.getParent1Name() ).append( ";" );
		sb.append( "Foresatt 2 mobil=" ).append( std.getParent1Phone() ).append( ";" );
		sb.append( "Foresatt 2 e-post=" ).append( std.getParent1Mail() ).append( ";" );

		return sb.toString();
	}
	
	
	/** 
	 * Checks if external storage is available for read and write 
	 */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/** 
	 * Checks if external storage is available to at least read 
	 */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	
	public static void main( String[] args ) {
		String mDate = "23.03.2001";
		
		try {
			Date date = new SimpleDateFormat( "dd.MM.yyyy", Locale.getDefault() ).parse( mDate );
			System.out.println( date.toString() );
		}
		catch ( Exception e ) {
			System.out.println( e.toString() );
		}
	}

}
