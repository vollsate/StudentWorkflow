package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.CSVReader;
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
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadableFilesFragment extends DialogFragmentBase {

	public static final String EXTRA_TASKNAME = BaseValues.EXTRA_BASEPARAM + "TaskName";
	OnDataLoadedListener listener;
	ProgressBar mProgressBar;
	Button mButton;
	ListView mListView;

	/**
	 * 
	 * @return
	 */
	private ProgressBar getProgressBar() {
		if ( mProgressBar == null ) {
			mProgressBar = (ProgressBar) getRootView().findViewById( R.id.PB_newclass_indeterminate );
		}

		return mProgressBar;
	}

	/**
	 * 
	 * @return
	 */
	protected Button getButton() {
		if ( mButton == null )
			mButton = (Button) getRootView().findViewById( R.id.BTN_loadData_cancel );

		return mButton;
	}

	/**
	 * 
	 * @return
	 */
	protected ListView getListView() {
		if ( mListView == null )
			mListView = (ListView) getRootView().findViewById( R.id.LV_loadData_filesList );

		return mListView;
	}

	/**
	 * 
	 */
	protected void hideProgressBar() {
		getProgressBar().setVisibility( View.INVISIBLE );

		getButton().setVisibility( View.VISIBLE );
		getListView().setVisibility( View.VISIBLE );
	}

	/**
	 * 
	 */
	protected void showProgressBar() {
		getProgressBar().setVisibility( View.VISIBLE );

		getButton().setVisibility( View.GONE );
		getListView().setVisibility( View.GONE );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
	}

	@Override
	protected int getRootViewID() {
		return R.layout.fragment_loaddata_files;
	}

	@Override
	protected int getTitle() {
		return R.string.loadData_csv_title;
	}

	@Override
	protected void buildView( View rootView ) {
		hideProgressBar();
		buildAdapter( rootView );
		buildButton( rootView );
	}

	/**
	 * 
	 * @param rootView
	 */
	private void buildButton( View rootView ) {
		Button btn = getButton();
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

		ListView listView = getListView();
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
	public static class LoadableFilesAdapter extends ArrayAdapter<String> implements OnClickListener,
			no.glv.android.stdntworkflow.core.CSVReader.OnDataLoadedListener {

		private List<String> files;
		LoadableFilesFragment fragment;

		public LoadableFilesAdapter( Context context, int resource, List<String> objects ) {
			super( context, resource, objects );

			this.files = objects;
		}

		@Override
		public void onError( Exception e ) {
			// TODO Auto-generated method stub

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
			fragment.showProgressBar();
			String fileName = v.getTag().toString();
			
			String msg = fragment.getResources().getString( R.string.loadData_installing_msg ).replace( "{class}", fileName );			
			fragment.getDialog().setTitle( msg );
			try {
				CSVReader reader = new CSVReader( this );
				reader.execute( new String[] { fileName } );
			}
			catch ( Exception e ) {
				Log.d( "", e.toString() );
			}
		}

		@Override
		public void onDataLoaded( StudentClass stdClass ) {
			fragment.hideProgressBar();
			if ( stdClass == null )
				return;

			DataHandler.GetInstance().addStudentClass( stdClass );
			DataHandler.GetInstance().notifyStudentClassAdd( stdClass );
			if ( fragment.listener != null )
				fragment.listener.onDataLoaded( stdClass );

			fragment.finish();
		}

	}

	/**
	 * 
	 * @param listener
	 * @param manager
	 */
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
