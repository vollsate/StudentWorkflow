package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentChangedListener;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.sql.Database;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


/**
 * This activity will show all the students in a certain class. 
 * 
 * The activity should be able to:
 * 		- Update the name of the class TODO
 * 		- Delete the class
 * 
 * @author GleVoll
 *
 */
public class StudentClassListActivity extends BaseActivity implements OnClickListener, OnStudentChangedListener {

	private static final String TAG = StudentClassListActivity.class.getSimpleName();
	
	private static final String CLASS_REPLACE = "{klasse}";

	/**  */
	private StudentClass stdClass;
	
	private boolean needUpdate;
	
	/**
	 * 
	 */
	public StudentClassListActivity() {
		DataHandler.GetInstance().addOnStudentChangeListener( this );
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_studentclass_list );
		
		needUpdate = true;
		createView();
	}
	
	/**
	 * 
	 */
	private void createView() {
		if (! needUpdate ) return;
		
		ListView listView = (ListView) findViewById( R.id.student_listview );
		stdClass = getStudentClassExtra();
		
		String title = getResources().getString( R.string.activity_studentList_title );
		title = title.replace( CLASS_REPLACE, stdClass.getName() );
		
		setTitle( title );

		StudentListAdapter adapter = new StudentListAdapter( this, stdClass.toList() );
		adapter.notifyDataSetChanged();
		listView.setAdapter( adapter );
		
		needUpdate = false;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		createView();
	}
	
	@Override
	public void onStudenChange( Student std, int mode ) {
		needUpdate = true;
		createView();
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
			Database.GetInstance( getApplicationContext() ).insertStudentClass( stdClass );
			return true;

		default:
			return super.onOptionsItemSelected( item );
		}
	}
}
