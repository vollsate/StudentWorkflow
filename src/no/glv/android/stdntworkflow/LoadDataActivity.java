package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LoadDataActivity extends BaseActivity implements OnClickListener {

	private StudentClass stdClass;
	
	private LoadableFilesFragment fragment;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_load_data );
		if ( savedInstanceState == null ) {
			getSupportFragmentManager().beginTransaction().add( R.id.container, new PlaceholderFragment() ).commit();
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
		case R.id.action_writeToLocal:
			break;

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
		fragment = new LoadableFilesFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		fragment.show( ft, getClass().getSimpleName() );
	}
	
	/**
	 * 
	 */
	public void startStudentListActivity() {
		Intent intent = new Intent( this, StudentClassListActivity.class );
		putStudentClassExtra( stdClass.getName(), intent );
		startActivity( intent );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_loaddata, container, false );

			Button btn = (Button) rootView.findViewById( R.id.BTN_loadFile );
			btn.setOnClickListener( (OnClickListener) getActivity() );

			return rootView;
		}
	}
}
