package no.glv.android.stdntworkflow.core;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public abstract class ViewGroupAdapter extends BaseFragment {

	private static final String TAG = ViewGroupAdapter.class.getSimpleName();

	private ViewGroup rootView;
	private LayoutInflater inflater;
	protected ViewGroup container;

	private boolean mModified;

	/**
	 * 
	 * @return
	 */
	public abstract int getViewGruopLayoutID();

	/**
	 * 
	 */
	public final void notifyDataSetChanged() {
		mModified = true;
		doCreateView( rootView );
		mModified = false;
	}

	/**
	 * 
	 */
	public final void invalidateView() {
		rootView.removeAllViews();
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isModified() {
		return mModified;
	}

	@Override
	public final View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		Log.i( TAG, "onCreateView()" );

		try {
			rootView = (ViewGroup) inflater.inflate( getViewGruopLayoutID(), container, false );
		}
		catch ( Exception e ) {
			rootView = (ViewGroup) container.findViewById( getViewGruopLayoutID() );
		}

		if ( rootView == null ) {
			throw new IllegalStateException( "Cannot find rootview for class: " + getClass().getSimpleName() );
		}

		this.inflater = inflater;
		this.container = container;

		doCreateView( rootView );
		return rootView;
	}

	/**
	 * 
	 * @return
	 */
	protected ViewGroup getRootView() {
		return rootView;
	}

	public TextView getTextView( int id ) {
		return (TextView) rootView.findViewById( id );
	}

	/**
	 * 
	 * @return
	 */
	protected LayoutInflater getInflater() {
		return inflater;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public TextView inflateTextView( int id ) {
		return (TextView) inflateView( id );
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public View inflateView( int id ) {
		return inflater.inflate( id, rootView, false );
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ViewGroup inflateViewGroup( int id ) {
		return (ViewGroup) inflateView( id );
	}

	/**
	 * 
	 * @param manager
	 * @param adapter
	 * @param args
	 * @param id
	 * @return
	 */
	public static void BeginFragmentTransaction( FragmentManager manager, ViewGroupAdapter adapter, Bundle args, int id ) {
		adapter.setArguments( args );
		manager.beginTransaction().add( id, adapter ).commit();
	}

	/**
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract void doCreateView( ViewGroup rootView );

}
