package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AddClassToTaskFragment extends ViewGroupAdapter {

    private List<String> mClasses;
    private Task mTask;

    @Override
    public int getViewGruopLayoutID() {
	return R.layout.fr_installedclasses_new;
    }

    Task getTask(Bundle args) {
	if ( mTask == null ) {
	    if ( args == null ) args = getArguments();
	    
	    mTask = (Task) args.getSerializable( Task.EXTRA_TASKNAME );
	}
    
    	return mTask;
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        
        mTask = getTask( savedInstanceState );
    }

    @Override
    public void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState( outState );
        
        outState.putSerializable( Task.EXTRA_TASKNAME, mTask );
    }

    @Override
    protected void doCreateView( ViewGroup rootView ) {
	mClasses = dataHandler.getStudentClassNames();

	for ( int i = 0; i < mClasses.size(); i++ ) {
	    ViewGroup myView = inflateViewGroup( R.layout.row_newtask_addclasses );
	    buildRow( myView, i );

	    rootView.addView( myView );
	}
    }

    protected void buildRow( ViewGroup parent, int position ) {
	TextView textView = (TextView) parent.findViewById( R.id.TV_newTask_className );
	textView.setText( mClasses.get( position ) );

	CheckBox chBox = (CheckBox) parent.findViewById( R.id.CB_newTask_addClass );
	chBox.setTag( mClasses.get( position ) );
	chBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
		String name = (String) buttonView.getTag();
		StudentClass stdcClass = DataHandler.GetInstance().getStudentClass( name );

		if ( isChecked ) mTask.addClass( stdcClass );
		else
		    mTask.removeClass( stdcClass );
	    }
	} );

    }

}
