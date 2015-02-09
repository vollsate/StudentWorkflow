package no.glv.android.stdntworkflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import no.glv.android.stdntworkflow.core.BaseFragment;
import no.glv.android.stdntworkflow.intrfc.SubjectType;

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
				switch ( id ) {
					case SubjectType.TYPE_SUBJECT:						
						break;

					case SubjectType.TYPE_TYPE:
						break;
				}
				
				st.setType( id );
			}
		} );
		
		
		
		return rootView;
	}
	
	private static class Holder {
		
		EditText etName;
		EditText etDesc;
		
		Spinner spSubjType;
	}
}
