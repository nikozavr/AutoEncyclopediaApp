package ru.nikozavr.auto;

import android.app.Activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.nikozavr.auto.adapter.NavDrawerListAdapter;
import ru.nikozavr.auto.fragments.HelpFragment;
import ru.nikozavr.auto.fragments.HistoryFragment;
import ru.nikozavr.auto.fragments.HomeFragment;
import ru.nikozavr.auto.fragments.InfoFragment;
import ru.nikozavr.auto.fragments.MarqInfoFragment;
import ru.nikozavr.auto.fragments.MarquesFragment;
import ru.nikozavr.auto.fragments.ProfileFragment;
import ru.nikozavr.auto.model.NavDrawerItem;

public class MainActivity2 extends Activity implements MarqInfoFragment.OnFragmentInteractionListener{
    private LruCache<String, Bitmap> mMemoryCache;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

 //   FragmentManager fragmentManager = getFragmentManager();
 //   Fragment curr_fragment = null;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

    AutoEncyclopedia globe;

    int NumSavedFragment = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main2);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_icons_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

        for(int i = 0; i < navMenuTitles.length; i++){
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i,-1)));
        }
/*		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
	//	navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
*/

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		//getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayFragment(1);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
        private int prev = 1;
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
            globe = (AutoEncyclopedia)getApplicationContext();
			if(position ==0 && !globe.isLoggedIn()){
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                mDrawerList.setItemChecked(prev, true);
                mDrawerList.setSelection(prev);
                mDrawerLayout.closeDrawer(mDrawerList);
            } else
                displayFragment(position);
                prev = position;
		}
	}

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title

        if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
            case R.id.home:
      /*          int count = fragmentManager.getBackStackEntryCount();
                String fragmentTag = fragmentManager.getBackStackEntryAt(count - 1).getName();
                Fragment curr_fragment = getFragmentManager().findFragmentByTag(fragmentTag);
                fragmentManager.beginTransaction().hide(curr_fragment).commit();
            //    fragmentManager.popBackStack();
                count--;
                fragmentTag = fragmentManager.getBackStackEntryAt(count - 1).getName();
                Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTag);
                if(fragment.isAdded()){
                    fragmentManager.beginTransaction().show(fragment).addToBackStack(fragmentTag).commit();
                }else {
                    fragmentManager.beginTransaction()
                            .add(R.id.frame_container, fragment, fragmentTag).addToBackStack(fragmentTag).commit();
                }*/
                return true;
	    	case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
		    	return true;
         ///   case R.id.action_exit:
         //       finish();
         //       return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayFragment(int position) {
		// update the main content by replacing fragments
/*
        Fragment fragment = null;
        String tag = null;
        NumSavedFragment = position;
		switch (position) {
		case 0:
            tag = "profile";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null){
                fragment = new ProfileFragment();
            }


/*
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new HomeFragment()).commit();

                mDrawerList.setItemChecked(1, true);
                mDrawerList.setSelection(1);
                setTitle(navMenuTitles[1]);
                mDrawerLayout.closeDrawer(mDrawerList);*/
/*
			break;
		case 1:
            tag = "home";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
			    fragment = new HomeFragment();
			break;
		case 2:
            tag = "marques";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
			    fragment = new MarquesFragment();
			break;
		case 3:
            tag = "history";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
			    fragment = new HistoryFragment();
			break;
		case 4:
            tag = "help";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
			    fragment = new HelpFragment();
			break;
		case 5:
            tag = "info";
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
			    fragment = new InfoFragment();
			break;

		default:
			break;
		}
        Fragment curr_fragment = null;
        int count = fragmentManager.getBackStackEntryCount();
        if(count != 0){
            String fragmentTag = fragmentManager.getBackStackEntryAt(count - 1).getName();
          //  curr_fragment = getFragmentManager().findFragmentByTag(fragmentTag);
        }
		if (fragment != null) {
            if(!fragment.isVisible()){
                if(curr_fragment != null){
                    fragmentManager.beginTransaction().hide(curr_fragment).commit();
                }
                if(fragment.isAdded()){
                    fragmentManager.beginTransaction().show(fragment).addToBackStack(tag).commit();
                }else {
                    fragmentManager.beginTransaction()
                    .add(R.id.frame_container, fragment, tag).addToBackStack(tag).commit();
                }
            }
           // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else{
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }*/
	}



	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void onFragmentInteraction(Uri uri){

    }
}
