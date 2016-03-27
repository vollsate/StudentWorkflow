package no.glv.android.stdntworkflow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnChangeListener;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.BaseValues;

/**
 * 
 * @author GleVoll
 *
 */
public abstract class InstalledDataFragment<T> extends ViewGroupAdapter {

	public static final String PARAM_CONFIG = BaseValues.EXTRA_BASEPARAM + "config";
	
	public abstract int getViewGroupLayoutID();

	public abstract List<T> getNames();

	protected abstract DataConfig getConfig();
		
	/**
	 * Will create the view that shows the individual rows. This method will
	 * limit the number of rows to show. In order to ignore this limit, an extra
	 * parameter must be set to <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @param rootView The root {@link ViewGroup} to add the individual rows to.
	 */
	protected void doCreateView( ViewGroup rootView ) {		
		final List<T> list = getNames();

		for ( int i = 0; i < list.size(); i++ ) {
			if ( getConfig() != null && getConfig().showCount > 0 && i >= getConfig().showCount )
				break;
			T name = list.get( i );

			View row = buildRow( name, i );
			rootView.addView( row );
		}
	}

	protected abstract View buildRow( final T name, int pos );

	public abstract int getRowLayoutID();

	/**
	 * Get the intent for an onClick event in this Fragment.
	 * 
	 * @param name
	 * @param context
	 * @return
	 */
	public abstract Intent createIntent( T name, Context context );

	/**
	 * 
	 * @param mode
	 */
	public void onDataChange( int mode ) {
		switch ( mode ) {
			case OnChangeListener.MODE_ADD:
			case OnChangeListener.MODE_UPD:
			case OnChangeListener.MODE_DEL:
			case OnChangeListener.MODE_CLS:
				invalidateView();
				break;

			default:
				break;
		}
	}
	
	public static class DataConfig implements Serializable {
		
		/** InstalledDataFragment.java */
		private static final long serialVersionUID = 1L;
		
		public int showCount = -1;
		public int sortBy = -1;
	}
}
