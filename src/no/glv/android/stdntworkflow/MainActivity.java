package no.glv.android.stdntworkflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		Button btn = ( Button ) findViewById( R.id.BTN_newclass );
		btn.setOnClickListener( this );
		btn = ( Button ) findViewById( R.id.BTN_newtask );
		btn.setOnClickListener( this );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
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
	

	@Override
	public void onClick( View v ) {
		int id = v.getId();
		Intent intent = null;
		
		switch ( id ) {
		case R.id.BTN_newtask:
			//intent = new Intent(this, StudentListActivity.class);
			break;
						
		case R.id.BTN_newclass:
			intent = new Intent( this, LoadDataActivity.class );
			break;
		}
		
		startActivity( intent );
	}
}
