package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler.OnStudentClassChangeListener;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledClassesFragment extends InstalledDataFragment implements OnStudentClassChangeListener {

	public InstalledClassesFragment() {
		super();
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		dataHandler.addOnStudentClassChangeListener( this );

		super.onCreate( savedInstanceState );
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
		TextView textView = inflateTextView( getRowLayoutID() );
		textView.setText( name );
		textView.setTag( String.valueOf( pos ) );

		textView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Intent intent = createIntent( name, getActivity() );
				if ( intent != null )
					startActivity( intent );
			}
		} );

		return textView;
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

}
