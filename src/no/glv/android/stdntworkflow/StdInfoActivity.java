package no.glv.android.stdntworkflow;

import java.util.Locale;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.sql.StudentBean;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StdInfoActivity extends Activity implements ActionBar.TabListener {

	private static final String TAG = StdInfoActivity.class.getSimpleName();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Student bean;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_std_info );

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		Log.d( TAG, "Actionbar: " + actionBar );
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

		bean = BaseActivity.GetStudentByIdentExtra( getIntent() );
		Log.d( TAG, "Student: " + bean );

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter( getFragmentManager() );
		mSectionsPagerAdapter.setStudent( bean );

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById( R.id.VP_stdInfo_pager );
		mViewPager.setAdapter( mSectionsPagerAdapter );

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected( int position ) {
				actionBar.setSelectedNavigationItem( position );
			}
		} );

		// For each of the sections in the app, add a tab to the action bar.
		for ( int i = 0; i < mSectionsPagerAdapter.getCount(); i++ ) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab( actionBar.newTab().setText( mSectionsPagerAdapter.getPageTitle( i ) )
					.setTabListener( this ) );
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.task, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	@Override
	public void onTabSelected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem( tab.getPosition() );
	}

	@Override
	public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
	}

	@Override
	public void onTabReselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment[] fragments = new Fragment[3];
		Student bean;

		public SectionsPagerAdapter( FragmentManager fm ) {
			super( fm );
		}

		public void setStudent( Student std ) {
			bean = std;
		}

		@Override
		public Fragment getItem( int position ) {
			Fragment fr = fragments[position];

			switch ( position ) {
			case 0:
				if ( fr == null ) {
					fr = new StdInfoFragment(  ).setStudent( bean );;
				}
				break;

			case 1:
				if ( fr == null ) {
					fr = new StdParent1Fragment(  ).setStudent( bean );
				}
				break;

			case 2:
				if ( fr == null ) {
					fr = new StdParent2Fragment( ).setStudent( bean );
				}
				break;

			default:
				break;
			}

			fragments[position] = fr;
			return fr;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle( int position ) {
			Locale l = Locale.getDefault();
			switch ( position ) {
			case 0:
				return getString( R.string.std_info_title ).toUpperCase( l );
			case 1:
				return getString( R.string.std_parent1_title ).toUpperCase( l );
			case 2:
				return getString( R.string.std_parent2_title ).toUpperCase( l );
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class StdParent1Fragment extends Fragment {

		private Student bean;
		
		public StdParent1Fragment setStudent( Student bean ) {
			this.bean = bean;
			return this;
		}



		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_std_parent1, container, false );
			Parent[] parents = bean.getParents();
			Parent parent = parents[0];

			EditText editText = (EditText) rootView.findViewById( R.id.ET_info_p1Name );
			editText.setText( parent.getFirstName() + " " + parent.getLastName() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_p1Mail );
			editText.setText( parent.getMail() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_p1Phone );
			editText.setText( parent.getPhoneNumbers().toString() );

			return rootView;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class StdParent2Fragment extends Fragment {

		private Student bean;
		
		public StdParent2Fragment setStudent( Student bean ) {
			this.bean = bean;
			return this;
		}



		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_std_parent2, container, false );
			Parent[] parents = bean.getParents();
			Parent parent = parents[1];

			EditText editText = (EditText) rootView.findViewById( R.id.ET_info_p2Name );
			editText.setText( parent.getFirstName() + " " + parent.getLastName() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_p2Mail );
			editText.setText( parent.getMail() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_p1Phone );
			editText.setText( parent.getPhoneNumbers().toString() );

			return rootView;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class StdInfoFragment extends Fragment {
		
		private Student bean;
		
		public StdInfoFragment setStudent( Student bean ) {
			this.bean = bean;
			return this;
		}

		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate( R.layout.fragment_std_info, container, false );

			TextView tv = (TextView) rootView.findViewById( R.id.TV_info_header );
			tv.setText( bean.getIdent() );

			EditText editText = (EditText) rootView.findViewById( R.id.ET_info_firstName );
			editText.setText( bean.getFirstName() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_LastName );
			editText.setText( bean.getLastname() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_ident );
			editText.setText( bean.getIdent() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_birth );
			editText.setText( bean.getBirth() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_pc );
			editText.setText( bean.getPostalCode() );

			editText = (EditText) rootView.findViewById( R.id.ET_info_adr );
			editText.setText( bean.getAdress() );

			Button btn = (Button) rootView.findViewById( R.id.BTN_info_update );
			btn.setTag( bean );
			btn.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick( View v ) {
					StudentBean bean = (StudentBean) v.getTag();
					View rootView = v.getRootView();

					EditText editText = (EditText) rootView.findViewById( R.id.ET_info_firstName );
					bean.setFirstName( editText.getText().toString() );

					editText = (EditText) rootView.findViewById( R.id.ET_info_LastName );
					bean.setLastName( editText.getText().toString() );

					editText = (EditText) rootView.findViewById( R.id.ET_info_ident );
					String oldIdent = bean.getIdent();
					bean.setIdent( editText.getText().toString() );

					DataHandler.GetInstance().updateStudent( bean, oldIdent );
				}
			} );

			return rootView;
		}
	}
}
