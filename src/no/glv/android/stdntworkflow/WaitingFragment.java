package no.glv.android.stdntworkflow;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;

import no.glv.android.stdntworkflow.core.DialogFragmentBase;

public class WaitingFragment extends DialogFragmentBase {
	
	private ProgressBar mProgressBar;

	@Override
	protected int getRootViewID() {
		return R.layout.fr_progressbar_waiting;
	}

	@Override
	protected int getTitle() {
		return 0;
	}

	@Override
	protected void buildView( View rootView ) {
		mProgressBar = getProgressBar();
	}
	
	private ProgressBar getProgressBar() {
		if ( mProgressBar == null ) {
			mProgressBar = (ProgressBar) getRootView().findViewById( R.id.PB_newclass_indeterminate );
		}
		
		return mProgressBar;
	}
	
	public void show() {
		getProgressBar().setVisibility( View.VISIBLE );
	}
	
	public void hide() {
		getProgressBar().setVisibility( View.GONE );
	}
	
	public void kill() {
		this.finish();
	}

	/**
	 * 
	 * @param manager
	 * @param listener
	 */
	public static WaitingFragment StartFragment( FragmentManager manager ) {
		WaitingFragment fragment = new WaitingFragment();
		FragmentTransaction ft = manager.beginTransaction();
		fragment.show( ft, fragment.getClass().getSimpleName() );
		
		return fragment;
	}
}
