package no.glv.android.stdntworkflow;

import java.util.ArrayList;

import no.glv.android.stdntworkflow.core.BaseFragment;
import no.glv.android.stdntworkflow.core.Utils;
import no.glv.android.stdntworkflow.intrfc.SubjectType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This will show the installed {@link SubjectType}s, and allow for custom
 * installation of new.
 * 
 * @author glevoll
 *
 */
public class SubjectTypesFragment extends BaseFragment {

	private View rootView;
	private Holder holder;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		rootView = inflater.inflate( R.layout.fr_subject_type, container, false );
		holder = new Holder();

		holder.etName = (EditText) rootView.findViewById( R.id.ET_subjectType_name );
		holder.etDesc = (EditText) rootView.findViewById( R.id.ET_subjectType_desc );
		holder.spSubjType = (Spinner) rootView.findViewById( R.id.SP_subjectType_type );

		Button btnCreate = (Button) rootView.findViewById( R.id.BTN_subjectType_create );
		btnCreate.setTag( holder );
		btnCreate.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Holder h = (Holder) v.getTag();

				SubjectType st = dataHandler.createSubjectType();

				st.setName( h.etName.getText().toString() );
				st.setDescription( h.etDesc.getText().toString() );
				int id = h.spSubjType.getSelectedItemPosition() + 1;
				st.setType( id | SubjectType.TYPE_CUSTOM );

				boolean success = dataHandler.addSubjectType( st );
				String msg = getResources().getString( R.string.subjectType_added );
				if ( !success ) {
					msg = getResources().getString( R.string.subjectType_added_err );
				}
				Toast.makeText( getActivity(), msg, Toast.LENGTH_LONG ).show();
			}
		} );

		// Setup spinners
		Spinner sp = (Spinner) rootView.findViewById( R.id.SP_task_subject );
		Utils.SetupSpinner( sp, new ArrayList<String>( dataHandler.getSubjectNames() ), null, getActivity() );
		sp = (Spinner) rootView.findViewById( R.id.SP_task_type );
		Utils.SetupSpinner( sp, new ArrayList<String>( dataHandler.getTypeNames() ), null, getActivity() );

		return rootView;
	}

	/**
	 * 
	 * @author glevoll
	 *
	 */
	private static class Holder {

		EditText etName;
		EditText etDesc;

		Spinner spSubjType;
	}
}
