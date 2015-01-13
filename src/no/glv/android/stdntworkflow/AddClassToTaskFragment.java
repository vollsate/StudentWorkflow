package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AddClassToTaskFragment extends ViewGroupAdapter {

	private List<String> mClasses;
	private Task task;

	@Override
	public int getViewGruopLayoutID() {
		return R.layout.fr_installedclasses_new;
	}

	@Override
	protected void doCreateView( ViewGroup rootView ) {
		mClasses = dataHandler.getStudentClassNames();
		task = (Task) getArguments().getSerializable( "task" );

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

				if ( isChecked ) task.addClass( stdcClass );
				else task.removeClass( stdcClass );
			}
		} );
		
	}

}
