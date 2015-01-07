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

/**
 * A dialog that will list every known student added to the task. The user may choose to remove certain
 * students from the list, if needed.
 * 
 * @author GleVoll
 *
 */
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
	
	/**
	 * 
	 * @param rootView
	 */
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

		btn = ( Button ) rootView.findViewById( R.id.BTN_newTask_cancelStudents );
		btn.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Fragment fr = AddedStudentsToTaskFragment.this;
				fr.getFragmentManager().beginTransaction().remove( fr ).commit();
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
		adapter.setTask( task );
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
		private Task task;
		
		
		public AddedStudentsAdapter( Context context, int resource, List<Student> objects ) {
			super( context, resource, objects );
			
			this.students = objects;
		}
		
		void setTask( Task task ) {
			this.task = task;
		}
		
		
		/**
		 * 
		 */
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			ViewHolder holder = null;
			
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			if ( convertView == null ) {
				convertView = inflater.inflate( R.layout.row_newtask_students, parent, false );
				holder = new ViewHolder();
				holder.studentIdent = (TextView) convertView.findViewById( R.id.TV_newTask_studentIdent );				
				holder.cBox = ( CheckBox ) convertView.findViewById( R.id.CB_newTask_addStudent );
				
				convertView.setTag( holder );
			}
			
			holder = ( ViewHolder ) convertView.getTag();
			
			Student std = students.get( position ); 
			String tag = std.getIdent();

			holder.studentIdent.setTag( std );
			holder.studentIdent.setText( tag );
			
			holder.cBox.setTag( std );
			holder.cBox.setChecked( true );
			holder.cBox.setOnCheckedChangeListener( this );

			
			return convertView;
		}


		@Override
		public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
			Student std = (Student) buttonView.getTag();
			if ( ! isChecked )
				task.removeStudent( std.getIdent() );
			else
				task.addStudent( std );
		}
	}
	
	static class ViewHolder {
		TextView studentIdent;
		CheckBox cBox;
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
