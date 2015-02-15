package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.SettingsManager;
import no.glv.android.stdntworkflow.core.Utils;
import no.glv.android.stdntworkflow.intrfc.Student;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
public class StudentListAdapter extends BaseExpandableListAdapter {

	/**  */
	@SuppressWarnings("unused")
	private static final String TAG = StudentListAdapter.class.getSimpleName();

	private SettingsManager mSettingsManager;
	private List<Student> students;
	private Context context;

	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public StudentListAdapter( Context context, List<Student> objects ) {
		// super( context, R.layout.row_stdclass_list, objects );
		mSettingsManager = DataHandler.GetInstance().getSettingsManager();
		students = objects;
		this.context = context;
	}

	private Student getItem( int pos ) {
		return students.get( pos );
	}

	/**
	 * 
	 */
	public View getView( int position, View convertView, ViewGroup parent ) {
		Student student = getItem( position );

		if ( convertView == null )
			convertView = createView( parent, student );

		if ( position % 2 == 0 )
			convertView.setBackgroundColor( context.getResources().getColor(
					R.color.task_stdlist_dark ) );
		else
			convertView.setBackgroundColor( context.getResources().getColor( R.color.task_stdlist_light ) );

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.imgTaskView.setTag( student );
		holder.id = position;

		if ( mSettingsManager.isShowFullname() )
			holder.textView.setText( student.getFirstName() + " "
					+ student.getLastName() );
		else
			holder.textView.setText( student.getFirstName() );

		holder.identText.setText( student.getIdent() );
		holder.birthText.setText( Utils.GetDateAsString( student.getBirth() ) );

		/*
		 * if ( position % 2 == 0 ) convertView.setBackgroundColor(
		 * getContext().getResources().getColor( R.color.task_stdlist_dark ) );
		 * else convertView.setBackgroundColor(
		 * getContext().getResources().getColor( R.color.task_stdlist_light ) );
		 */
		return convertView;
	}

	/**
	 * 
	 * @param context
	 * @param parent
	 * @return
	 */
	private View createView( ViewGroup parent, final Student student ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.row_stdclass_list, parent, false );
/*		
		RelativeLayout ll = (RelativeLayout) myView.findViewById( R.id.LL_stdList_container );
		ll.setTag( student );
		ll.setOnLongClickListener( new OnLongClickListener() {
			
			@Override
			public boolean onLongClick( View v ) {
				Student std = getItem( ( (ViewHolder) v.getTag()).id );
				StdInfoActivity.StartActivity( context, std );
				
				return true;
			}
		} );
*/
		ViewHolder holder = new ViewHolder();

		ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
		imgTaskView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Toast.makeText( context, "Will implement individual StudentTask soon..", Toast.LENGTH_LONG )
						.show();
			}
		} );

		ImageView ivInfo = (ImageView) myView.findViewById( R.id.info );
		ivInfo.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				StdInfoActivity.StartActivity( context, student );
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

	@Override
	public int getGroupCount() {
		return students.size();
	}

	@Override
	public int getChildrenCount( int groupPosition ) {
		return 1;
	}

	@Override
	public Object getGroup( int groupPosition ) {
		return getItem( groupPosition );
	}

	@Override
	public Object getChild( int groupPosition, int childPosition ) {
		return getItem( groupPosition );
	}

	@Override
	public long getGroupId( int groupPosition ) {
		return groupPosition;
	}

	@Override
	public long getChildId( int groupPosition, int childPosition ) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ) {
		View view = getView( groupPosition, convertView, parent );

		return view;
	}

	@Override
	public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent ) {

		final Student st = getItem( groupPosition );

		if ( convertView == null ) {
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE );
			convertView = inflater.inflate( R.layout.row_stdlist_stditem, parent, false );

			StudentItemHolder holder = new StudentItemHolder();

			final Spinner spClass = (Spinner) convertView.findViewById( R.id.SP_stdList_stditem_classes );
			holder.spClass = spClass;

			spClass.setOnItemSelectedListener( new OnItemSelectedListener() {
				@Override
				public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
					String grade = spClass.getSelectedItem().toString();
					
					if ( st.getGrade() != null && !st.getGrade().equals( grade ) ) {					
						st.setGrade( grade );
						DataHandler.GetInstance().updateStudent( st, null );
					}
				}

				@Override
				public void onNothingSelected( AdapterView<?> parent ) {
				}

			} );

			ImageView ivSave = (ImageView) convertView.findViewById( R.id.IV_stdList_stditem_save );
			ivSave.setTag( holder );
			ivSave.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					DataHandler.GetInstance().updateStudent( st, null );
				}
			} );

			convertView.setTag( holder );
		}
		
		StudentItemHolder holder = (StudentItemHolder) convertView.getTag();

		if ( st.getGrade() != null && st.getGrade().length() > 0 ) {
			int sel = (int) st.getGrade().charAt( 0 );
			holder.spClass.setSelection( sel - (int) 'A' );
		}
		else {
			String[] classes = context.getResources().getStringArray( R.array.stdList_classes );
			holder.spClass.setSelection( classes.length - 1 );
		}
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable( int groupPosition, int childPosition ) {
		// TODO Auto-generated method stub
		return false;
	}

	static class StudentItemHolder {

		Spinner spClass;
	}

}
