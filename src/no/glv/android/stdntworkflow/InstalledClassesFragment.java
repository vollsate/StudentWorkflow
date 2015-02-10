package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataComparator;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Fragment;
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
 * This will list any installed class in the system. It may be installed as a
 * fragment in any container with the ID: <tt>fr.installedClasses</tt>
 * 
 * <p>
 * The fragment task a {@link DataConfig} parameter to be used to control the
 * layout and view of the class. The following parameters may be used:
 * <blockquote>
 * <ul>
 * <li>showCount - The number of classes to show.
 * <li>sortBy - The {@link DataComparator} sortBy int ID.
 * <li>showDesc - A boolean telling weather or not to display the number of
 * students in the class
 * </ul>
 * </blockquote>
 * 
 * 
 * @author GleVoll
 *
 */
public class InstalledClassesFragment extends InstalledDataFragment<String> implements OnStudentClassChangeListener {

	/**  */
	public static final String INST_STATE_CLASS_NAMES = BaseValues.EXTRA_BASEPARAM + "names";

	private ClassViewConfig config;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		dataHandler.addOnStudentClassChangeListener( this );
		super.onCreate( savedInstanceState );

		config = (ClassViewConfig) getArguments().getSerializable( PARAM_CONFIG );
	}

	@Override
	protected DataConfig getConfig() {
		return config;
	}

	@Override
	public void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );
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
		return R.layout.fr_installedclasses;
	}

	@Override
	public List<String> getNames() {
		return dataHandler.getStudentClassNames();
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

		TextView tvStds = (TextView) vg.findViewById( R.id.TV_classList_desc );
		TextView tvStdCount = (TextView) vg.findViewById( R.id.TV_classList_counter );
		if ( config.showStudentCount ) {
			tvStdCount.setText( String.valueOf( stdClass.getSize() ) );
			tvStdCount.setTag( String.valueOf( pos ) );
		}
		else {
			tvStdCount.setVisibility( View.GONE );
			tvStds.setVisibility( View.GONE );
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
	 * @param config
	 */
	public static void StartFragment( FragmentManager manager, ClassViewConfig config ) {
		Bundle args = new Bundle();

		StartFragment( manager, config, args, new InstalledClassesFragment(), R.id.FR_installedClasses_container );
	}

	/**
	 * 
	 * @param manager
	 * @param config
	 * @param args
	 * @param fragment
	 * @param containerID
	 */
	public static void StartFragment( FragmentManager manager, ClassViewConfig config, Bundle args, Fragment fragment,
			int containerID ) {
		int showCount = config.showCount;
		if ( showCount < 0 ) {
			showCount = DataHandler.GetInstance().getSettingsManager()
					.getShowCount();
		}

		args.putSerializable( PARAM_CONFIG, config );
		fragment.setArguments( args );

		FragmentTransaction tr = manager.beginTransaction();
		tr.replace( containerID, fragment ).commit();
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

	public static class ClassViewConfig extends DataConfig {

		/** InstalledTasksFragment.java */
		private static final long serialVersionUID = 1L;

		public boolean showStudentCount;
	}
}
