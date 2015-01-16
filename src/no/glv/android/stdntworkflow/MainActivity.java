package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.core.DataHandler.OnTaskChangedListener;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import no.glv.android.stdntworkflow.sql.Database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main task that shows the first page.
 * 
 * - The user may look at a class or a task - A new task may be loaded - A new
 * class may be installed
 * 
 * Uses fragments to show the installed classes and installed tasks.
 * 
 * @author GleVoll
 *
 */
public class MainActivity extends Activity implements OnStudentClassChangeListener, OnTaskChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean initiated;

    DataHandler dataHandler;
    InstalledClassesFragment classesFragment;
    InstalledTasksFragment tasksFragment;

    /**
     * Will initiate the {@link DataHandler} for the rest of the application.
     * 
     * Listeners for StudentClass change and Task change are added
     */
    private void init() {
	if ( initiated ) return;

	Log.d( TAG, "Initiating DataHandler" );
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
	// createListView();

	if ( savedInstanceState == null ) {
	    classesFragment = new InstalledClassesFragment();
	    Bundle args = new Bundle();
	    args.putInt( InstalledClassesFragment.EXTRA_SHOWCOUNT, dataHandler.getSettingsManager().getShowCount() );
	    classesFragment.setArguments( args );
	    getFragmentManager().beginTransaction().add( R.id.FR_installedClasses_container, classesFragment ).commit();

	    tasksFragment = new InstalledTasksFragment();
	    args = new Bundle();
	    args.putInt( InstalledTasksFragment.EXTRA_SHOWCOUNT, dataHandler.getSettingsManager().getShowCount() );
	    tasksFragment.setArguments( args );
	    getFragmentManager().beginTransaction().add( R.id.FR_installedTasks_container, tasksFragment ).commit();
	} else {
	    classesFragment = (InstalledClassesFragment) getFragmentManager().findFragmentById(
		    R.id.FR_installedClasses_container );

	    tasksFragment = (InstalledTasksFragment) getFragmentManager().findFragmentById(
		    R.id.FR_installedTasks_container );
	}
    }

    @Override
    protected void onRestart() {
	super.onRestart();
	update();
    }

    @Override
    protected void onResume() {
	super.onResume();
	update();
    }

    /**
	 * 
	 */
    private void update() {
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
	    update();
	    break;

	case R.id.menu_newTask:
	    intent = new Intent( this, NewTaskActivity.class );
	    break;

	case R.id.menu_loadData:
	    intent = new Intent( this, LoadDataActivity.class );
	    break;

	case R.id.menu_listDB:
	    dataHandler.listDB();
	    break;

	default:
	    return super.onOptionsItemSelected( item );
	}

	if ( intent != null ) startActivity( intent );
	return true;
    }

    @Override
    public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
	update();
    }

    @Override
    public void onTaskChange( Task newTask, int mode ) {
	update();
    }

}
