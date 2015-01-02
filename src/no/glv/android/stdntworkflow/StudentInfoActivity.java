package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.BaseFragment;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.sql.StudentBean;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentInfoActivity extends BaseActivity {
	
	private static final String STUDENT_REPLACE = "{elev}";

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_info );
		if ( savedInstanceState == null ) {
			getSupportFragmentManager().beginTransaction().add( R.id.container, new PlaceholderFragment( this ) )
					.commit();
		}

		TextView textView = (TextView) findViewById( R.id.TV_info_header );
		Student student = getStudentByIdentExtra();
		textView.setText( student.getFirstName() );
		
		String name = student.getIdent();
		String title = name.replace( STUDENT_REPLACE, name );
		setTitle( title );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_student_info, menu );
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
	public static class PlaceholderFragment extends BaseFragment {

		/**
		 * 
		 */
		public PlaceholderFragment( BaseActivity base ) {
			super( base );
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_student_info, container, false );
 			Student bean = getBaseActivity().getStudentByIdentExtra();
 			
 			EditText editText = ( EditText ) rootView.findViewById( R.id.ET_info_firstName );
 			editText.setText( bean.getFirstName() );
 			
 			editText = ( EditText ) rootView.findViewById( R.id.ET_info_LastName );
 			editText.setText( bean.getLastname() );
 			
 			editText = ( EditText ) rootView.findViewById( R.id.ET_info_ident );
 			editText.setText( bean.getIdent() );
 			
 			Button btn = ( Button ) rootView.findViewById( R.id.BTN_info_update );
 			btn.setTag( bean );
 			btn.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick( View v ) {
					StudentBean bean = ( StudentBean ) v.getTag();
					View rootView = v.getRootView();
					
		 			EditText editText = ( EditText ) rootView.findViewById( R.id.ET_info_firstName );
		 			bean.setFirstName( editText.getText().toString() );
		 			
		 			editText = ( EditText ) rootView.findViewById( R.id.ET_info_LastName );
		 			bean.setLastName( editText.getText().toString() );
		 			
		 			editText = ( EditText ) rootView.findViewById( R.id.ET_info_ident );
		 			String oldIdent = bean.getIdent();
		 			bean.setIdent( editText.getText().toString() );
		 			
		 			DataHandler.GetInstance().updateStudent( bean, oldIdent );
				}
			});

			return rootView;
		}
	}
}
