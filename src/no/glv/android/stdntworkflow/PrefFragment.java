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

import java.util.TreeMap;

import no.glv.android.stdntworkflow.core.DataHandler;

public class PrefFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private TreeMap<Preference, CharSequence> prefSummary;
	
	public PrefFragment() {
		prefSummary = new TreeMap<Preference, CharSequence>();
	}

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
		
		if ( key.contains( "task" ) ) {
			DataHandler.GetInstance().notifyTaskSettingsChange();
		}
		
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

		updatePrefSummary( p );
	}

	/**
	 * 
	 * @param p
	 */
	private void updatePrefSummary( Preference p ) {
		CharSequence summary = prefSummary.get( p );

		if ( ! prefSummary.containsKey( p ) ) {
			prefSummary.put( p, p.getSummary() );
			summary = p.getSummary();
		}
		
		if ( p instanceof ListPreference ) {
			ListPreference listPref = (ListPreference) p;
			String str = "[" + listPref.getEntry() + "]\n\n" + summary;
			p.setSummary( str );
		}
		if ( p instanceof EditTextPreference ) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			if ( p.getTitle().toString().contains( "assword" ) ) {
				p.setSummary( "******" );
			}
			else {
				String str = "[" + editTextPref.getText() + "]\n\n" + summary;
				p.setSummary( str );
			}
		}
		if ( p instanceof MultiSelectListPreference ) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary( editTextPref.getText() );
		}
	}

}
