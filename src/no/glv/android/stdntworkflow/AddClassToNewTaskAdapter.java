package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Task;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * This adapter will display all the students in a class, with a checkbox. The
 * checkbox may be used to remove a certain student from the task.
 * 
 * @author GleVoll
 *
 */
public class AddClassToNewTaskAdapter extends ArrayAdapter<String> {

	/**  */
	private static final String TAG = AddClassToNewTaskAdapter.class.getSimpleName();

	private List<String> mClasses;

	private Task task;

	/**
	 * 
	 * @param context
	 * @param resource
	 */
	private AddClassToNewTaskAdapter( Context context, int resource ) {
		super( context, resource );
	}

	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public AddClassToNewTaskAdapter( Context context, int resource, List<String> objects ) {
		super( context, resource, objects );
		mClasses = objects;
	}

	/**
	 * 
	 * @param task
	 */
	public void setTask( Task task ) {
		this.task = task;
	}

	/**
	 * 
	 */
	public View getView( int position, View convertView, ViewGroup parent ) {
		if ( convertView == null ) {
			convertView = createView( getContext(), parent, position );
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.textView.setText( mClasses.get( position ).toString() );
		holder.ID = position;

		return convertView;
	}

	/**
	 * 
	 * @param context
	 * @param parent
	 * @param position
	 * @return
	 */
	private View createView( Context context, ViewGroup parent, int position ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.row_addedstudents_newtask, parent, false );
		ViewHolder holder = new ViewHolder();

		TextView textView = (TextView) myView.findViewById( R.id.TV_newTask_studentIdent );
		CheckBox chBox = (CheckBox) myView.findViewById( R.id.CB_newTask_addStudent );

		// Setting the StudentClassname as a TAG for this view
		textView.setTag( mClasses.get( position ) );
		textView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );
			}
		} );

		chBox.setTag( mClasses.get( position ) );
		chBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
				String name = (String) buttonView.getTag();

				if ( isChecked ) task.addClass( name );
				else task.removeClass( name );
			}
		} );

		holder.textView = textView;
		holder.checkBox = chBox;
		holder.ID = position;
		myView.setTag( holder );

		return myView;
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	static class ViewHolder {

		TextView textView;
		CheckBox checkBox;
		int ID;

	}
}
