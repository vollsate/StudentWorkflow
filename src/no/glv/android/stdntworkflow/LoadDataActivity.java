package no.glv.android.stdntworkflow;

import java.net.URL;
import java.util.List;

import no.glv.android.stdntworkflow.LoadableFilesFragment.OnDataLoadedListener;
import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.ExcelReader;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class LoadDataActivity extends Activity implements OnClickListener, OnDataLoadedListener {

	private StudentClass stdClass;

	private LoadableFilesFragment fragment;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_load_data );
		if ( savedInstanceState == null ) {
			getFragmentManager().beginTransaction().add( R.id.container, new PlaceholderFragment() ).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_load_data, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();

		switch ( id ) {
			default:
				break;
		}

		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	@Override
	public void onClick( View v ) {
		if ( v.getId() == R.id.BTN_loadFile ) {
			fragment = new LoadableFilesFragment();
			fragment.listener = this;
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			fragment.show( ft, getClass().getSimpleName() );

			return;
		}

		if ( v.getId() == R.id.BTN_loadXML ) {
			try {
				URL url = new URL( DataHandler.GetInstance().getSettingsManager().getXMLDataURL() );

				LoadXMLData xmlData = new LoadXMLData();
				xmlData.execute( url );
			}
			catch ( Exception e ) {

			}
		}

		// Load data from Excel workbook
		if ( v.getId() == R.id.BTN_loadExcel ) {
			try {
				fragment = new LoadableExcelClassesFragment();
				fragment.listener = this;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				fragment.show( ft, getClass().getSimpleName() );
			}
			catch ( Exception e ) {
				Toast.makeText( getApplication(), e.toString(), Toast.LENGTH_LONG ).show();
			}
		}
	}

	@Override
	public void onDataLoaded( StudentClass stdClass ) {
		String msg = getResources().getString( R.string.loadData_added_toast );
		msg = msg.replace( "{class}", stdClass.getName() );

		Toast.makeText( getApplicationContext(), msg, Toast.LENGTH_LONG ).show();
	}

	/**
     * 
     */
	public void startStudentListActivity() {
		Intent intent = new Intent( this, StdClassListActivity.class );
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

			btn = (Button) rootView.findViewById( R.id.BTN_loadXML );
			btn.setOnClickListener( (OnClickListener) getActivity() );

			btn = (Button) rootView.findViewById( R.id.BTN_loadExcel );
			btn.setOnClickListener( (OnClickListener) getActivity() );

			return rootView;
		}
	}
}
