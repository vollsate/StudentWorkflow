package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnTasksChangedListener;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class will show any installed tasks in the system. You may specify
 * certain arguments in order to control the output from this fragment.
 * 
 * <p>
 * The following arguments are leagal: <blockquote>
 * <table>
 * <tr>
 * <td><tt>Task.TASK_STATE_OPEN</tt></td>
 * <td>Shows any open tasks in the system
 * </tr>
 * <tr>
 * <td><tt>Task.TASK_STATE_CLOSED</tt></td>
 * <td>Shows any closed tasks in the system
 * </tr>
 * <tr>
 * <td><tt>Task.TASK_STATE_EXPIRED</tt></td>
 * <td>Shows any expired in the system
 * </tr>
 * </table>
 * </blockquote>
 * 
 * @author GleVoll
 *
 */
public class InstalledTasksFragment extends InstalledDataFragment implements OnTasksChangedListener {
	
	/** This is used as a flag, and may contain none, one or all known states  */
	public static final String PARAM_TASK_STATE = BaseValues.EXTRA_BASEPARAM + "taskState";
	
	private int taskState;

	/**
	 * 
	 */
	public InstalledTasksFragment() {
		super();
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		if ( savedInstanceState != null ) {
			taskState = savedInstanceState.getInt( PARAM_TASK_STATE );
		}
		else {
			taskState = getArguments().getInt( PARAM_TASK_STATE );
		}

		dataHandler.addOnTaskChangeListener( this );
	}
	
	@Override
	public void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );
		
		outState.putInt( PARAM_TASK_STATE, taskState );
	}

	@Override
	public int getViewGruopLayoutID() {
		return R.layout.fr_installedtasks;
	}

	@Override
	public List<String> getNames() {
		return dataHandler.getTaskNames( taskState );
	}

	@Override
	public int getRowLayoutID() {
		return R.layout.row_installed_task;
	}

	@Override
	protected View buildRow( final String name, int pos ) {
		ViewGroup vg = (ViewGroup) inflateView( getRowLayoutID() );
		Task task = dataHandler.getTask( name );

		TextView tvName = (TextView) vg.findViewById( R.id.TV_task_name );
		tvName.setText( name );
		tvName.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Intent intent = createIntent( name, getActivity() );
				if ( intent != null )
					startActivity( intent );
			}
		} );

		TextView tvDesc = (TextView) vg.findViewById( R.id.TV_task_desc );

		String desc = task.getDesciption();
		if ( desc.length() > 30 ) {
			desc = desc.substring( 0, 30 ) + "...";
		}
		tvDesc.setText( desc );

		return vg;
	}

	@Override
	public Intent createIntent( String name, Context context ) {
		return TaskActivity.CreateActivityIntent( name, context );
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.menu_fr_installedclasses, menu );
	}

	@Override
	public void onTaskChange( Task newTask, int mode ) {
		onDataChange( mode );
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------

	public static interface TaskArguments {

		public static final int TASK_ONLY_OPEN = 0;
	}
}
