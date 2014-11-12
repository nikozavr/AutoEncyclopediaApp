package ru.nikozavr.auto.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nikozavr.auto.R;

/**
 * Created by nikozavr on 2/16/14.
 */
public class MainFragment extends Fragment {

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        return rootView;
    }
}
