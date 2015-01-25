package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.SettingsManager;
import no.glv.android.stdntworkflow.intrfc.Student;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Adapter will list all the student in a given StudentClass. This class
 * will load a XML layout row: row_student_list.
 * 
 * This list MUST display the first name of every student, and the
 * 
 * 
 * @author GleVoll
 *
 */
public class StudentListAdapter extends ArrayAdapter<Student> {

	/**  */
	@SuppressWarnings("unused")
	private static final String TAG = StudentListAdapter.class.getSimpleName();

	private SettingsManager mSettingsManager;

	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public StudentListAdapter( Context context, List<Student> objects ) {
		super( context, R.layout.row_stdclass_list, objects );
		mSettingsManager = DataHandler.GetInstance().getSettingsManager();
	}

	/**
	 * 
	 */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		Student student = getItem( position );

		if ( convertView == null )
			convertView = createView( parent, student );

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.imgTaskView.setTag( student );
		holder.id = position;

		if ( mSettingsManager.isShowFullname() )
			holder.textView.setText( student.getFirstName() + " "
					+ student.getLastName() );
		else
			holder.textView.setText( student.getFirstName() );

		holder.identText.setText( student.getIdent() );
		holder.birthText.setText( student.getBirth() );

/*
		if ( position % 2 == 0 )
			convertView.setBackgroundColor( getContext().getResources().getColor(
					R.color.task_stdlist_dark ) );
		else
			convertView.setBackgroundColor( getContext().getResources().getColor( R.color.task_stdlist_light ) );
*/
		return convertView;
	}

	/**
	 * 
	 * @param context
	 * @param parent
	 * @return
	 */
	private View createView( ViewGroup parent, Student student ) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.row_stdclass_list, parent, false );

		ViewHolder holder = new ViewHolder();

		ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
		imgTaskView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Toast.makeText( getContext(), "Will implement individual StudentTask soon..", Toast.LENGTH_LONG )
						.show();
			}
		} );

		TextView textView = (TextView) myView.findViewById( R.id.TV_stdlist_name );
		textView.setTag( student );
		holder.textView = textView;

		textView = (TextView) myView.findViewById( R.id.TV_stdlist_ident );
		holder.identText = textView;

		textView = (TextView) myView.findViewById( R.id.TV_stdlist_birth );
		holder.birthText = textView;

		holder.imgTaskView = imgTaskView;

		myView.setTag( holder );

		return myView;
	}

	static class ViewHolder {
		int id;

		TextView textView;
		TextView identText;
		TextView birthText;
		ImageView imgTaskView;

	}

}
