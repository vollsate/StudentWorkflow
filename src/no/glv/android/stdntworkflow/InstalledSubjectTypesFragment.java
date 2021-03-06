package no.glv.android.stdntworkflow;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.Utils;
import no.glv.android.stdntworkflow.intrfc.SubjectType;

public class InstalledSubjectTypesFragment extends InstalledDataFragment<SubjectType> {

    private DataHandler dataHandler;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        dataHandler = DataHandler.GetInstance();
    }

    @Override
    public int getViewGroupLayoutID() {
        return R.layout.fr_installed_subjecttypes;
    }

    @Override
    public List<SubjectType> getNames() {
        LinkedList<SubjectType> list = new LinkedList<SubjectType>();

        Iterator<SubjectType> sts = dataHandler.getSubjects().iterator();
        while ( sts.hasNext() ) {
            SubjectType st = sts.next();
            if ( ( st.getType() & SubjectType.TYPE_CUSTOM ) == SubjectType.TYPE_CUSTOM )
                list.add( st );
        }

        sts = dataHandler.getTypes().iterator();
        while ( sts.hasNext() ) {
            SubjectType st = sts.next();
            if ( ( st.getType() & SubjectType.TYPE_CUSTOM ) == SubjectType.TYPE_CUSTOM )
                list.add( st );
        }

        return list;
    }

    @Override
    protected DataConfig getConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected View buildRow( SubjectType st, int pos ) {
        ViewGroup viewGroup = inflateViewGroup( getRowLayoutID() );

        TextView tvName = ( TextView ) viewGroup.findViewById( R.id.TV_subtype_name );
        TextView tvDesc = ( TextView ) viewGroup.findViewById( R.id.TV_subtype_desc );
        TextView tvtype = ( TextView ) viewGroup.findViewById( R.id.TV_subtype_type );

        tvName.setText( st.getName() );
        tvDesc.setText( st.getDescription() != null ? " " : st.getDescription() );
        tvtype.setText( Utils.GetSubjectType( st ) );

        ImageView ivDelete = ( ImageView ) viewGroup.findViewById( R.id.IV_subtype_delete );
        ivDelete.setTag( st );
        ivDelete.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                final SubjectType subtype = ( SubjectType ) v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

                builder.setTitle( getResources().getString( R.string.task_delete_title ) );
                builder.setMessage( subtype.getName() );

                builder.setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        // Delete and finish
                        dataHandler.deleteSubjectType( subtype );
                        //notifyDataSetChanged();

                        Utils.FragmentDetachAttach( InstalledSubjectTypesFragment.this);
                    }
                } );

                builder.setNegativeButton( R.string.cancel, null );

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        } );

        return viewGroup;
    }

    @Override
    public int getRowLayoutID() {
        return R.layout.row_subjecttype;
    }

    @Override
    public Intent createIntent( SubjectType name, Context context ) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param manager
     * @param containerID
     */
    public static void StartFragment( FragmentManager manager, int containerID ) {
        InstalledSubjectTypesFragment fragment = new InstalledSubjectTypesFragment();

        FragmentTransaction tr = manager.beginTransaction();
        tr.replace( containerID, fragment ).commit();

    }

}
