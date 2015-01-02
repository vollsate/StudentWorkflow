package no.glv.android.stdntworkflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class AddedStudentsToTaskFragment extends DialogFragment {
	
	public static final String EXTRA_TASKNAME = BaseValues.EXTRA_BASEPARAM + "TaskName";
	
	private Task task;
	private OnStudentsVerifiedListener listener;

	public void setTask( Task task ) {
		this.task = task;
	}
	
	public void setOnVerifiedListener( OnStudentsVerifiedListener listener ) {
		this.listener = listener;
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
	}
	
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_students_newtask, container, false );
		
		buildAdapter( rootView );
		buildButton( rootView );
		
		getDialog().setTitle( "Add students to: " + task.getName() );
		
		return rootView;
	}
	
	
	private void buildButton( View rootView ) {
		Button btn = ( Button ) rootView.findViewById( R.id.BTN_newTask_verifyStudents );
		btn.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Fragment fr = AddedStudentsToTaskFragment.this;
				fr.getFragmentManager().beginTransaction().remove( fr ).commit();
				
				listener.onStudentsVerified( task );
			}
		} );
	}
	
	
	/**
	 * 
	 * @param rootView
	 */
	private void buildAdapter(View rootView ) {
		List<Student> students = createStudentList();
		
		ListView listView = ( ListView ) rootView.findViewById( R.id.LV_newTask_addedStudents );
		AddedStudentsAdapter adapter = new AddedStudentsAdapter( getActivity(), R.id.LV_newTask_addedStudents, students );
		listView.setAdapter( adapter );
	}

	
	/**
	 * 
	 * @return
	 */
	private List<Student> createStudentList( ) {
		List<String> mClasses = task.getClasses();
		List<Student> students = new ArrayList<Student>();
		
		Iterator<String> it = mClasses.iterator();
		while ( it.hasNext() ) {
			String className = it.next();
			StudentClass stdClass = DataHandler.GetInstance().getStudentClass( className );
			students.addAll( stdClass.getStudents() );
		}
		
		return students;
	}
	
	
	
	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static class AddedStudentsAdapter extends ArrayAdapter<Student> implements OnCheckedChangeListener {
		
		private List<Student> students;
		
		public AddedStudentsAdapter( Context context, int resource, List<Student> objects ) {
			super( context, resource, objects );
			
			this.students = objects;
		}
		
		
		/**
		 * 
		 */
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			if ( convertView == null ) {
				convertView = inflater.inflate( R.layout.row_newtask_addclasses, parent, false );
			}
			Student std = students.get( position ); 
			String tag = std.getIdent();

			TextView textView = (TextView) convertView.findViewById( R.id.TV_newTask_studentIdent );
			textView.setTag( std.getFirstName() );
			textView.setText( tag );
			
			CheckBox cBox = ( CheckBox ) convertView.findViewById( R.id.CB_newTask_addStudent );
			cBox.setTag( tag );
			cBox.setChecked( true );
			cBox.setOnCheckedChangeListener( this );

			
			return convertView;
		}


		@Override
		public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
			String stdName = buttonView.getTag().toString();
			students.remove( stdName );
		}
	}
	
	
	
	
	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static interface OnStudentsVerifiedListener {
		
		public void onStudentsVerified(Task task);
	}

}
