package no.glv.android.stdntworkflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.core.LoadDataHandler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		Button btn = (Button) findViewById( R.id.BTN_newclass );
		btn.setOnClickListener( this );
		btn = (Button) findViewById( R.id.BTN_newtask );
		btn.setOnClickListener( this );

		LoadDataHandler.LoadLocalStudentClasses( this );
		createListView();
	}

	/**
	 * 
	 */
	private void createListView() {
		Log.d( TAG, "Creating ListView" );
		List<String> filesList = LoadDataHandler.GetLocalStudentClasses( this );

		if ( filesList.isEmpty() ) return;

		ListView listView = (ListView) findViewById( R.id.LV_classes );
		List<String> mClasses = new ArrayList<String>();
		Iterator<String> it = filesList.iterator();

		while ( it.hasNext() ) {
			String fileName = it.next();
			fileName = fileName.substring( 0, fileName.length() - 4 );
			mClasses.add( fileName );
		}

		StudentClassAdapter adapter = new StudentClassAdapter( this, R.layout.classes_list_row, mClasses );
		listView.setAdapter( adapter );
	}

	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.menu_main, menu );
		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();
		// Intent intent = null;

		switch ( id ) {
		case R.id.menu_settings:
			// intent = new Intent(this, SettingsActivity.class);
			break;

		default:
			return super.onOptionsItemSelected( item );
		}

		// if (intent != null ) startActivity( intent );
		return true;
	}

	@Override
	public void onClick( View v ) {
		int id = v.getId();
		Intent intent = null;

		switch ( id ) {
		case R.id.BTN_newtask:
			// intent = new Intent(this, StudentListActivity.class);
			break;

		case R.id.BTN_newclass:
			intent = new Intent( this, LoadDataActivity.class );
			break;
		}

		startActivity( intent );
	}
}
