package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.core.DataHandler.OnTaskChangedListener;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import no.glv.android.stdntworkflow.sql.Database;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * Main task that shows the first page.
 * 
 * - The user may look at a class or a task - A new task may be loaded - A new
 * class may be installed
 * 
 * @author GleVoll
 *
 */
public class MainActivity extends BaseActivity implements OnStudentClassChangeListener,
		OnTaskChangedListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	List<String> mClasses;
	List<String> mTasks;

	private boolean needUpdate = false;
	private boolean initiated;

	DataHandler dataHandler;
	InstalledClassesFragment classesFragment;

	/**
	 * Will initiate the Datahandler for the rest of the applicaion.
	 * 
	 * Listeners for StudentClass change and Task change are added
	 */
	private void init() {
		if ( initiated ) return;

		dataHandler = DataHandler.Init( getApplicationContext() );
		dataHandler.addOnStudentClassChangeListener( this );
		dataHandler.addOnTaskChangeListener( this );

		initiated = true;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		setTitle( "Student manager" );

		init();

		updateLists();
/*
		classesFragment = new InstalledClassesFragment( );
		getFragmentManager().beginTransaction().add( R.id.activity_main, classesFragment ).commit();
*/
		createListView();
}

	@Override
	protected void onRestart() {
		super.onRestart();

		if ( !needUpdate ) return;

		updateLists();
		createListView();
		needUpdate = false;
	}

	/**
	 * 
	 */
	private void createListView() {
		Log.d( TAG, "Creating ListView" );

		ListView listView;
		if ( !mClasses.isEmpty() ) {
			listView = (ListView) findViewById( R.id.LV_classes );
			InstalledStudentClassListAdapter classAdapter = new InstalledStudentClassListAdapter( this,
					R.layout.row_classes_list, mClasses );
			classAdapter.setBaseActivity( this );
			listView.setAdapter( classAdapter );
		}

		if ( !mTasks.isEmpty() ) {
			listView = (ListView) findViewById( R.id.LV_tasks );
			InstalledTaskListAdapter taskAdapter = new InstalledTaskListAdapter( this, R.layout.row_tasks_list, mTasks );
			taskAdapter.setBaseActivity( this );
			listView.setAdapter( taskAdapter );
		}
	}

	/**
	 * 
	 */
	private void updateLists() {
		mClasses = dataHandler.getStudentClassNames();
		mTasks = dataHandler.getTaskNames();
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
		Intent intent = null;

		switch ( id ) {
		case R.id.menu_settings:
			// intent = new Intent(this, SettingsActivity.class);
			break;

		case R.id.menu_resetDB:
			Database.GetInstance( getApplicationContext() ).runCreate();
			break;

		case R.id.menu_newTask:
			intent = new Intent( this, NewTaskActivity.class );
			break;

		case R.id.menu_loadData:
			intent = new Intent( this, LoadDataActivity.class );
			break;

		default:
			return super.onOptionsItemSelected( item );
		}

		if ( intent != null ) startActivity( intent );
		return true;
	}

	@Override
	public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
		needUpdate = true;
	}

	@Override
	public void onTaskChange( Task newTask, int mode ) {
		needUpdate = true;
	}

}
