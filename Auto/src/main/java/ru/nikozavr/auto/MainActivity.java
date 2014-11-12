package ru.nikozavr.auto;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.nikozavr.auto.fragments.HelpFragment;
import ru.nikozavr.auto.fragments.HistoryFragment;
import ru.nikozavr.auto.fragments.HomeFragment;
import ru.nikozavr.auto.fragments.InfoFragment;
import ru.nikozavr.auto.fragments.MarqInfoFragment;
import ru.nikozavr.auto.fragments.MarquesFragment;
import ru.nikozavr.auto.fragments.ProfileFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MarqInfoFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Resources res;

    private Boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        String tag = null;

        Fragment curr_fragment = null;
        String curr_tag = null;

        FragmentManager fragmentManager = getSupportFragmentManager();
       // fragmentManager.beginTransaction()
       //         .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
       //         .commit();

        String[] list_items = res.getStringArray(R.array.nav_drawer_list_items);

        int count = fragmentManager.getBackStackEntryCount();
        if(count != 0)
            curr_tag = fragmentManager.getBackStackEntryAt(count-1).getName();

        tag = list_items[position];

        if(curr_tag != tag){
            switch (position) {
                case 0:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null){
                        fragment = new ProfileFragment();
                    }
                    break;
                case 1:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null)
                        fragment = new HomeFragment();
                    break;
                case 2:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null)
                        fragment = new MarquesFragment();
                    break;
                case 3:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null)
                        fragment = new HistoryFragment();
                    break;
                case 4:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null)
                        fragment = new HelpFragment();
                    break;
                case 5:
                    fragment = fragmentManager.findFragmentByTag(tag);
                    if(fragment == null)
                        fragment = new InfoFragment();
                    break;

                default:
                    break;
            }


            curr_fragment = fragmentManager.findFragmentByTag(curr_tag);
            if(fragment != null){
                if(!fragment.isVisible()){
                    if(curr_fragment != null){
                        fragmentManager.beginTransaction().hide(curr_fragment).commit();
                    }
                    if(fragment.isAdded()){
                        fragmentManager.beginTransaction().show(fragment).addToBackStack(tag).commit();
                    }else{
                        fragmentManager.beginTransaction()
                                .add(R.id.container, fragment, tag).addToBackStack(tag).commit();
                    }
                }
            } else{
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        }



    }

    public void onSectionAttached(int number) {
        String[] Titles = res.getStringArray(R.array.nav_drawer_name_items);
        mTitle = Titles[number-1];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
