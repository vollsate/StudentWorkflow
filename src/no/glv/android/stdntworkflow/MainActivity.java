package no.glv.android.stdntworkflow;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent = null;
		
		switch ( id ) {
		case R.id.menu_students:
			intent = new Intent(this, StudentListActivity.class);
			break;
						
		case R.id.menu_installData:
			intent = new Intent( this, LoadDataActivity.class );
			break;
			
		default:
			return super.onOptionsItemSelected( item );
		}
		
		startActivity( intent );
		return true;
	}
}
