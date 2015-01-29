package no.glv.android.stdntworkflow;

import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import no.glv.android.stdntworkflow.InstalledClassesFragment.ClassViewConfig;
import no.glv.android.stdntworkflow.intrfc.Task;

/**
 * 
 * @author glevoll
 *
 */
public class InstalledClassesInTaskFragment extends InstalledClassesFragment {

	public static final String PARAM_TASK = "task";

	@Override
	public List<String> getNames() {
		String name = getArguments().getString( PARAM_TASK );
		Task tas = dataHandler.getTask( name );
		return tas.getClasses();
	}

	@Override
	public Intent createIntent( String name, Context context ) {
		return StdClassListActivity.CreateActivityIntent( name, getActivity() );
	}

	/**
	 * 
	 * @param task
	 * @param manager
	 * @param replace
	 * @return
	 */
	public static void NewInstance( Task task, FragmentManager manager ) {
		ClassViewConfig config = new ClassViewConfig();
		config.showStudentCount = false;
		Bundle args = new Bundle();
		args.putString( InstalledClassesInTaskFragment.PARAM_TASK, task.getName() );

		StartFragment( manager, config, args, new InstalledClassesInTaskFragment() );
	}

}
