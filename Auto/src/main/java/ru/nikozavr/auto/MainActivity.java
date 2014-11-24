package ru.nikozavr.auto;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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

import java.util.Arrays;
import java.util.List;

import ru.nikozavr.auto.fragments.HelpFragment;
import ru.nikozavr.auto.fragments.HistoryFragment;
import ru.nikozavr.auto.fragments.HomeFragment;
import ru.nikozavr.auto.fragments.InfoFragment;
import ru.nikozavr.auto.fragments.MarqInfoFragment;
import ru.nikozavr.auto.fragments.MarquesFragment;
import ru.nikozavr.auto.fragments.ProfileFragment;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MarqInfoFragment.OnFragmentInteractionListener, FragmentsInteraction.ChangeToolbox{
    private MainActivity mainActivity;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public FragmentsInteraction fragmentsInteraction;

    private FragmentManager fragmentManager;

    /**
  }.
     */
    private CharSequence mTitle;

    private Resources res;

    private Boolean first;

    private Toolbar toolbar;

    private List<String> navDrawNames;

    private AutoEncyclopedia globe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        super.onCreate(savedInstanceState);
        res = getResources();
//        setContentView(R.layout.activity_main);

  /*      toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
*/
        fragmentManager = getSupportFragmentManager();
        navDrawNames = Arrays.asList(res.getStringArray(R.array.nav_drawer_list_items));
    }

    public MainActivity getMainActivity(){
        return this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        String curr_tag = null;
        String tag = navDrawNames.get(position);

        globe = (AutoEncyclopedia)getApplicationContext();
        if(position ==0 && !globe.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
        } else{
            int count = fragmentManager.getBackStackEntryCount();
            if(count != 0){
                curr_tag = fragmentManager.getBackStackEntryAt(count - 1).getName();
            }

            if(curr_tag != tag) {
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                Fragment curr_fragment = fragmentManager.findFragmentByTag(curr_tag);
                if (fragment == null) {
                    switch (position) {
                        case 0:
                            fragment = new ProfileFragment();
                            break;
                        case 1:
                            fragment = new HomeFragment();
                            break;
                        case 2:
                            fragment = new MarquesFragment();
                            break;
                        case 3:
                            fragment = new HistoryFragment();
                            break;
                        case 4:
                            fragment = new HelpFragment();
                            break;
                        case 5:
                            fragment = new InfoFragment();
                            break;
                    }
                }
                if (fragment != null) {
                    if (!fragment.isVisible()) {
                        if (curr_fragment != null) {
                                fragmentManager.beginTransaction().remove(curr_fragment).commit();

                        }
                        if (fragment.isAdded()) {
                            fragmentManager.beginTransaction().show(fragment).addToBackStack(tag).commit();
                        } else {
                            fragmentManager.beginTransaction()
                                    .add(R.id.container, fragment, tag).addToBackStack(tag).commit();
                        }
                    }
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
     //       return true;
       // }
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

    public Toolbar getToolBar(){
        return toolbar;
    }

    @Override
    public void ChangeToolboxIcon(boolean isArrow, int numberTitle) {
        mNavigationDrawerFragment.ChangeToolboxIcon(isArrow, numberTitle);
    }

    @Override
    public void onBackPressed() {
        if(!fragmentsInteraction.NavigiteUpFragments())
            finish();
    }

  //  @Override
  //  public final MainActivity getParent(){

   // }
     public void LaunchNewActivity(){
         Intent i = new Intent(getApplicationContext(), MarqInfoActivity.class);

         //  ((AutoEncyclopedia)getActivity().getApplication()).addBitmapToMemoryCache(result.get(position).getImage().getKey(),
         //          result.get(position).getImage().getBitmap());
         i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivityForResult(i, 1);
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = data.getIntExtra("position", -1);
        onNavigationDrawerItemSelected(position);
    }
}
