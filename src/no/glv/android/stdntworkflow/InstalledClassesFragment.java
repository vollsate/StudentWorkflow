package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledClassesFragment extends InstalledDataFragment {

    public InstalledClassesFragment() {
	super();
	DataHandler.GetInstance().addOnStudentClassChangeListener( this );
    }

    @Override
    public int getViewGruopLayoutID() {
	return R.layout.fr_installedclasses_new;
    }

    @Override
    public List<String> getNames() {
	return DataHandler.GetInstance().getStudentClassNames();
    }

    @Override
    public int getRowLayoutID() {
	return R.layout.row_installed_class;
    }

    @Override
    public Intent createIntent( String name, Context context ) {
	return StdClassListActivity.CreateActivityIntent( name, context );
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
	// super.onCreateOptionsMenu( menu, inflater );
	inflater.inflate( R.menu.menu_fr_installedclasses, menu );
    }

}
