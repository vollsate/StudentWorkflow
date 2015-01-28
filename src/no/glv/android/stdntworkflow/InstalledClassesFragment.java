package no.glv.android.stdntworkflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledClassesFragment extends InstalledDataFragment implements OnStudentClassChangeListener {
	
	/**  */
	public static final String INST_STATE_CLASS_NAMES = BaseValues.EXTRA_BASEPARAM + "names";

	private ArrayList<String> classNames;
	
	private ClassViewConfig config;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		dataHandler.addOnStudentClassChangeListener( this );

		super.onCreate( savedInstanceState );
		
		if ( savedInstanceState != null ) {
			classNames = savedInstanceState.getStringArrayList( INST_STATE_CLASS_NAMES );
			config = (ClassViewConfig) savedInstanceState.getSerializable( PARAM_CONFIG );
		}
		else {
			classNames = dataHandler.getStudentClassNames();
			config = (ClassViewConfig) getArguments().getSerializable( PARAM_CONFIG );
		}
	}
	
	@Override
	public void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );
		outState.putStringArrayList( INST_STATE_CLASS_NAMES, classNames );
		outState.putSerializable( PARAM_CONFIG, config );
	}

	@Override
	protected void doCreateView( ViewGroup rootView ) {
		super.doCreateView( rootView );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int getViewGruopLayoutID() {
		return R.layout.fr_installedclasses_new;
	}

	@Override
	public List<String> getNames() {
		return classNames;
	}

	@Override
	public int getRowLayoutID() {
		return R.layout.row_installed_class;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected View buildRow( final String name, int pos ) {
		ViewGroup vg = (ViewGroup) inflateView( getRowLayoutID() );
		StudentClass stdClass = dataHandler.getStudentClass( name );
		
		LinearLayout ll = (LinearLayout) vg.findViewById( R.id.LL_classList_rowData );
		ll.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Intent intent = createIntent( name, getActivity() );
				if ( intent != null )
					startActivity( intent );
			}
		} );

		TextView textView = (TextView) vg.findViewById( R.id.TV_classList_name );
		textView.setText( name );
		textView.setTag( String.valueOf( pos ) );

		TextView tvStdCount = (TextView) vg.findViewById( R.id.TV_classList_counter );
		if ( config.showStudentCount ) {
			tvStdCount.setText( String.valueOf( stdClass.getSize() ) );
			tvStdCount.setTag( String.valueOf( pos ) );
		}
		else {
			tvStdCount.setVisibility( View.GONE );
		}

		return vg;
	}

	@Override
	public Intent createIntent( String name, Context context ) {
		return StdClassListActivity.CreateActivityIntent( name, context );
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.menu_fr_installedclasses, menu );
	}

	@Override
	public void onStudentClassUpdate( StudentClass stdClass, int mode ) {
		onDataChange( mode );
	}

	/**
	 * 
	 * @param manager
	 * @param replace
	 */
	public static void StartFragment( FragmentManager manager, ClassViewConfig config ) {
		InstalledClassesFragment classesFragment = new InstalledClassesFragment();
		Bundle args = new Bundle();
		args.putInt( InstalledClassesFragment.EXTRA_SHOWCOUNT, DataHandler.GetInstance().getSettingsManager()
				.getShowCount() );
		args.putSerializable( PARAM_CONFIG, config );
		classesFragment.setArguments( args );

		FragmentTransaction tr = manager.beginTransaction();
		tr.replace( R.id.FR_installedClasses_container, classesFragment ).commit();
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	//
	// Configuration class
	//
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * The configuration class for
	 * 
	 * @author glevoll
	 *
	 */

	public static class ClassViewConfig implements Serializable {

		/** InstalledTasksFragment.java */
		private static final long serialVersionUID = 1L;

		public boolean showStudentCount;

		public int showCount;
	}
}
