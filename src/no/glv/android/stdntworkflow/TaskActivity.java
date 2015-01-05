package no.glv.android.stdntworkflow;

import java.util.Date;
import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.BaseTabActivity;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentTask;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.ActionBar;
import android.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
			fragments = new BaseTabFragment[] { 
					new TaskInfoFragment(), 
					new TaskClassesFragment() };
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
			String newName = ( (TextView) findViewById( R.id.ET_task_name )).getText().toString();
			String newDesc = ( (TextView) findViewById( R.id.ET_task_desc )).getText().toString();
			String newDate = ( (TextView) findViewById( R.id.ET_task_date )).getText().toString();
			
			String oldName = mTask.getName();
			Date date = BaseActivity.GetDateFromString( newDate );
			
			mTask.setName( newName );
			mTask.setDescription( newDesc );
			mTask.setDate( date );

			getDataHandler().updateTask( mTask, oldName );
			return true;
		
		case R.id.task_action_Delete:
			if ( getDataHandler().deleteTask( mTask.getName() ) ) 
				finish();
			
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
	public static class TaskInfoFragment extends BaseTabFragment  {
		
		@Override
		public View doCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			String taskName = ( ( TaskActivity )getBaseTabActivity()).getTaskName();
			Task task = getDataHandler().getTask( taskName );
			
			getTextView( R.id.TV_task_header ).setText( getString( R.string.task_header ) );
			
			getEditText( R.id.ET_task_name ).setText( task.getName() );
			getEditText( R.id.ET_task_desc ).setText( task.getDesciption() );
			getEditText( R.id.ET_task_date ).setText( BaseActivity.GetDateAsString( task.getDate() ) );
			
			( ( TaskActivity ) getBaseTabActivity() ).mTask = task;
			
			return rootView;
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
			Task task = getDataHandler().getTask( ( ( TaskActivity )getBaseTabActivity()).getTaskName() );
			
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
		
		public StudentListAdapter( Context ctx, List<StudentTask> stdList) {
			super( ctx, R.layout.row_task_stdlist, stdList );
			
			this.students = stdList;
		}
		
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			if ( convertView == null ) {
				convertView = createView( getContext(), parent, position );
			}
			
			Student std = students.get( position ).getStudent();
			
			ViewHolder holder = ( ViewHolder ) convertView.getTag();
			holder.nameTV.setText( std.getFirstName() );
			holder.identTV.setText( std.getIdent() );
			holder.classTV.setText( std.getStudentClass() );
			
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
			View myView = inflater.inflate( R.layout.row_task_stdlist, parent, false );
			ViewHolder holder = new ViewHolder();
			Student student = students.get( position ).getStudent();

			ImageView imgInfoView = (ImageView) myView.findViewById( R.id.info );
			imgInfoView.setTag( student );
			imgInfoView.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {

					Intent intent = new Intent( getContext(), StudentInfoActivity.class );

					Student std = (Student) v.getTag();
					BaseActivity.putIdentExtra( std, intent );

					getContext().startActivity( intent );
				}
			} );

			ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
			imgTaskView.setTag( student );
			imgInfoView.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					Intent intent = new Intent( getContext(), StdInfoActivity.class );
					Student std = (Student) v.getTag();
					BaseActivity.putIdentExtra( std, intent );
					getContext().startActivity( intent );
				}
			} );

			holder.nameTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_name );
			holder.nameTV.setTag( student );

			holder.identTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_ident );
			holder.identTV.setTag( student );
			
			holder.classTV = (TextView) myView.findViewById( R.id.TV_task_stdlist_class );
			holder.classTV.setTag( student );
			
			holder.imgInfoView = imgInfoView;
			holder.imgTaskView = imgTaskView;
			holder.id = position;

			myView.setTag( holder );

			return myView;
		}
		
	}

	static class ViewHolder {
		int id;

		TextView nameTV;
		TextView identTV;
		TextView classTV;
		ImageView imgInfoView;
		ImageView imgTaskView;

	}
}
