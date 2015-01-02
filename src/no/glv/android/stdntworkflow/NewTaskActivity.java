package no.glv.android.stdntworkflow;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import no.glv.android.stdntworkflow.AddedStudentsToTaskFragment.OnStudentsVerifiedListener;
import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class NewTaskActivity extends Activity {

	private static final String TAG = NewTaskActivity.class.getSimpleName();
	
	private Task task;
	
	private NewTaskFragment fragment;
	
	

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_new_task );

		if ( savedInstanceState == null ) {
			fragment = new NewTaskFragment( );
			fragment.setTask( DataHandler.GetInstance().createTask() );
			getFragmentManager().beginTransaction().add( R.id.container, fragment ).commit();
		}		
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.new_task, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();
		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class NewTaskFragment extends Fragment implements OnClickListener, OnDateSetListener, OnStudentsVerifiedListener {

		private View rootView;
		
		private AddClassToNewTaskAdapter adapter;
		
		private Task task;

		
		/**
		 * 
		 * @param task
		 */
		public void setTask( Task task ) {
			this.task = task;
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			rootView = inflater.inflate( R.layout.fragment_new_task, container, false );

			Button btn = (Button) rootView.findViewById( R.id.BTN_newTask_create );
			btn.setOnClickListener( this );
			btn = (Button) rootView.findViewById( R.id.BTN_newTask_date );
			btn.setOnClickListener( this );

			EditText eText = (EditText) rootView.findViewById( R.id.ET_newTask_date );
			eText.setText( BaseActivity.GetDateAsString( Calendar.getInstance().getTime() ) );

			createListView( rootView );			

			return rootView;
		}

		
		/**
		 * 
		 * @param v
		 */
		private void createListView( View v) {
			ListView listView = (ListView) v.findViewById( R.id.LV_newTask_classes );
			List<String> mClasses = DataHandler.GetInstance().getStudentClassNames(); 
			adapter = new AddClassToNewTaskAdapter( getActivity(), R.layout.row_newtask_addclasses, mClasses );
			adapter.setTask( task );
			listView.setAdapter( adapter );
		}
		
		
		@Override
		public void onClick( View v ) {
			int id = v.getId();
			switch ( id ) {
			case R.id.BTN_newTask_create:
				createNewTask( v );
				break;

			case R.id.BTN_newTask_date:
				Log.d( TAG, v.toString() );

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime( date );
				
				int day = cal.get( Calendar.DAY_OF_MONTH );
				int month = cal.get( Calendar.MONTH );
				int year = cal.get( Calendar.YEAR );
				
				DatePickerDialog dpd = new DatePickerDialog( getActivity(), this, year, month, day );

				DatePicker picker = dpd.getDatePicker();
				picker.setSpinnersShown( false );
				picker.setCalendarViewShown( true );

				dpd.show();

			default:
				break;
			}

		}

		/**
		 * 
		 * @param v
		 * @return
		 */
		private boolean createNewTask( View v ) {
			EditText editText = (EditText) rootView.findViewById( R.id.ET_newTask_name );
			String taskName = editText.getText().toString();

			editText = (EditText) rootView.findViewById( R.id.ET_newTask_desc );
			String taskDesc = editText.getText().toString();

			editText = (EditText) rootView.findViewById( R.id.ET_newTask_date );
			String dateStr = editText.getText().toString();
			
			task.setName( taskName );
			task.setDescription( taskDesc );
			task.setDate( BaseActivity.GetDateFromString( dateStr ) );
			
			// Show FragmentDialog to confirm all the students in the task
			AddedStudentsToTaskFragment fragment = new AddedStudentsToTaskFragment();
			fragment.setTask( task );
			fragment.setOnVerifiedListener( this );

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			fragment.show( ft, getClass().getSimpleName() );

			return true;
		}

		@Override
		public void onDateSet( DatePicker picker, int year, int monthOfYear, int dayOfMonth ) {
			EditText eText = (EditText) rootView.findViewById( R.id.ET_newTask_date );
			eText.setText( BaseActivity.GetDateAsString( year, monthOfYear, dayOfMonth ) );
		}

		@Override
		public void onStudentsVerified( Task task ) {
			DataHandler.GetInstance().addTask( task ).commitTasks();
			
			getActivity().finish();
		}
	}
}
