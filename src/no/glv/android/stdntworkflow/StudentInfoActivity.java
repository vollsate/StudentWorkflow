package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.Student;
import no.glv.android.stdntworkflow.core.StudentClass;
import no.glv.android.stdntworkflow.core.StudentClassHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StudentInfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_info );
		if ( savedInstanceState == null ) {
			getSupportFragmentManager().beginTransaction().add( R.id.container, new PlaceholderFragment() ).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_student_info, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		/**
		 * 
		 */
		public PlaceholderFragment() {
			super();
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_student_info, container, false );

			TextView textView = (TextView) rootView.findViewById( R.id.textview_StudentName );
			Bundle bundle = getActivity().getIntent().getExtras();

			String sName = bundle.getString( Student.EXTRA_STUDENTNAME );
			String sClass = bundle.getString( Student.EXTRA_STUDENTCLASS );

			StudentClass stdClass = StudentClassHandler.GetInstance().getStudentClass( sClass );
			Student bean = stdClass.getStudentByFirstName( sName );

			textView.setText( bean.getFirstName() + " " + bean.getLastname() );

			return rootView;
		}
	}
}
