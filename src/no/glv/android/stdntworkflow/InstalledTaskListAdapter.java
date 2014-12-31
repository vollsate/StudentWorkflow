package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.StudentClass;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledTaskListAdapter extends ArrayAdapter<String> {

	/**  */
	private static final String TAG = InstalledTaskListAdapter.class.getSimpleName();

	private List<String> mTasks;
	
	private BaseActivity baseActivity;

	
	/**
	 * 
	 * @param context
	 * @param resource
	 */
	private InstalledTaskListAdapter( Context context, int resource ) {
		super( context, resource );
	}
	
	void setBaseActivity( BaseActivity base ) {
		this.baseActivity = base;
	}

	
	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public InstalledTaskListAdapter( Context context, int resource, List<String> objects ) {
		super( context, resource, objects );
		mTasks = objects;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		if ( convertView == null ) {
			convertView = createView( getContext(), parent, position );
		}

		StudentClassHolder holder = (StudentClassHolder) convertView.getTag();
		holder.textView.setText( mTasks.get( position ).toString() );
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
		View myView = inflater.inflate( R.layout.row_tasks_list, parent, false );
		StudentClassHolder holder = new StudentClassHolder();

		TextView textView = (TextView) myView.findViewById( R.id.TV_main_taskName );

		// Setting the StudentClassname as a TAG for this view
		textView.setTag( mTasks.get( position ) );
		textView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );
/*
				Intent intent = new Intent( getContext(), StudentListActivity.class );
				String studentClass = (String) v.getTag();
				baseActivity.putStudentClassExtra( studentClass, intent );
				InstalledTaskListAdapter.this.getContext().startActivity( intent );
*/
			}
		} );

		holder.textView = textView;
		holder.ID = position;
		myView.setTag( holder );

		return myView;
	}

	
	
	/**
	 * 
	 * @author GleVoll
	 *
	 */
	static class StudentClassHolder {

		TextView textView;
		int ID;

	}
}
