package ru.nikozavr.auto.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.CurrentAuto;
import ru.nikozavr.auto.MainActivity;
import ru.nikozavr.auto.MarqInfoActivity;
import ru.nikozavr.auto.R;
import ru.nikozavr.auto.adapter.MarqAllAdapter;
import ru.nikozavr.auto.instruments.database.AllMarq.AsyncAllMarq;
import ru.nikozavr.auto.instruments.database.AllMarq.GetAllMarq;
import ru.nikozavr.auto.instruments.database.Images.Array.AsyncArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Array.GetArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Single.AsyncGetImage;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Marque;

public class MarquesFragment extends Fragment implements AsyncArrayImage,AsyncAllMarq,AsyncGetImage{


    GetAllMarq getallmarq = null;
    GetArrayImage getarrayimage = null;
    boolean isDownload = false;

    AutoEncyclopedia globe;
	public MarquesFragment(){}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // TODO Add your menu entries here

     //   menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                return true;
          /*  case R.id.action_refresh:
                ShowMarques();
                return true; */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowMarques(){
        globe = (AutoEncyclopedia)getActivity().getApplicationContext();
        if(!globe.isOnline()){
            Toast.makeText(getActivity(), R.string.error_connect,
                    Toast.LENGTH_SHORT).show();
        }else {
            getallmarq = new GetAllMarq(this, getString(R.string.url_get_all));
            getActivity().setProgressBarIndeterminateVisibility(true);

            getallmarq.execute();
        }
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        setHasOptionsMenu(true);
        ShowMarques();

        View rootView = inflater.inflate(R.layout.fragment_marques, container, false);
        return rootView;
    }

    private ArrayList<ImageItem> forAdapter(ArrayList<Marque> marques){
        ArrayList<ImageItem> result = new ArrayList<ImageItem>();
        for (int i = 0; i < marques.size(); i++){
            result.add(marques.get(i).getImage());
        }
        return result;
    }

    public void finishAllMarq(final ArrayList<Marque> result){
        if(result == null){
            Toast.makeText(getActivity(), getString(R.string.error_server),
                    Toast.LENGTH_SHORT).show();
            getActivity().setProgressBarIndeterminateVisibility(false);
        } else {
            TextView txtDownloading = (TextView) getView().findViewById(R.id.txtDownloadMarq);
            txtDownloading.setVisibility(TextView.INVISIBLE);

            GridView gridView = (GridView) getView().findViewById(R.id.marqView);
            ArrayList<ImageItem> images = forAdapter(result);

            final MarqAllAdapter marqad = new MarqAllAdapter(getView().getContext(), R.layout.row_grid_marq,
                    images);
            gridView.setAdapter(marqad);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    CurrentAuto.setMarque(result.get(position));
                    TextView txt = (TextView) v.findViewById(R.id.txt_marq);
                    Toast.makeText(getActivity(), txt.getText(),
                            Toast.LENGTH_SHORT).show();

                   // ((MainActivity)getActivity()).fragmentsInteraction.Interact("marq_info");
                                     //   Fragment fragment = new MarqInfoFragment();
                 //   FragmentManager fragmentManager = getFragmentManager();
                 //   fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                 //           add(R.id.container, fragment, MarqInfoFragment.TAG).addToBackStack(MarqInfoFragment.TAG).commit();
                    ((MainActivity)getActivity()).LaunchNewActivity();
                   // Intent i = new Intent(getActivity().getApplicationContext(), MarqInfoActivity.class);

                 //  ((AutoEncyclopedia)getActivity().getApplication()).addBitmapToMemoryCache(result.get(position).getImage().getKey(),
                 //          result.get(position).getImage().getBitmap());
                  // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  // startActivity(i);
                }
            });

            getarrayimage = new GetArrayImage(this, this);
            getarrayimage.Width = 150;
            getarrayimage.Height = 150;
            getarrayimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, images);
        }

    };

    @Override
    public void finishArrayImage() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        isDownload = true;
    }


    public void finishGetPic(ImageItem result){
        GridView gridView = (GridView) getView().findViewById(R.id.marqView);
        gridView.invalidateViews();
    }

    public AutoEncyclopedia getAutoEncyclopedia(){
        return (AutoEncyclopedia)getActivity().getApplication();
    }

}
