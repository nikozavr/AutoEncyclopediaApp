package ru.nikozavr.auto.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nikozavr.auto.R;

public class GalleryFragment extends Fragment {
	
	public GalleryFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
         
        return rootView;
    }
}
