package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.LoadDataHandler;
import no.glv.android.stdntworkflow.core.StudentClass;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


/**
 * 
 * @author GleVoll
 *
 */
public class StudentListActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = StudentListActivity.class.getSimpleName();
	
	private static final String CLASS_REPLACE = "{klasse}";

	/**  */
	private StudentClass stdClass;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_list );

		ListView listView = (ListView) findViewById( R.id.student_listview );
		stdClass = getStudentClass();
		
		String title = getResources().getString( R.string.activity_studentList_title );
		title = title.replace( CLASS_REPLACE, stdClass.getName() );
		
		setTitle( title );

		StudentListAdapter adapter = new StudentListAdapter( this, stdClass.toList() );
		adapter.setBaseActivity( this );
		listView.setAdapter( adapter );
	}

	/**
	 * 
	 */
	public void onClick( View v ) {
		Log.d( TAG, "onClick: " + v );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.menu_student_list, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();

		switch ( id ) {
		case R.id.action_settings:
			return true;

		case R.id.action_writeToLocal:
			LoadDataHandler.WriteStudentClass( stdClass, this );
			return true;

		default:
			return super.onOptionsItemSelected( item );
		}
	}
}
