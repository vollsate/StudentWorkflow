package no.glv.android.stdntworkflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.ExpandableListViewBase;
import no.glv.android.stdntworkflow.core.SettingsManager;
import no.glv.android.stdntworkflow.core.Utils;
import no.glv.android.stdntworkflow.intrfc.Student;

/**
 * This Adapter will list all the student in a given StudentClass. This class
 * will load a XML layout row: row_student_list.
 * <p>
 * This list MUST display the first name of every student, and the
 *
 * @author GleVoll
 */
@SuppressWarnings("deprecation")
public class StudentListAdapter extends ExpandableListViewBase<Student> {

    /**  */
    @SuppressWarnings("unused")
    private static final String TAG = StudentListAdapter.class.getSimpleName();

    private SettingsManager mSettingsManager;

    /**
     * A-F. Not used or stored anywhere as of yet.
     **/
    private String[] baseClassNames;

    /**
     * @param context
     * @param objects
     */
    public StudentListAdapter( Context context, List<Student> objects ) {
        super( context, objects );

        mSettingsManager = DataHandler.GetInstance().getSettingsManager();
        baseClassNames = context.getResources().getStringArray( R.array.stdList_classes );
    }

    /**
     *
     */
    public View getView( int position, View convertView, ViewGroup parent, boolean isExpanded ) {
        Student student = getItem( position );

        if ( convertView == null )
            convertView = createView( parent, student, position, isExpanded );

        if ( position % 2 == 0 )
            convertView.setBackgroundColor( getContext().getResources().getColor(
                    R.color.task_stdlist_dark ) );
        else
            convertView.setBackgroundColor( getContext().getResources().getColor( R.color.task_stdlist_light ) );

        ViewHolder holder = ( ViewHolder ) convertView.getTag();
        holder.imgTaskView.setTag( student );
        holder.id = position;

        if ( mSettingsManager.isShowFullname() )
            holder.textView.setText( student.getFirstName() + " "
                    + student.getLastName() );
        else
            holder.textView.setText( student.getFirstName() );

        holder.identText.setText( student.getIdent() );
        holder.birthText.setText( Utils.GetDateAsString( student.getBirth() ) );

        return convertView;
    }

    /**
     * @param parent
     * @param student
     * @param groupPos
     * @param isExpanded
     * @return
     */
    private View createView( final ViewGroup parent, final Student student, final int groupPos, final boolean isExpanded ) {
        LayoutInflater inflater = ( LayoutInflater ) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myView = inflater.inflate( R.layout.row_stdclass_list, parent, false );

        ViewHolder holder = new ViewHolder();

        ImageView imgTaskView = ( ImageView ) myView.findViewById( R.id.task );
        imgTaskView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                Toast.makeText( getContext(), "Will implement individual StudentTask soon..", Toast.LENGTH_LONG )
                        .show();
            }
        } );

        TextView textView = ( TextView ) myView.findViewById( R.id.TV_stdlist_name );
        textView.setTag( student );
        holder.textView = textView;

        textView = ( TextView ) myView.findViewById( R.id.TV_stdlist_ident );
        holder.identText = textView;

        textView = ( TextView ) myView.findViewById( R.id.TV_stdlist_birth );
        holder.birthText = textView;

        holder.imgTaskView = imgTaskView;

        myView.setTag( holder );

        return myView;
    }

    static class ViewHolder {
        int id;

        TextView textView;
        TextView identText;
        TextView birthText;
        ImageView imgTaskView;

    }

    @Override
    public View getGroupView( final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent ) {
        View view = getView( groupPosition, convertView, parent, isExpanded );

        RelativeLayout ll = ( RelativeLayout ) view.findViewById( R.id.LL_stdList_container );
        ll.setOnLongClickListener( new OnLongClickListener() {

            @Override
            public boolean onLongClick( View v ) {
                Student std = getItem( groupPosition );
                StdInfoActivity.StartActivity( getContext(), std );

                return true;
            }
        } );

        ll.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                if ( !isExpanded )
                    ( ( ExpandableListView ) parent ).expandGroup( groupPosition );
                else
                    ( ( ExpandableListView ) parent ).collapseGroup( groupPosition );
            }
        } );

        return view;
    }

    /**
     * @param parent
     * @return
     */
    private View createChildView( ViewGroup parent ) {
        LayoutInflater inflater = ( LayoutInflater ) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View convertView = inflater.inflate( R.layout.row_stdlist_stditem, parent, false );

        StudentItemHolder holder = new StudentItemHolder();
        convertView.setTag( holder );

        return convertView;
    }


    @Override
    public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView,
                              ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = createChildView( parent );
        }

        final Student st = getItem( groupPosition );
        final StudentItemHolder holder = ( StudentItemHolder ) convertView.getTag();
        holder.spClass = ( Spinner ) convertView.findViewById( R.id.SP_stdList_stditem_classes );
        holder.st = st;

        if ( st.getGrade() != null && st.getGrade().length() > 0 ) {
            int sel = ( int ) st.getGrade().charAt( 0 );
            holder.spClass.setSelection( sel - ( int ) 'A' );
        } else {
            holder.spClass.setSelection( baseClassNames.length - 1 );
        }

        ImageView ivSave = ( ImageView ) convertView.findViewById( R.id.IV_stdList_stditem_save );
        ivSave.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                String g = holder.spClass.getSelectedItem().toString();
                holder.st.setGrade( g );

                DataHandler.GetInstance().updateStudent( st, null );
                Toast.makeText( getContext(), "Saved", Toast.LENGTH_SHORT ).show();
            }
        } );

        holder.spClass.setOnItemSelectedListener( new OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                String grade = holder.spClass.getSelectedItem().toString();
                Student student = holder.st;
                if ( student.getGrade() == null ) {
                    student.setGrade( grade );
                } else if ( position > 0 && !st.getGrade().equals( grade ) ) {
                    st.setGrade( grade );
                    DataHandler.GetInstance().updateStudent( st, null );
                }
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent ) {
            }

        } );

        return convertView;
    }

    /**
     * @author glevoll
     */
    static class StudentItemHolder {

        Spinner spClass;
        Student st;
    }

}
