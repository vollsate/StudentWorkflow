package no.glv.android.stdntworkflow.core;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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

	private ViewGroup rootView;
	private LayoutInflater inflater;
	protected ViewGroup container;
	protected Bundle savedInstanceState;
	protected DataHandler dataHandler;

	/**
	 * 
	 */
	public ViewGroupAdapter() {
		dataHandler = DataHandler.GetInstance();
	}

	@Override
	public final void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.savedInstanceState = savedInstanceState;

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
		try {
			rootView = ( ViewGroup ) inflater.inflate( getViewGruopLayoutID(), container, false );
		}
		catch ( Exception e ) {
			rootView = (ViewGroup) container.findViewById( getViewGruopLayoutID() );					
		}
		this.inflater = inflater;
		this.container = container;
		this.savedInstanceState = savedInstanceState;
		
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
		return (ViewGroup ) inflateView( id );
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
		manager.beginTransaction().add( id, adapter )
				.commit();
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
