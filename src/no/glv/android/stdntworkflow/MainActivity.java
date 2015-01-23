package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.DataHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	public static final String STATE_APP_INIT = "stdntWF.init";

	DataHandler dataHandler;

	public MainActivity() {
		Log.i( TAG, "Constructor" );
	}

	/**
	 * Will initiate the {@link DataHandler} for the rest of the application.
	 * 
	 * Listeners for StudentClass change and Task change are added
	 */
	private void init() {
		dataHandler = DataHandler.Init( getApplication() );
	}

	@Override
	protected void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );

		Log.i( TAG, "onSaveInstanceState()" );
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		Log.i( TAG, "onCreate()" );

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		setTitle( getResources().getString( R.string.app_name ) );
		init();

		getInstalledClassesFR( savedInstanceState, false );
		getInstalledTasksFR( savedInstanceState, false );
	}

	/**
	 * 
	 * @param inState
	 * @return
	 */
	private InstalledTasksFragment getInstalledTasksFR( Bundle inState, boolean forceReplace ) {
		if ( forceReplace ) {
			return startTaskFramgent( forceReplace );
		}

		if ( inState == null ) {
			return startTaskFramgent( false );
		}

		return (InstalledTasksFragment) getFragmentManager().findFragmentById( R.id.FR_installedTasks_container );
	}

	/**
	 * 
	 * @param replace
	 * @return
	 */
	private InstalledTasksFragment startTaskFramgent( boolean replace ) {
		InstalledTasksFragment tasksFragment = new InstalledTasksFragment();
		Bundle args = new Bundle();
		args.putInt( InstalledTasksFragment.EXTRA_SHOWCOUNT, dataHandler.getSettingsManager().getShowCount() );
		tasksFragment.setArguments( args );

		FragmentTransaction tr = getFragmentManager().beginTransaction();

		if ( replace ) {
			tr.replace( R.id.FR_installedTasks_container, tasksFragment ).commit();
		}
		else
			tr.add( R.id.FR_installedTasks_container, tasksFragment ).commit();

		return tasksFragment;
	}

	/**
	 * 
	 * @param inState
	 * @return
	 */
	private InstalledClassesFragment getInstalledClassesFR( Bundle inState, boolean forceReplace ) {
		if ( forceReplace )
			startClassesFragment( forceReplace );

		if ( inState == null ) {
			startClassesFragment( false );
		}

		return (InstalledClassesFragment) getFragmentManager().findFragmentById( R.id.FR_installedClasses_container );
	}

	/**
	 * 
	 * @param replace
	 * @return
	 */
	private InstalledClassesFragment startClassesFragment( boolean replace ) {
		InstalledClassesFragment classesFragment = new InstalledClassesFragment();
		Bundle args = new Bundle();
		args.putInt( InstalledClassesFragment.EXTRA_SHOWCOUNT, dataHandler.getSettingsManager().getShowCount() );
		classesFragment.setArguments( args );

		FragmentTransaction tr = getFragmentManager().beginTransaction();
		if ( replace )
			tr.replace( R.id.FR_installedClasses_container, classesFragment ).commit();
		else
			tr.add( R.id.FR_installedClasses_container, classesFragment ).commit();

		return classesFragment;
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
				intent = new Intent( this, SettingsActivity.class );
				break;

			case R.id.menu_resetDB:
				resetDB();
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

		if ( intent != null )
			startActivity( intent );
		return true;
	}

	private void resetDB() {
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		String msg = getResources().getString( R.string.action_resetDB_msg );

		builder.setMessage( msg ).setTitle( R.string.action_resetDB );
		builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which ) {
				dataHandler.resetDB();
				Toast.makeText( MainActivity.this, R.string.action_resetDB_done, Toast.LENGTH_LONG ).show();
			}
		} );

		builder.setNegativeButton( "Avbryt", null );

		AlertDialog dialog = builder.create();
		dialog.show();

	}

}
