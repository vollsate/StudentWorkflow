package no.glv.android.stdntworkflow;

import java.io.IOException;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.ExcelReader;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoadableExcelClassesFragment extends LoadableFilesFragment {

	public static final String PARAM_FILENAME = BaseValues.EXTRA_BASEPARAM + "fileName";

	OnDataLoadedListener listener;
	ExcelReader reader;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		Bundle args = getArguments();
		String fileName;
		
		if ( args != null ) {
			fileName = args.getString( PARAM_FILENAME );
		}
		else {
			fileName = null;
		}

		try {
			reader = new ExcelReader( getActivity(), fileName );
		}
		catch ( IOException e ) {
			// TODO: handle exception
		}

		// List<StudentClass> stdClasses = reader.loadClasses( );
	}

	@Override
	protected String getTitle() {
		return "Load a class ..";
	}

	@Override
	protected void buildView( View rootView ) {
		buildAdapter( rootView );
		buildButton( rootView );
	}

	/**
	 * 
	 * @param rootView
	 */
	private void buildButton( View rootView ) {
		Button btn = (Button) rootView.findViewById( R.id.BTN_loadData_cancel );
		btn.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				LoadableExcelClassesFragment.this.finish();
			}
		} );
	}

	/**
	 * 
	 * @param rootView
	 */
	private void buildAdapter( View rootView ) {
		List<String> list = createFileList();

		ListView listView = (ListView) rootView.findViewById( R.id.LV_loadData_filesList );
		LoadableFilesAdapter adapter = new LoadableFilesAdapter( getActivity(), R.id.LV_loadData_filesList, list );
		adapter.fragment = this;
		listView.setAdapter( adapter );
	}

	/**
	 * 
	 * @return
	 */
	private List<String> createFileList() {
		List<String> list = reader.getAvailableClasses();
		
		for ( String name : list ) {
			if ( DataHandler.GetInstance().getStudentClassNames().contains( name ) )
				list.remove( name );
		}
		
		return reader.getAvailableClasses();
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static class LoadableFilesAdapter extends ArrayAdapter<String> implements OnClickListener {

		private List<String> files;
		LoadableExcelClassesFragment fragment;

		public LoadableFilesAdapter( Context context, int resource, List<String> objects ) {
			super( context, resource, objects );

			this.files = objects;
		}

		/**
		 * 
		 */
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			if ( convertView == null ) {
				convertView = inflater.inflate( R.layout.row_loaddata_files, parent, false );
			}

			String file = files.get( position );

			TextView textView = (TextView) convertView.findViewById( R.id.TV_loadData_fileName );
			textView.setTag( file );
			textView.setText( file );
			textView.setOnClickListener( this );

			return convertView;
		}

		@Override
		public void onClick( View v ) {
			String fileName = v.getTag().toString();
			StudentClass stdClass = fragment.reader.loadClass( fileName );
			DataHandler.GetInstance().addStudentClass( stdClass );
			if ( fragment.listener != null )
				fragment.listener.onDataLoaded( stdClass );
			
			String msg = getContext().getResources().getString( R.string.loadData_added_toast );
			msg = msg.replace( "{class}", fileName );
			Toast.makeText( getContext(), msg, Toast.LENGTH_LONG ).show();
			
			fragment.finish();
		}
	}

	static interface OnDataLoadedListener {
		public void onDataLoaded( StudentClass stdClass );
	}
}
