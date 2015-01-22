package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public abstract class InstalledDataFragment extends ViewGroupAdapter  {
    
    public static final String EXTRA_SHOWCOUNT = BaseValues.EXTRA_BASEPARAM + "classesCount";

    protected int showClassesCount;

    @Override
    public final void doCreate( Bundle savedInstanceState ) {
	showClassesCount = getArguments().getInt( EXTRA_SHOWCOUNT, 1 );
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

	for ( int i = 0; i < list.size(); i++ ) {
	    if ( i >= showClassesCount ) break;
	    String name = list.get( i );

	    View row = buildRow( name, i );
	    rootView.addView( row );
	}
    }
    
    protected abstract View buildRow( final String name, int pos );    

    public abstract int getRowLayoutID();


    /**
     * Get the intent for an onClick event in this Fragment.
     * 
     * @param name
     * @param context
     * @return
     */
    public abstract Intent createIntent( String name, Context context );

    /**
     * 
     * @param name
     * @param mode
     */
    public void onDataChange( int mode ) {
	ViewGroup rootView = getRootView();
	switch ( mode ) {
	    case OnStudentClassChangeListener.MODE_ADD:
	    case OnStudentClassChangeListener.MODE_UPD:
	    case OnStudentClassChangeListener.MODE_DEL:
		rootView.removeAllViewsInLayout();
		notifyDataSetChanged();
		break;

	    default:
		break;
	}
    }
}
