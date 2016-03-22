package no.glv.android.stdntworkflow.core;

import android.os.AsyncTask;

import no.glv.android.stdntworkflow.intrfc.StudentClass;

public class LoadAndStoreASyncTask extends AsyncTask<String, Void, StudentClass> {

	private ExcelReader reader;
	private OnStudentClassStoredListener listener;

	public LoadAndStoreASyncTask( ExcelReader reader, OnStudentClassStoredListener l ) {
		this.reader = reader;
		this.listener = l;
	}

	@Override
	protected StudentClass doInBackground( String... params ) {
		String fileName = params[0];
		StudentClass stdClass = reader.loadClass( fileName );
		DataHandler.GetInstance().addStudentClass( stdClass );

		return stdClass;
	}

	@Override
	protected void onPostExecute( StudentClass result ) {
		listener.onStudentClassStore( result );
	}

	/**
	 * 
	 * @author glevoll
	 *
	 */
	public static interface OnStudentClassStoredListener {

		public void onStudentClassStore( StudentClass stdClass );
	}

}
