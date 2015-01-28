package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Main fragment that shows the first page. This fragment will list all the
 * classes installed on the system and any open tasks.
 * 
 * <p>
 * 
 * - The user may look at a class or a task - A new task may be loaded - A new
 * class may be installed
 * 
 * Uses fragments to show the installed classes and installed tasks.
 * 
 * @author GleVoll
 *
 */
public class MainFragment extends Fragment {

	private static final String TAG = MainFragment.class.getSimpleName();

	public static final String STATE_APP_INIT = "stdntWF.init";

	DataHandler dataHandler;

	public MainFragment() {
		Log.i( TAG, "Constructor" );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu( true );

		dataHandler = DataHandler.GetInstance();
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		Log.i( TAG, "onCreate()" );

		super.onCreate( savedInstanceState );
		// setContentView( R.layout.activity_main );
		View rootView = inflater.inflate( R.layout.activity_main, container, false );

		getActivity().setTitle( getResources().getString( R.string.app_name ) );

		getInstalledClassesFR( savedInstanceState, false );
		getInstalledTasksFR( savedInstanceState, false );

		return rootView;
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
		args.putInt( InstalledTasksFragment.PARAM_TASK_STATE, Task.TASK_STATE_OPEN );
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
	public boolean onCreateOptionsMenu( Menu menu ) {
		// getMenuInflater().inflate( R.menu.menu_main, menu );
		return true;
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.menu_main, menu );
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
				intent = new Intent( getActivity(), SettingsActivity.class );
				break;

			case R.id.menu_resetDB:
				resetDB();
				break;

			case R.id.menu_newTask:
				intent = new Intent( getActivity(), NewTaskActivity.class );
				break;

			case R.id.menu_loadData:
				intent = new Intent( getActivity(), LoadDataActivity.class );
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
		AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
		String msg = getResources().getString( R.string.action_resetDB_msg );

		builder.setMessage( msg ).setTitle( R.string.action_resetDB );
		builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which ) {
				dataHandler.resetDB();
				Toast.makeText( MainFragment.this.getActivity(), R.string.action_resetDB_done, Toast.LENGTH_LONG )
						.show();
			}
		} );

		builder.setNegativeButton( "Avbryt", null );

		AlertDialog dialog = builder.create();
		dialog.show();

	}

}
