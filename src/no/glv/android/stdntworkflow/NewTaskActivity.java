package no.glv.android.stdntworkflow;

import java.util.Calendar;
import java.util.Date;

import no.glv.android.stdntworkflow.AddedStudentsToTaskFragment.OnStudentsVerifiedListener;
import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DatePickerDialogHelper;
import no.glv.android.stdntworkflow.core.ViewGroupAdapter;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Creates a new Task.
 * 
 * Uses the {@link NewTaskFragment} to collect the data.
 * 
 * @author GleVoll
 *
 */
public class NewTaskActivity extends Activity implements OnClickListener, OnDateSetListener, OnStudentsVerifiedListener {

    private static final String TAG = NewTaskActivity.class.getSimpleName();
    private Task task;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	setContentView( R.layout.activity_new_task );
	if ( savedInstanceState != null ) task = (Task) savedInstanceState.getSerializable( Task.EXTRA_TASKNAME );
	else
	    task = DataHandler.GetInstance().createTask();

	Button btn = (Button) findViewById( R.id.BTN_newTask_create );
	btn.setOnClickListener( this );
	btn = (Button) findViewById( R.id.BTN_newTask_date );
	btn.setOnClickListener( this );

	EditText eText = (EditText) findViewById( R.id.ET_newTask_date );
	eText.setText( BaseActivity.GetDateAsString( Calendar.getInstance().getTime() ) );

	AddClassToTaskFragment fragment = new AddClassToTaskFragment();
	Bundle args = new Bundle();
	args.putSerializable( Task.EXTRA_TASKNAME, task );
	ViewGroupAdapter.beginFragmentTransaction( getFragmentManager(), fragment, args, R.id.LL_newTask_classes );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
	super.onSaveInstanceState( outState );

	outState.putString( Task.EXTRA_TASKNAME, task.getName() );
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

    @Override
    public void onClick( View v ) {
	int id = v.getId();

	switch ( id ) {
	case R.id.BTN_newTask_create:
	    createNewTask( v );
	    break;

	case R.id.BTN_newTask_date:
	    Log.d( TAG, v.toString() );

	    DatePickerDialogHelper.OpenDatePickerDialog( new Date(), this, this, false, true );
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
	EditText editText = (EditText) findViewById( R.id.ET_newTask_name );
	String taskName = editText.getText().toString();

	editText = (EditText) findViewById( R.id.ET_newTask_desc );
	String taskDesc = editText.getText().toString();

	editText = (EditText) findViewById( R.id.ET_newTask_date );
	String dateStr = editText.getText().toString();

	task.setName( taskName );
	task.setDescription( taskDesc );
	task.setDate( BaseActivity.GetDateFromString( dateStr ) );

	// Show FragmentDialog to confirm all the students in the task
	AddedStudentsToTaskFragment.StartFragment( task, this, getFragmentManager() );
	return true;
    }

    @Override
    public void onDateSet( DatePicker picker, int year, int monthOfYear, int dayOfMonth ) {
	EditText eText = (EditText) findViewById( R.id.ET_newTask_date );
	eText.setText( BaseActivity.GetDateAsString( year, monthOfYear, dayOfMonth ) );
    }

    @Override
    public void onStudentsVerified( Task task ) {
	DataHandler.GetInstance().addTask( task );

	String msg = getResources().getString( R.string.newTask_added_toast );
	msg = msg.replace( "{task}", task.getName() );
	Toast.makeText( this, msg, Toast.LENGTH_LONG ).show();

	finish();
    }

    @Override
    public void addStudent( Student std ) {
	task.addStudent( std );
    }

    @Override
    public void removeStudent( Student std ) {
	task.removeStudent( std.getIdent() );
    }

}
