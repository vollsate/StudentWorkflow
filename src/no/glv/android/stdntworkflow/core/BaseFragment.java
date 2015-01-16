/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import android.support.v4.app.Fragment;

/**
 * @author GleVoll
 *
 */
public class BaseFragment extends Fragment {

    private final BaseActivity baseActivity;

    /**
	 * 
	 */
    public BaseFragment( BaseActivity base ) {
	super();

	this.baseActivity = base;
    }

    public BaseFragment() {
	super();

	baseActivity = null;
    }

    protected BaseActivity getBaseActivity() {
	return baseActivity;
    }

}
