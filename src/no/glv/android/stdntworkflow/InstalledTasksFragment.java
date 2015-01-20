package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnTasksChangedListener;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledTasksFragment extends InstalledDataFragment implements OnTasksChangedListener {

    public InstalledTasksFragment() {
	super();
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        
        dataHandler.addOnTaskChangeListener( this );
    }

    @Override
    public int getViewGruopLayoutID() {
	return R.layout.fr_installedtasks;
    }

    @Override
    public List<String> getNames() {
	return dataHandler.getTaskNames();
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
	inflater.inflate( R.menu.menu_fr_installedclasses, menu );
    }
    
    @Override
    public void onTaskChange( Task newTask, int mode ) {
	onDataChange( mode );
    }
}
