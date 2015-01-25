package no.glv.android.stdntworkflow.core;

import android.app.Activity;
import android.app.Fragment;
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
public abstract class ViewGroupAdapter extends Fragment {

	private static final String TAG = ViewGroupAdapter.class.getSimpleName();

	private ViewGroup rootView;
	private LayoutInflater inflater;
	protected ViewGroup container;
	protected DataHandler dataHandler;

	/**
	 * 
	 */
	public ViewGroupAdapter() {
		dataHandler = DataHandler.GetInstance();

		Log.i( TAG, "Constructor" );
	}

	@Override
	public void onAttach( Activity activity ) {
		super.onAttach( activity );

		Log.i( TAG, "onAttach()" );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i( TAG, "onDestroy()" );
	}

	@Override
	public void onDetach() {
		super.onDetach();

		Log.i( TAG, "onDetach()" );
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Log.i( TAG, "onDestroyView()" );
	}

	@Override
	public void onPause() {
		super.onPause();

		Log.i( TAG, "onPause()" );
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.i( TAG, "onResume()" );
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.i( TAG, "onStart()" );
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.i( TAG, "onStop()" );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		Log.i( TAG, "onCreate()" );

		doCreate( savedInstanceState );
	}

	/**
	 * 
	 * @param savedInstanceState
	 */
	public void doCreate( Bundle savedInstanceState ) {
	}

	/**
	 * 
	 * @return
	 */
	public abstract int getViewGruopLayoutID();

	/**
	 * 
	 */
	public final void notifyDataSetChanged() {
		doCreateView( rootView );
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
	public static void beginFragmentTransaction( FragmentManager manager, ViewGroupAdapter adapter, Bundle args, int id ) {
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
