package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.InstalledClassesFragment.ClassViewConfig;
import no.glv.android.stdntworkflow.LoadableFilesFragment.OnDataLoadedListener;
import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author glevoll
 *
 */
public class NewStudentClassFragment extends Fragment implements OnClickListener, OnDataLoadedListener {

	private StudentClass stdClass;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
	}

	/**
	 * 
	 */
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_loaddata, container, false );
		
		// Start the Installed classes fragment
		ClassViewConfig config = new ClassViewConfig();
		config.showStudentCount = true;		
		InstalledClassesFragment.StartFragment( getFragmentManager(), config );

		Button btn = (Button) rootView.findViewById( R.id.BTN_loadFile );
		btn.setOnClickListener( this );

		btn = (Button) rootView.findViewById( R.id.BTN_loadExcel );
		btn.setOnClickListener( this );

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.menu_load_data, menu );
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		//int id = item.getItemId();
		return super.onOptionsItemSelected( item );
	}

	@Override
	public void onClick( View v ) {
		if ( v.getId() == R.id.BTN_loadFile ) {
			LoadableFilesFragment.StartFragment( this, getFragmentManager() );
			return;
		}

		// Load data from Excel workbook
		else if ( v.getId() == R.id.BTN_loadExcel ) {
			try {
				LoadableFilesFragment fragment = new LoadableExcelClassesFragment();
				fragment.listener = this;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				fragment.show( ft, getClass().getSimpleName() );
			}
			catch ( Exception e ) {
				Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_LONG ).show();
			}
		}
	}

	@Override
	public void onDataLoaded( StudentClass stdClass ) {
		String msg = getResources().getString( R.string.loadData_added_toast );
		msg = msg.replace( "{class}", stdClass.getName() );

		Toast.makeText( getActivity(), msg, Toast.LENGTH_LONG ).show();
	}

	/**
     * 
     */
	public void startStudentListActivity() {
		Intent intent = new Intent( getActivity(), StdClassListActivity.class );
		BaseActivity.PutStudentClassExtra( stdClass.getName(), intent );
		startActivity( intent );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends android.app.Fragment {

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_loaddata, container, false );

			Button btn = (Button) rootView.findViewById( R.id.BTN_loadFile );
			btn.setOnClickListener( (OnClickListener) getActivity() );

			btn = (Button) rootView.findViewById( R.id.BTN_loadExcel );
			btn.setOnClickListener( (OnClickListener) getActivity() );

			return rootView;
		}
	}
}
