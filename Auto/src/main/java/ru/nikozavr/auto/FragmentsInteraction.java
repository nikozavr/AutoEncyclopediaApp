package ru.nikozavr.auto;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nikozavr.auto.fragments.HelpFragment;
import ru.nikozavr.auto.fragments.HistoryFragment;
import ru.nikozavr.auto.fragments.HomeFragment;
import ru.nikozavr.auto.fragments.InfoFragment;
import ru.nikozavr.auto.fragments.MarqInfoFragment;
import ru.nikozavr.auto.fragments.MarquesFragment;
import ru.nikozavr.auto.fragments.ProfileFragment;

/**
 * Created by Никита on 15.11.2014.
 */
public class FragmentsInteraction {

    private FragmentManager fragmentManager = null;

    private List<String> fragmentNames = null;

    private List<String> noDrawNames = null;

    private List<String> navDrawNames = null;

    private String tagFragVisible = null;

    private String tagFragPrevious = null;

    ChangeToolbox delegate = null;


    public FragmentsInteraction(Context context){
        Resources res = context.getResources();
        fragmentManager = ((ActionBarActivity)context).getSupportFragmentManager();
        delegate = (ChangeToolbox)context;

        fragmentNames = Arrays.asList(res.getStringArray(R.array.frag_tags));
        navDrawNames = Arrays.asList(res.getStringArray(R.array.nav_drawer_list_items));
        noDrawNames = new ArrayList<String>(fragmentNames);
        noDrawNames.removeAll(navDrawNames);
    }

    public void InteractNavDraw(int pos){
        Interact(navDrawNames.get(pos));
    }


    public void Interact(String tag){
        String curr_tag = null;

        int count = fragmentManager.getBackStackEntryCount();
        if(count != 0){
            curr_tag = fragmentManager.getBackStackEntryAt(count - 1).getName();
        }

        if(curr_tag != tag){
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            Fragment curr_fragment = fragmentManager.findFragmentByTag(curr_tag);
            if(fragment == null) {
                switch (fragmentNames.indexOf(tag)) {
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
                    case 6:
                        fragment = new MarqInfoFragment();
                      //  tag = ((MarqInfoFragment)fragment).getFragID();
                        break;
                    case 7:

                        break;
                }
            }
            if(fragment != null){
                if(!fragment.isVisible()){
                    if(curr_fragment != null){
                        if(noDrawNames.contains(curr_tag)) {
                            fragmentManager.beginTransaction().remove(curr_fragment).commit();
                            tagFragPrevious = null;
                        } else {
                            fragmentManager.beginTransaction().hide(curr_fragment).commit();
                            tagFragPrevious = curr_tag;
                        }
                    }
                    if(fragment.isAdded()){
                        fragmentManager.beginTransaction().show(fragment).addToBackStack(tag).commit();
                    }else{
                        fragmentManager.beginTransaction()
                                .add(R.id.container, fragment, tag).addToBackStack(tag).commit();
                    }
                }
                tagFragVisible = tag;
            } else{
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
        if(noDrawNames.contains(tagFragVisible)){
            delegate.ChangeToolboxIcon(true, fragmentNames.indexOf(tag));
        }else{
            delegate.ChangeToolboxIcon(false, fragmentNames.indexOf(tag));
        }
    }

    public interface ChangeToolbox{
        public void ChangeToolboxIcon(boolean isArrow, int numberName);
    };


    public Boolean NavigiteUpFragments(){
        Boolean res = false;
        int count = fragmentManager.getBackStackEntryCount();
        if(count > 1){
            Fragment prevFragment = fragmentManager.findFragmentByTag(tagFragPrevious);
            Fragment visFragment = fragmentManager.findFragmentByTag(tagFragVisible);
            fragmentManager.beginTransaction().show(prevFragment).addToBackStack(tagFragPrevious).commit();
            fragmentManager.beginTransaction().remove(visFragment).commit();
           // tagFragVisible =
            res = true;
            if(noDrawNames.contains(tagFragVisible)){
                delegate.ChangeToolboxIcon(true, fragmentNames.indexOf(tagFragPrevious));
            }else{
                delegate.ChangeToolboxIcon(false, fragmentNames.indexOf(tagFragPrevious));
            }
        }

        return res;
    }

    public boolean isRoot(){
        return navDrawNames.contains(tagFragVisible);
    }


}
