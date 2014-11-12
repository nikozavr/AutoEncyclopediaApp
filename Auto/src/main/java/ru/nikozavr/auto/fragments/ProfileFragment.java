package ru.nikozavr.auto.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.LoginActivity;
import ru.nikozavr.auto.MainActivity;
import ru.nikozavr.auto.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {
	
	AutoEncyclopedia globe;

    TextView txtLogin;
    TextView txtEmail;
    TextView txtPermission;
    Button btnExit;

    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    private String[] navMenuTitles;

    @Override
    public void onClick(View v){
        globe.logoutUser();

        Fragment home = new HomeFragment();
        FragmentTransaction tr = getFragmentManager().beginTransaction();

        tr.replace(R.id.frame_container, home, "home");
        tr.addToBackStack("home");
        tr.commit();
        mDrawerList = (ListView)getActivity().findViewById(R.id.list_slidermenu);
     //   navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);

        mDrawerList.setItemChecked(1, true);
        mDrawerList.setSelection(1);
        getActivity().setTitle(navMenuTitles[1]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;

        globe = (AutoEncyclopedia)getActivity().getApplicationContext();
        if(globe.isLoggedIn()) {

            rootView = inflater.inflate(R.layout.fragment_profile, container, false);

            ImageView image = (ImageView) rootView.findViewById(R.id.imProfile);
            image.setImageResource(R.drawable.ic_action_person);

            btnExit = (Button) rootView.findViewById(R.id.btnExit);
            btnExit.setOnClickListener(this);
        } else {
            Logging();
        }
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(globe.isLoggedIn()) {
            String[] info;
            globe = (AutoEncyclopedia) getActivity().getApplicationContext();
            info = globe.getUserDetails();

            txtLogin = (TextView) getView().findViewById(R.id.lblProfileName);
            txtEmail = (TextView) getView().findViewById(R.id.lblProfileEmail);
            txtPermission = (TextView) getView().findViewById(R.id.lblProfileStatus);

            txtLogin.setText(info[0]);
            txtEmail.setText(getString(R.string.email) + " " + info[1]);
            txtPermission.setText(getString(R.string.permission) + " " + info[2]);

            TextView txtDownloading = (TextView) getView().findViewById(R.id.lblDownloadEdition);
            TextView txtNoEdition = (TextView) getView().findViewById(R.id.lblNoEdition);

            txtDownloading.setVisibility(View.INVISIBLE);
            txtNoEdition.setVisibility(View.VISIBLE);
        } else {
            Logging();
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    private void Logging(){
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
    }

}
