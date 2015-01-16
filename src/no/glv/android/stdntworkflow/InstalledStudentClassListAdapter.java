package no.glv.android.stdntworkflow;

import java.util.List;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class InstalledStudentClassListAdapter extends ArrayAdapter<String> {

    /**  */
    private static final String TAG = InstalledStudentClassListAdapter.class.getSimpleName();

    /**
     * 
     * @param context
     * @param resource
     */
    public InstalledStudentClassListAdapter( Context context ) {
	this( context, R.layout.row_installed_class, DataHandler.GetInstance().getStudentClassNames() );
    }

    /**
     * 
     * @param context
     * @param resource
     * @param objects
     */
    private InstalledStudentClassListAdapter( Context context, int resource, List<String> objects ) {
	super( context, resource, objects );
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
	if ( convertView == null ) {
	    convertView = createView( getContext(), parent, position );
	}

	StudentClassHolder holder = (StudentClassHolder) convertView.getTag();
	holder.textView.setText( getItem( position ).toString() );
	holder.ID = position;

	return convertView;
    }

    /**
     * 
     * @param context
     * @param parent
     * @param position
     * @return
     */
    private View createView( Context context, ViewGroup parent, int position ) {
	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	View myView = inflater.inflate( R.layout.row_installed_class, parent, false );
	StudentClassHolder holder = new StudentClassHolder();

	TextView textView = (TextView) myView.findViewById( R.id.TV_studentClassName );

	// Setting the StudentClassname as a TAG for this view
	textView.setTag( getItem( position ) );
	textView.setOnClickListener( new View.OnClickListener() {

	    @Override
	    public void onClick( View v ) {
		Log.d( TAG, "" + v.getTag() );

		Intent intent = new Intent( getContext(), StdClassListActivity.class );
		String studentClass = (String) v.getTag();
		BaseActivity.PutStudentClassExtra( studentClass, intent );
		InstalledStudentClassListAdapter.this.getContext().startActivity( intent );
	    }
	} );

	holder.textView = textView;
	holder.ID = position;
	myView.setTag( holder );

	return myView;
    }

    /**
     * 
     * @author GleVoll
     *
     */
    static class StudentClassHolder {

	TextView textView;
	int ID;

    }
}
