package no.glv.android.stdntworkflow;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

public class PrefFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		addPreferencesFromResource( R.xml.pref_main );
		PreferenceManager.setDefaultValues( getActivity(), R.xml.pref_main, false );
		initSummary( getPreferenceScreen() );

	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener( this );
	}

	@Override
	public void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener( this );
	}

	/**
	 * 
	 */
	public void onSharedPreferenceChanged( SharedPreferences sharedPreferences,
			String key ) {
		updatePrefSummary( findPreference( key ) );
	}

	/**
	 * 
	 * @param p
	 */
	private void initSummary( Preference p ) {
		if ( p instanceof PreferenceGroup ) {
			PreferenceGroup pGrp = (PreferenceGroup) p;
			for ( int i = 0; i < pGrp.getPreferenceCount(); i++ ) {
				initSummary( pGrp.getPreference( i ) );
			}
		}
		else {
			updatePrefSummary( p );
		}
	}

	/**
	 * 
	 * @param p
	 */
	private void updatePrefSummary( Preference p ) {
		if ( p instanceof ListPreference ) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary( listPref.getEntry() );
		}
		if ( p instanceof EditTextPreference ) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			if ( p.getTitle().toString().contains( "assword" ) ) {
				p.setSummary( "******" );
			}
			else {
				p.setSummary( editTextPref.getText() );
			}
		}
		if ( p instanceof MultiSelectListPreference ) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary( editTextPref.getText() );
		}
	}

}
