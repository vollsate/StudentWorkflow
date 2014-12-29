package no.glv.android.stdntworkflow;

import java.util.ArrayList;

import no.glv.android.stdntworkflow.core.LoadDataHandler;
import no.glv.android.stdntworkflow.core.Student;
import no.glv.android.stdntworkflow.core.StudentClass;
import no.glv.android.stdntworkflow.core.StudentClassHandler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LoadDataActivity extends ActionBarActivity implements OnClickListener {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_load_data );
		if ( savedInstanceState == null ) {
			getSupportFragmentManager().beginTransaction()
					.add( R.id.container, new PlaceholderFragment() ).commit();
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}
	
	
	@Override
	public void onClick( View v ) {
		StudentClass stdClass = LoadDataHandler.LoadStudentClassFromDownloadDir( this, "" );
		StudentClassHandler.GetInstance().addStudentClass( stdClass );
		
		Intent intent = new Intent( this, StudentListActivity.class );
		intent.putExtra( StudentClass.EXTRA_STUDENTCLASS, stdClass.getName() );
		startActivity( intent );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_load_data,
					container, false );
			
			Button btn = (Button) rootView.findViewById( R.id.BTN_loadFile );
			btn.setOnClickListener( (OnClickListener) getActivity() );
			
			return rootView;
		}
	}
}
