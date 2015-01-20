package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
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
public class InstalledClassesFragment extends InstalledDataFragment implements OnStudentClassChangeListener {

    public InstalledClassesFragment() {
	super();
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
	dataHandler.addOnStudentClassChangeListener( this );
	
        super.onCreate( savedInstanceState );	
    }
    
    @Override
    public void onDestroy() {
	super.onDestroy();
    }
    
    @Override
    public int getViewGruopLayoutID() {
	return R.layout.fr_installedclasses_new;
    }

    @Override
    public List<String> getNames() {
	return dataHandler.getStudentClassNames();
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
	inflater.inflate( R.menu.menu_fr_installedclasses, menu );
    }
    
    @Override
    public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
	onDataChange( mode );
    }

}
