/**
 * 
 */
package no.glv.android.stdntworkflow;

import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import no.glv.android.stdntworkflow.InstalledTasksFragment.TaskViewConfig;
import no.glv.android.stdntworkflow.core.BaseTabActivity;
import no.glv.android.stdntworkflow.core.DataComparator;
import no.glv.android.stdntworkflow.intrfc.Task;

/**
 * @author glevoll
 *
 */
@SuppressWarnings("deprecation")
public class TaskViewActivity extends BaseTabActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected( Tab tab, FragmentTransaction ft ) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected( Tab tab, FragmentTransaction ft ) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.BaseTabActivity#getLayoutID()
	 */
	@Override
	public int getLayoutID() {
		return R.layout.activity_taskview;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.BaseTabActivity#getViewpagerID()
	 */
	@Override
	public int getViewpagerID() {
		return R.id.VP_taskview_pager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.BaseTabActivity#getFragments()
	 */
	@Override
	public BaseTabFragment[] getFragments() {
		return new BaseTabFragment[] { new OpenTasksFragment(), new ClosedTasksFragment() };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.glv.android.stdntworkflow.core.BaseTabActivity#getTabTitles()
	 */
	@Override
	public String[] getTabTitles() {
		return new String[] { getString( R.string.taskview_tab_open_title ), getString( R.string.taskview_tab_close_title ) };
	}
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	public static TaskViewConfig GetConfig( int state ) {
		TaskViewConfig config = new TaskViewConfig();
		config.showCounterHandin = true;
		config.showCounterPending = true;
		config.showDescription = true;
		config.sortBy = DataComparator.SORT_TASKNAME_ASC;
		config.taskState = state;
		config.showCount = Integer.MAX_VALUE;
		
		return config;
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// 
	// 
	// 
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * 
	 * @author glevoll
	 *
	 */
	public static class OpenTasksFragment extends BaseTabFragment {
		@Override
		protected View doCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			InstalledTasksFragment.StartFragment( getFragmentManager(), GetConfig( Task.TASK_STATE_OPEN ), R.id.FR_openTasks_container );
			
			//View v = container.findViewById( R.id.FR_openTasks_container );
			
			return null;
		}

		@Override
		protected int getLayoutID() {
			return R.layout.fr_tasks_open;
		}
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * 
	 * @author glevoll
	 *
	 */
	public static class ClosedTasksFragment extends BaseTabFragment {
		@Override
		protected View doCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			InstalledTasksFragment.StartFragment( getFragmentManager(), GetConfig( Task.TASK_STATE_CLOSED ), R.id.FR_closedTasks_container);
			return null;
		}

		@Override
		protected int getLayoutID() {
			return R.layout.fr_tasks_closed;
		}
	}
}
