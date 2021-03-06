package no.glv.android.stdntworkflow;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.core.DataHandler;

/**
 * The entry point to the Application.
 * <p>
 * <p>This will load the <code>MainFragment</code>.</p>
 */
@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {

    public static final int FRAG_MAIN = 0;
    public static final int FRAG_CLASS = 1;
    public static final int FRAG_TASK = 2;
    public static final int FRAG_SUBJTYPE = 3;
    public static final int FRAG_PREF = 4;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.navdrawer_container );
        DataHandler.Init( getApplication() );

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray( R.array.nav_drawer_items );

        // nav drawer icons from resources
        TypedArray navMenuIcons = getResources()
                .obtainTypedArray( R.array.nav_drawer_icons );

        mDrawerLayout = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        mDrawerList = ( ListView ) findViewById( R.id.list_slidermenu );

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        for ( int i=0 ; i<navMenuTitles.length ; i++ ) {
            navDrawerItems.add( new NavDrawerItem( navMenuTitles[i], navMenuIcons.getResourceId( i, -1 ) ) );
        }

        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener( new SlideMenuClickListener() );

        // setting the nav drawer list adapter
        NavDrawerListAdapter adapter = new NavDrawerListAdapter( getApplicationContext(),
                navDrawerItems );
        mDrawerList.setAdapter( adapter );

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled( true );
        getActionBar().setHomeButtonEnabled( true );


        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed( View view ) {
                getActionBar().setTitle( mTitle );
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened( View drawerView ) {
                getActionBar().setTitle( mDrawerTitle );
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener( mDrawerToggle );

        if ( savedInstanceState == null ) {
            // on first time display view for first nav item
            displayView( 0 );
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
            // display view for selected nav drawer item
            displayView( position );
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // toggle nav drawer on selecting action bar app icon/title
        if ( mDrawerToggle.onOptionsItemSelected( item ) ) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu( Menu menu ) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen( mDrawerList );
        MenuItem item = menu.findItem( R.id.menu_overflow );
        if ( item != null )
            item.setVisible( !drawerOpen );

        return super.onPrepareOptionsMenu( menu );
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView( int position ) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        Intent intent = null;
        switch ( position ) {
            case FRAG_MAIN:
                fragment = new MainFragment();
                break;
            case FRAG_CLASS:
                fragment = new NewClassFragment();
                break;
            case FRAG_TASK:
                intent = new Intent( this, TaskViewActivity.class );
                break;
            case FRAG_SUBJTYPE:
                fragment = new SubjectTypesFragment();
                break;
            case FRAG_PREF:
                fragment = new PrefFragment();
                break;
            case 5:
                fragment = null;
                break;

            default:
                break;
        }

        boolean updateDrawer = false;

        if ( fragment != null ) {
            updateDrawer = true;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace( R.id.frame_container, fragment );
            if ( position > 0 ) {
                ft.addToBackStack( null );
            }
            ft.commit();
        } else if ( intent != null ) {
            updateDrawer = true;
            startActivity( intent );
        }

        if ( updateDrawer ) {
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked( position, true );
            mDrawerList.setSelection( position );
            setTitle( navMenuTitles[position] );
            mDrawerLayout.closeDrawer( mDrawerList );
        }
    }

    @Override
    public void setTitle( CharSequence title ) {
        mTitle = title;
        getActionBar().setTitle( mTitle );
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate( Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig ) {
        super.onConfigurationChanged( newConfig );
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged( newConfig );
    }

}
