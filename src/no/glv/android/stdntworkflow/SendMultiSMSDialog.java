package no.glv.android.stdntworkflow;

import java.util.LinkedList;
import java.util.List;

import no.glv.android.stdntworkflow.core.DialogFragmentBase;
import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentClass;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SendMultiSMSDialog extends DialogFragmentBase {

	OnVerifySendSMSListener listener;
	StudentClass stdClass;

	public SendMultiSMSDialog() {
	}

	@Override
	protected void buildView( View rootView ) {
		buildButton( rootView );
	}

	/**
	 * 
	 * @param rootView
	 */
	private void buildButton( View rootView ) {
		final Fragment fr = this;

		Button btn = (Button) rootView.findViewById( R.id.BTN_stdlist_sendSMS );
		btn.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View view ) {
				boolean p1, p2 = false;

				View v = view.getRootView();

				CheckBox cb = (CheckBox) v.findViewById( R.id.checkBox1 );
				p1 = cb.isChecked();
				cb = (CheckBox) v.findViewById( R.id.checkBox2 );
				p2 = cb.isChecked();

				List<Phone> pList = new LinkedList<Phone>();

				for ( Student s : stdClass.getStudents() ) {
					int i = 0;
					if ( s.getParents() == null || s.getParents().size() == i )
						continue;

					if ( p1 && s.getParents().size() >= ++i ) {
						Parent par = s.getParents().get( i - 1 );
						long num = par.getPhoneNumber( Phone.MOBIL );
						if ( num != 0 ) {
							pList.add( par.getPhone( Phone.MOBIL ) );
						}
					}

					if ( p2 && s.getParents().size() >= ++i ) {
						Parent par = s.getParents().get( i - 1 );
						long num = par.getPhoneNumber( Phone.MOBIL );
						if ( num != 0 ) {
							pList.add( par.getPhone( Phone.MOBIL ) );
						}
					}
				}

				EditText et = (EditText) v.findViewById( R.id.ET_stdList_sms );
				listener.verifySendSMS( pList, et.getText().toString() );

				fr.getFragmentManager().beginTransaction().remove( fr ).commit();
			}
		} );

		btn = (Button) rootView.findViewById( R.id.BTN_stdList_cancelSMS );
		btn.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				fr.getFragmentManager().beginTransaction().remove( fr ).commit();
			}
		} );
	}

	@Override
	protected int getRootViewID() {
		return R.layout.dialog_sms;
	}

	@Override
	protected String getTitle() {
		return "Send SMS til foresatt(e)";
	}

	public static SendMultiSMSDialog StartFragment( StudentClass stdClass, OnVerifySendSMSListener listener,
			FragmentManager manager ) {
		SendMultiSMSDialog fragment = new SendMultiSMSDialog();
		fragment.listener = listener;
		fragment.stdClass = stdClass;

		FragmentTransaction ft = manager.beginTransaction();
		fragment.show( ft, fragment.getClass().getSimpleName() );

		return fragment;
	}

	public static interface OnVerifySendSMSListener {
		public void verifySendSMS( List<Phone> p, String msg );
	}

}
