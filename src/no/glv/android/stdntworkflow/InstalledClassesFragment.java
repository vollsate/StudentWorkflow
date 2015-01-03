package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledClassesFragment extends Fragment implements OnStudentClassChangeListener {

	public static final String EXTRA_TASKNAME = BaseValues.EXTRA_BASEPARAM + "TaskName";

	private List<String> mClasses;
	private boolean needUpdate = false;
	StudentClassListAdapter adapter;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		DataHandler.GetInstance().addOnStudentClassChangeListener( this );
		
		needUpdate = true;
		updateLists();
	}
	
	@Override
	public void onResume() {
		super.onResume();

		updateLists();
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_installedclasses, container, false );

		buildView( rootView );
		return rootView;
	}

	/**
	 * 
	 * @param rootView
	 */
	private void buildView( View rootView ) {
		buildAdapter( rootView );
	}

	/**
	 * 
	 */
	private void updateLists() {
		if ( ! needUpdate ) return;
		
		mClasses = DataHandler.GetInstance().getStudentClassNames();
		
		needUpdate = false;
	}
	
	/**
	 * 
	 * @param rootView
	 */
	private void buildAdapter( View rootView ) {
		ListView listView = (ListView) rootView.findViewById( R.id.LV_installedClasses );
		adapter = new StudentClassListAdapter( getActivity(), mClasses );
		listView.setAdapter( adapter );
	}
	
	@Override
	public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
		needUpdate = true;
		updateLists();
		adapter.notifyDataSetChanged();
	}

	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	//
	// STUDENTCLASSLISTADAPTER
	//
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------

	/**
	 * 
	 * @author GleVoll
	 *
	 */
	static class StudentClassListAdapter extends ArrayAdapter<String> {

		private List<String> mClasses;

		/**
		 * 
		 * @param context
		 * @param resource
		 * @param objects
		 */
		public StudentClassListAdapter( Context context, List<String> objects ) {
			super( context, R.layout.row_classes_list, objects );
			this.mClasses = objects;
		}

		/**
		 * 
		 */
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			if ( convertView == null ) {
				convertView = inflater.inflate( R.layout.row_classes_list, parent, false );
			}

			String className = mClasses.get( position );

			TextView textView = (TextView) convertView.findViewById( R.id.TV_studentClassName );
			textView.setTag( className );
			textView.setText( className );
			textView.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					Intent intent = new Intent( getContext(), StudentClassListActivity.class );
					String studentClass = (String) v.getTag();
					BaseActivity.PutStudentClassExtra( studentClass, intent );
					getContext().startActivity( intent );
				}
			} );

			return convertView;
		}

	}

}
