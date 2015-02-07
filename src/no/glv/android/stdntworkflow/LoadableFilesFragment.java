package no.glv.android.stdntworkflow;

import java.io.IOException;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DialogFragmentBase;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LoadableFilesFragment extends DialogFragmentBase {

	public static final String EXTRA_TASKNAME = BaseValues.EXTRA_BASEPARAM + "TaskName";
	OnDataLoadedListener listener;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
	}

	@Override
	protected int getRootViewID() {
		return R.layout.fragment_loaddata_files;
	}

	@Override
	protected String getTitle() {
		return "Load a file ..";
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
				LoadableFilesFragment.this.finish();
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
		return DataHandler.GetInstance().getFilesFromDownloadDir();
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static class LoadableFilesAdapter extends ArrayAdapter<String> implements OnClickListener {

		private List<String> files;
		LoadableFilesFragment fragment;

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
			try {
				StudentClass stdClass = DataHandler.LoadStudentClassFromDownloadDir( getContext(), fileName );
				DataHandler.GetInstance().addStudentClass( stdClass );
				if ( fragment.listener != null )
					fragment.listener.onDataLoaded( stdClass );
			}
			catch ( IOException e ) {
				Log.d( "", e.toString() );
			}

			fragment.finish();
		}
	}
	
	public static void StartFragment( OnDataLoadedListener listener, FragmentManager manager ) {
		LoadableFilesFragment fragment = new LoadableFilesFragment();
		
		fragment.listener = listener;
		FragmentTransaction ft = manager.beginTransaction();
		fragment.show( ft, fragment.getClass().getSimpleName() );
		
	}

	static interface OnDataLoadedListener {
		public void onDataLoaded( StudentClass stdClass );
	}
}
