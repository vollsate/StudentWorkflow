package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.InstalledClassesFragment.ClassViewConfig;
import no.glv.android.stdntworkflow.InstalledTasksFragment.TaskViewConfig;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

	// private static final String TAG = MainFragment.class.getSimpleName();

	DataHandler dataHandler;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu( true );

		dataHandler = DataHandler.GetInstance();
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		View rootView = inflater.inflate( R.layout.fr_main, container, false );

		getActivity().setTitle( getResources().getString( R.string.app_name ) );

		startInstalledClassesFR( savedInstanceState );
		startInstalledTasksFR( savedInstanceState );

		return rootView;
	}

	/**
	 * 
	 * @param inState
	 * @return
	 */
	private void startInstalledTasksFR( Bundle inState ) {
		TaskViewConfig config = new TaskViewConfig();
		config.showCounterPending = true;
		config.showCounterHandin = true;
		config.showDescription = true;
		config.tastState = Task.TASK_STATE_OPEN;

		InstalledTasksFragment.StartFragment( getFragmentManager(), config );
	}

	/**
	 * 
	 * @param inState
	 * @return
	 */
	private void startInstalledClassesFR( Bundle inState ) {
		ClassViewConfig config = new ClassViewConfig();
		config.showStudentCount = true;
		
		InstalledClassesFragment.StartFragment( getFragmentManager(), config );
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

	/**
	 * Resets the database. Will start up an dialog to confirm such a reset.
	 * 
	 * <p>
	 * This action is not reversible!
	 */
	private void resetDB() {
		AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
		String msg = getResources().getString( R.string.action_resetDB_msg );

		builder.setMessage( msg ).setTitle( R.string.action_resetDB );
		builder.setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which ) {
				dataHandler.resetDB();
				Toast.makeText( MainFragment.this.getActivity(), R.string.action_resetDB_done, Toast.LENGTH_LONG )
						.show();
			}
		} );

		builder.setNegativeButton( R.string.cancel, null );

		AlertDialog dialog = builder.create();
		dialog.show();

	}

}
