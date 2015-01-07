package no.glv.android.stdntworkflow;

import java.io.BufferedInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.BaseTabActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DatePickerDialogHelper;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;
import no.glv.android.stdntworkflow.sql.DBUtils;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Shows detailed information about av specific task.
 * 
 * The {@link Task} have information about itself and when it's due, and
 * information about all the students currently involved in the Task: pending,
 * cancelled or handed in (finished).
 * 
 * @author GleVoll
 *
 */
public class TaskActivity extends BaseTabActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private BaseTabFragment[] fragments;

	Task mTask;

	@Override
	protected String getTabTitle() {
		return getString( R.string.task_title );
	}

	@Override
	public BaseTabFragment[] getFragments() {
		if ( fragments == null ) {
			fragments = new BaseTabFragment[] { new TaskInfoFragment(), new TaskClassesFragment() };
		}

		return fragments;
	}

	@Override
	public String[] getTabTitles() {
		return new String[] { getString( R.string.task_tab_section1 ), getString( R.string.task_tab_section2 ) };
	}

	@Override
	public int getLayoutID() {
		return R.layout.activity_task;
	}

	@Override
	public int getViewpagerID() {
		return R.id.VP_task_pager;
	}

	String getTaskName() {
		return getIntent().getStringExtra( Task.EXTRA_TASKNAME );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.task, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
		case R.id.task_action_Update:
			String newName = ((TextView) findViewById( R.id.ET_task_name )).getText().toString();
			String newDesc = ((TextView) findViewById( R.id.ET_task_desc )).getText().toString();
			String newDate = ((TextView) findViewById( R.id.ET_task_date )).getText().toString();

			String oldName = mTask.getName();
			Date date = BaseActivity.GetDateFromString( newDate );

			mTask.setName( newName );
			mTask.setDescription( newDesc );
			mTask.setDate( date );

			getDataHandler().updateTask( mTask, oldName );
			return true;

		case R.id.task_action_Delete:
			if ( getDataHandler().deleteTask( mTask.getName() ) ) finish();

		default:
			break;
		}

		return super.onOptionsItemSelected( item );
	}

	@Override
	public void onTabSelected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
		super.onTabSelected( tab, fragmentTransaction );
	}

	@Override
	public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
	}

	@Override
	public void onTabReselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class TaskInfoFragment extends BaseTabFragment implements OnDateSetListener {
		
		private Task task;

		@Override
		public View doCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			Task task = getTask();

			getTextView( R.id.TV_task_header ).setText( getString( R.string.task_header ) );

			getEditText( R.id.ET_task_name ).setText( task.getName() );
			getEditText( R.id.ET_task_desc ).setText( task.getDesciption() );
			getEditText( R.id.ET_task_date ).setText( BaseActivity.GetDateAsString( task.getDate() ) );

			Button btn = getButton( R.id.BTN_task_date );
			btn.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					Task task = getTask();
					DatePickerDialogHelper.OpenDatePickerDialog( task.getDate(), getActivity(), TaskInfoFragment.this,
							false, true );

				}
			} );

			((TaskActivity) getBaseTabActivity()).mTask = task;
			
			return rootView;
		}
		
		/**
		 * 
		 * @return
		 */
		protected Task getTask() {
			if ( task == null ) {			
				String taskName = ((TaskActivity) getBaseTabActivity()).getTaskName();
				task = getDataHandler().getTask( taskName );
			}
			
			return task;
		}

		@Override
		public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
			Task task = getTask();
			
			Calendar cal = Calendar.getInstance();
			cal.set( year, monthOfYear, dayOfMonth );
			task.setDate( cal.getTime() );
			
			getEditText( R.id.ET_task_date ).setText( BaseActivity.GetDateAsString( task.getDate() ) );
		}

		@Override
		protected int getRootViewID() {
			return R.layout.fragment_task_info;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class TaskClassesFragment extends BaseTabFragment {

		@Override
		public View doCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			ListView listView = getListView( R.id.LV_task_students );
			Task task = getDataHandler().getTask( ((TaskActivity) getBaseTabActivity()).getTaskName() );

			StudentListAdapter adapter = new StudentListAdapter( getActivity(), task.getStudentsInTask() );
			listView.setAdapter( adapter );

			return rootView;
		}

		@Override
		protected int getRootViewID() {
			return R.layout.fragment_task_students;
		}
	}

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	static class StudentListAdapter extends ArrayAdapter<StudentTask> {

		private List<StudentTask> students;

		public StudentListAdapter( Context ctx, List<StudentTask> stdList ) {
			super( ctx, R.layout.row_task_stdlist, stdList );

			this.students = stdList;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			StudentTask stdTask = students.get( position );
			Student std = stdTask.getStudent();

			if ( convertView == null ) {
				convertView = createView( getContext(), parent, stdTask );
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.id = position;
			holder.nameTV.setText( std.getFirstName() );
			holder.identTV.setText( std.getIdent() );
			holder.classTV.setText( std.getStudentClass() );
			holder.chkBox.setChecked( stdTask.isHandedIn() );
			
			String handinDate;
			if ( stdTask.isHandedIn() ) {
				handinDate = "Handin: " + DBUtils.ConvertToString( stdTask.getHandInDate() );
			}
			else {
				handinDate = "pending .." ;
			}
			
			holder.handinDateTV.setText( handinDate );
			
			holder.imgInfoView.setTag( stdTask );
			holder.imgTaskView.setTag( stdTask );
			holder.chkBox.setTag( stdTask );

			return convertView;
		}

		/**
		 * 
		 * @param context
		 * @param parent
		 * @param position
		 * @return
		 */
		private View createView( Context context, ViewGroup parent, StudentTask stdTask ) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View myView = inflater.inflate( R.layout.row_task_stdlist, parent, false );
			ViewHolder holder = new ViewHolder();

			holder.imgInfoView = BaseActivity.GetImageVire( myView, R.id.info );
			holder.imgInfoView.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					Intent intent = new Intent( getContext(), StdInfoActivity.class );
					
					StudentTask stdTask = ( StudentTask ) v.getTag();
					BaseActivity.putIdentExtra( stdTask.getStudent(), intent );

					getContext().startActivity( intent );
				}
			} );

			holder.imgTaskView = BaseActivity.GetImageVire( myView,  R.id.task );
			holder.imgTaskView.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					Intent intent = new Intent( getContext(), StdInfoActivity.class );
					StudentTask stdTask = ( StudentTask ) v.getTag();
					BaseActivity.putIdentExtra( stdTask.getStudent(), intent );
					getContext().startActivity( intent );
				}
			} );
			
			holder.chkBox = BaseActivity.GetCheckBox( myView, R.id.CB_task_stdlist );
			holder.chkBox.setTag( stdTask );
			holder.chkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
					StudentTask stdTask = ( StudentTask ) buttonView.getTag();
					
					if ( ! isChecked )
						stdTask.handIn( StudentTask.MODE_PENDING );
					
					DataHandler.GetInstance().handin( stdTask );
				}
			} );

			holder.nameTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_name );
			holder.nameTV.setTag( stdTask );

			holder.identTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_ident );
			holder.identTV.setTag( stdTask );

			holder.classTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_class );
			holder.classTV.setTag( stdTask );

			holder.handinDateTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_handinDate );
			holder.handinDateTV.setTag( stdTask );

			myView.setTag( holder );

			return myView;
		}

	}

	static class ViewHolder {
		int id;

		TextView nameTV;
		TextView identTV;
		TextView classTV;
		TextView handinDateTV;
		ImageView imgInfoView;
		ImageView imgTaskView;
		CheckBox chkBox;

	}
}
