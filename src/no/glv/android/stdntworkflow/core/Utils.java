package no.glv.android.stdntworkflow.core;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.glv.android.stdntworkflow.R;
import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Phone;
import no.glv.android.stdntworkflow.intrfc.SubjectType;

public class Utils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN, Locale.getDefault() );

    /**
     * @param spinner  The {@link Spinner} to setup
     * @param data     The data used to populate the spinner
     * @param selected The default selected item
     * @param ctx      The context to use for the adapter.
     */
    public static void SetupSpinner( Spinner spinner, List<String> data, String selected, Context ctx ) {
        // Set the proper SubjectTypes to the spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( ctx, android.R.layout.simple_spinner_dropdown_item,
                data );
        spinner.setAdapter( adapter );
        if ( selected == null )
            spinner.setSelection( 0 );
        else
            spinner.setSelection( data.indexOf( selected ) );
    }

    /**
     * @param mails
     * @return
     */
    public static Intent CreateMailIntent( String[] mails, Context ctx ) {
        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.setType( "message/rfc822" );
        intent.putExtra( Intent.EXTRA_EMAIL, mails );
        intent.putExtra( Intent.EXTRA_SUBJECT, ctx.getResources().getString( R.string.stdlist_mail_subject ) );
        intent.putExtra( Intent.EXTRA_TEXT, ctx.getResources().getString( R.string.stdlist_mail_body ) );

        return intent;
    }

    /**
     * @param p
     * @return
     */
    public static Intent CreateCallIntent( Phone p ) {
        String tel = "tel:" + p.getNumber();

        Intent intent = new Intent( Intent.ACTION_CALL );
        intent.setData( Uri.parse( tel ) );

        return intent;
    }

    /**
     * @return
     */
    public static String GetDateAsString() {
        return GetDateAsString( new Date() );
    }

    /**
     * @param date
     * @return
     */
    public static String GetDateAsString( Date date ) {
        return sdf.format( date );
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String GetDateAsString( int year, int month, int day ) {
        Calendar cal = Calendar.getInstance();
        cal.set( year, month, day );

        return sdf.format( cal.getTime() );
    }

    /**
     * @param date
     * @return The Date instance or null if some error occurs.
     */
    public static Date GetDateFromString( String date ) {
        try {
            return sdf.parse( date );
        } catch ( ParseException e ) {
            // TODO: handle exception
            return null;
        }
    }

    /**
     *
     * @param fragment
     */
    public static void FragmentDetachAttach( Fragment fragment ) {
        fragment.getActivity().getFragmentManager().beginTransaction().
                detach( fragment ).
                attach( fragment ).
                commit();
    }

    /**
     *
     * @param st
     * @return
     */
    public static String GetSubjectType( SubjectType st ) {
        int type = st.getType();
        if ( ( type & SubjectType.TYPE_THEME ) == SubjectType.TYPE_THEME ) return "TEMA";

        return "FAG";
    }

}
