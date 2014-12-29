package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.LoadDataHandler;
import no.glv.android.stdntworkflow.core.StudentClass;
import no.glv.android.stdntworkflow.core.StudentClassHandler;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
public class StudentListActivity extends ActionBarActivity implements OnClickListener {

	private static final String TAG = StudentListActivity.class.getSimpleName();

	/**  */
	private StudentClass stdClass;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_list );

		ListView listView = (ListView) findViewById( R.id.student_listview );
		Bundle bundle = getIntent().getExtras();
		String className = bundle.getString( StudentClass.EXTRA_STUDENTCLASS );
		stdClass = StudentClassHandler.GetInstance().getStudentClass( className );

		StudentListAdapter adapter = new StudentListAdapter( this, stdClass.toList() );
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
