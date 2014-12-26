package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.base.StudentBean;
import no.glv.android.stdntworkflow.test.StudentBeanData;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class StudentListActivity extends ActionBarActivity implements
		OnClickListener {

	private static final String TAG = StudentListActivity.class.getSimpleName();

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_list );

		ListView listView = (ListView) findViewById( R.id.student_listview );
		StudentBean[] testData = StudentBeanData.CreateTestData();
		StudentListAdapter adapter = new StudentListAdapter( this, testData );
		listView.setAdapter( adapter );
//		listView.setOnClickListener( this );
	}

	
	/**
	 * 
	 */
	public void onClick( View v ) {
		Log.d( TAG, "onClick: " + v );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.student_list, menu );
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
}
