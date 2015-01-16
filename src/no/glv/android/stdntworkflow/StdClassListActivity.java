package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentChangedListener;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * This activity will show all the students in a certain class.
 * 
 * The activity should be able to: - Update the name of the class TODO - Delete
 * the class
 * 
 * @author GleVoll
 *
 */
public class StdClassListActivity extends Activity implements OnClickListener, OnStudentChangedListener {

    private static final String TAG = StdClassListActivity.class.getSimpleName();

    private static final String CLASS_REPLACE = "{klasse}";

    /**  */
    private StudentClass stdClass;

    StudentListAdapter adapter;

    /**
	 * 
	 */
    public StdClassListActivity() {
	DataHandler.GetInstance().addOnStudentChangeListener( this );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	setContentView( R.layout.activity_stdclass_list );

	Log.d( TAG, "onCreate()" );

	stdClass = BaseActivity.GetStudentClassExtra( this.getIntent() );

	String title = getResources().getString( R.string.activity_studentList_title );
	title = title.replace( CLASS_REPLACE, stdClass.getName() );
	setTitle( title );

	ListView listView = (ListView) findViewById( R.id.student_listview );
	adapter = new StudentListAdapter( this, stdClass.getStudents() );
	listView.setAdapter( adapter );
    }

    /**
	 * 
	 */
    private void update() {
	adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
	super.onRestart();
	update();
    }

    @Override
    public void onStudentChange( Student std, int mode ) {
	update();
    }

    /**
	 * 
	 */
    public void onClick( View v ) {
	Log.d( TAG, "onClick: " + v );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
	getMenuInflater().inflate( R.menu.menu_stdclass_list, menu );
	return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
	int id = item.getItemId();

	switch ( id ) {
	case R.id.action_settings:
	    break;

	case R.id.menu_stdlist_delete:
	    DataHandler.GetInstance().deleteStudentClass( stdClass.getName() );
	    finish();
	    break;

	case R.id.menu_stdList_sort_firstNameAsc:
	    DataHandler.GetInstance().getSettingsManager().sortByFirstNameAsc( stdClass.getName() );
	    update();
	    return true;

	case R.id.menu_stdList_sort_lastNameAsc:
	    DataHandler.GetInstance().getSettingsManager().sortByLastNameAsc( stdClass.getName() );
	    update();
	    return true;

	}

	return super.onOptionsItemSelected( item );
    }

    /**
     * 
     * @param stdClassName
     * @param ctx
     * @return
     */
    static final Intent CreateActivityIntent( String stdClassName, Context ctx ) {
	Intent intent = new Intent( ctx, StdClassListActivity.class );
	BaseActivity.PutStudentClassExtra( stdClassName, intent );

	return intent;
    }
}
