package no.glv.android.stdntworkflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledTasksFragment extends InstalledDataFragment {
	
	public InstalledTasksFragment() {
		super();
		DataHandler.GetInstance().addOnTaskChangeListener( this );		
	}

	@Override
	public int getViewGruopLayoutID() {
		return R.layout.fr_installedtasks;
	}

	@Override
	public List<String> getNames() {
		Iterator<Task> tasks = dataHandler.getTasks().iterator();
		List<String> list = new ArrayList<String>(  );

		while ( tasks.hasNext() ) {
			Task task = tasks.next();
			if ( task.getType() == Task.TASK_OPEN )
				list.add( task.getName() );
		}
		
		return list;
	}

	@Override
	public int getRowLayoutID() {
		return R.layout.row_installed_class;
	}

	@Override
	public Intent createIntent( String name, Context context ) {
		return TaskActivity.CreateActivityIntent( name, context );
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		// super.onCreateOptionsMenu( menu, inflater );
		inflater.inflate( R.menu.menu_fr_installedclasses, menu );
	}
}
