package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.core.DataHandler.OnTaskChangedListener;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public abstract class InstalledDataFragment extends ViewGroupAdapter implements OnStudentClassChangeListener, OnTaskChangedListener {

	public static final String EXTRA_SHOWCOUNT = BaseValues.EXTRA_BASEPARAM + "classesCount";

	int showClassesCount;

	@Override
	public final void doCreate( Bundle savedInstanceState ) {
		showClassesCount = getArguments().getInt( EXTRA_SHOWCOUNT, 1 );
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public abstract int getViewGruopLayoutID();

	public abstract List<String> getNames();

	/**
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected void doCreateView( ViewGroup rootView ) {
		final List<String> list = getNames();
		
		for ( int i=0 ; i<list.size() ; i++ ) {
			if ( i >= showClassesCount ) break;
			String name = list.get( i );
			
			View row = buildRow( name, i );
			rootView.addView( row );
		}
	}
	
	public abstract int getRowLayoutID();
		
	/**
	 * 
	 * @param name
	 * @return
	 */
	private View buildRow( final String name, int pos ) {
		TextView textView = inflateTextView( getRowLayoutID() );
		textView.setText( name );
		textView.setTag( String.valueOf( pos ) );
		
		textView.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Intent intent = createIntent( name, getActivity() );
				startActivity( intent );
			}
		} );
						
		return textView;
	}
	
	public abstract Intent createIntent( String name, Context context );
	
	
	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		// super.onCreateOptionsMenu( menu, inflater );
		inflater.inflate( R.menu.menu_fr_installedclasses, menu );
	}

	@Override
	public void onTaskChange( Task newTask, int mode ) {
		onDataChange( newTask.getName(), mode );
	}

	@Override
	public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
		onDataChange( stdClass.getName(), mode );
	}
		
	/**
	 * 
	 * @param name
	 * @param mode
	 */
	private void onDataChange( String name, int mode ) {
		ViewGroup rootView = getRootView();
		switch ( mode ) {
		case OnStudentClassChangeListener.MODE_ADD:
		case OnStudentClassChangeListener.MODE_UPD:
			rootView.removeAllViewsInLayout();
			doCreateView( rootView );
			break;
			
		case OnStudentClassChangeListener.MODE_DEL:
			rootView.removeAllViewsInLayout();
			doCreateView( rootView );
/*
			for ( int i=0 ; i<rootView.getChildCount() ; i++ ) {
				TextView tv = ( TextView ) rootView.getChildAt( i );
				if ( tv.getText().toString().equals( name ) ) {
					rootView.removeViewAt( i );
				}
			}
*/
		default:
			break;
		}		
	}
}
