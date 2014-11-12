package ru.nikozavr.auto.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.CurrentAuto;
import ru.nikozavr.auto.DictionaryCountries;
import ru.nikozavr.auto.ModelInfoActivity;
import ru.nikozavr.auto.R;
import ru.nikozavr.auto.adapter.ImageDownloadView;
import ru.nikozavr.auto.adapter.ListModelAdapter;
import ru.nikozavr.auto.instruments.database.Images.Array.AsyncArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Array.GetArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Single.AsyncGetImage;
import ru.nikozavr.auto.instruments.database.Images.Single.GetImage;
import ru.nikozavr.auto.instruments.database.MarqInfo.AsyncMarqInfo;
import ru.nikozavr.auto.instruments.database.MarqInfo.GetMarqInfo;
import ru.nikozavr.auto.instruments.database.MarqInfo.MarqModel.AsyncMarqModels;
import ru.nikozavr.auto.instruments.database.MarqInfo.MarqModel.GetMarqModels;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Marque;
import ru.nikozavr.auto.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MarqInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MarqInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MarqInfoFragment extends Fragment implements AsyncGetImage, AsyncMarqInfo, AsyncMarqModels, AsyncArrayImage {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "marq_info";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private DictionaryCountries trans;

    private LinearLayout CarsViewLayout;

    private Boolean isRefreshing;

    private View MarqInfoFragmentView;


    private void reset(){
        ScrollView scrollView = (ScrollView) MarqInfoFragmentView.findViewById(R.id.scrollViewMarq);
        scrollView.setVisibility(ScrollView.INVISIBLE);

        ListView lstModels = (ListView) MarqInfoFragmentView.findViewById(R.id.lstModels);
        lstModels.setVisibility(ListView.INVISIBLE);

        TextView txtDownloading = (TextView) MarqInfoFragmentView.findViewById(R.id.lblDownloadMarq);
        txtDownloading.setVisibility(TextView.VISIBLE);

        Button btnChange = (Button) MarqInfoFragmentView.findViewById(R.id.btnMarqChange);
        btnChange.setVisibility(View.INVISIBLE);
        btnChange.setText(R.string.all_models);

        try{
            CarsViewLayout.removeViews(0, CurrentAuto.getMarque().getModels().size());
        } catch (NullPointerException e){

        }
    }


    private void getInfo() {
        isRefreshing = true;
        getActivity().invalidateOptionsMenu();
        TextView txtName = (TextView) MarqInfoFragmentView.findViewById(R.id.lblMarqName);
        txtName.setText(CurrentAuto.getMarque().getName());
        ImageView imgView = (ImageView) MarqInfoFragmentView.findViewById(R.id.imMarq);
        //   Bitmap image = ((AutoEncyclopedia)getApplication()).getBitmapFromMemCache(CurrentAuto.getMarque().getImage().getKey());
        //    if(image != null) {
        //       CurrentAuto.getMarque().getImage().setBitmap(image);
        //   } else {
        //       CurrentAuto.getMarque().getImage().repairBitmap();
        //    }
        imgView.setImageBitmap(CurrentAuto.getMarque().getImage().getBitmap());
        if(!CurrentAuto.getMarque().getImage().isDownloaded()){
            GetImage task = new GetImage(this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrentAuto.getMarque().getImage());
        }

        GetMarqInfo task = new GetMarqInfo(this, getString(R.string.url_get_marq_info));

        getActivity().setProgressBarIndeterminateVisibility(true);
        task.execute(CurrentAuto.getMarque());

        GetMarqModels task2 = new GetMarqModels(this, getString(R.string.url_get_marq_models));
        task2.execute(CurrentAuto.getMarque());
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarqInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarqInfoFragment newInstance(String param1, String param2) {
        MarqInfoFragment fragment = new MarqInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public MarqInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        trans = new DictionaryCountries(getResources());

//        getActivity().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MarqInfoFragmentView = inflater.inflate(R.layout.fragment_marq_info, container, false);

//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout web = (LinearLayout) MarqInfoFragmentView.findViewById(R.id.layoutWeb);
        web.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackground(getResources().getDrawable(R.color.blue));
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackground(getResources().getDrawable(R.color.background_layout));
                        Intent openlink = new Intent(Intent.ACTION_VIEW, Uri.parse(CurrentAuto.getMarque().getContact().toString()));
                        startActivity(openlink);
                        break;
                }
                return true;
            }
        });

        final Button btnChange = (Button) MarqInfoFragmentView.findViewById(R.id.btnMarqChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = (ScrollView) MarqInfoFragmentView.findViewById(R.id.scrollViewMarq);
                ListView lstModels = (ListView)MarqInfoFragmentView. findViewById(R.id.lstModels);
                if (scrollView.getVisibility() == ScrollView.VISIBLE) {
                    scrollView.setVisibility(ScrollView.INVISIBLE);
                    lstModels.setVisibility(ListView.VISIBLE);
                    btnChange.setText(getString(R.string.info_marq));
                    lstModels.invalidateViews();
                } else {
                    scrollView.setVisibility(ScrollView.VISIBLE);
                    lstModels.setVisibility(ListView.INVISIBLE);
                    btnChange.setText(getString(R.string.all_models));
                }
            }
        });

        getInfo();

        return MarqInfoFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    public void onBackPressed(){
      //  finish();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.marq_info, menu);
        //this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        if(isRefreshing){
            menu.findItem(R.id.action_marq_refresh).setVisible(false);
            getActivity().setProgressBarIndeterminateVisibility(true);
        } else {
            menu.findItem(R.id.action_marq_refresh).setVisible(true);
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

               // finish();
                return true;
            //  case R.id.action_marq_home:
            //      finish();
            //      return true;
            case R.id.action_marq_refresh:
                item.setVisible(false);
                reset();
                getInfo();
                return true;
            //  case R.id.action_marq_settings:
            //      return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getMarqInfoFinish(Marque result){
        if(result != null) {
            TextView txtDownloading = (TextView) MarqInfoFragmentView.findViewById(R.id.lblDownloadMarq);
            txtDownloading.setVisibility(TextView.INVISIBLE);

            TextView txtContry = (TextView) MarqInfoFragmentView.findViewById(R.id.lblMarqCountry);
            txtContry.setVisibility(TextView.VISIBLE);
            txtContry.setText(getString(R.string.country) + " " + trans.Translate(CurrentAuto.getMarque().getCountry()));

            TextView txtYear = (TextView) MarqInfoFragmentView.findViewById(R.id.lblMarqYear);
            txtContry.setVisibility(TextView.VISIBLE);
            String date = CurrentAuto.getMarque().getDateProd().toLocaleString().split("г.")[0];
            txtYear.setVisibility(TextView.VISIBLE);
            txtYear.setText(getString(R.string.year) + " " + date);


            TextView txtInfo = (TextView) MarqInfoFragmentView.findViewById(R.id.txtInfoMarq);
            if(CurrentAuto.getMarque().getInfo() != null) {

                txtInfo.setText(CurrentAuto.getMarque().getInfo());
            } else {
                txtInfo.setText(R.string.error_info);
            }

            ScrollView scrollView = (ScrollView) MarqInfoFragmentView.findViewById(R.id.scrollViewMarq);
            scrollView.setVisibility(ScrollView.VISIBLE);

            Button btnChange = (Button) MarqInfoFragmentView.findViewById(R.id.btnMarqChange);
            btnChange.setVisibility(Button.VISIBLE);

            TextView txtWeb = (TextView) MarqInfoFragmentView.findViewById(R.id.txtWeb);
            txtWeb.setText(CurrentAuto.getMarque().getContact().toString());

            getActivity().setProgressBarIndeterminateVisibility(true);
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_server),
                    Toast.LENGTH_SHORT).show();
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    public void finishGetPic(ImageItem result) {
        if(result.getType() == ImageItem.Type.MARQ) {
            ImageView imgView = (ImageView) MarqInfoFragmentView.findViewById(R.id.imMarq);
            imgView.setImageBitmap(result.getBitmap());
        } else {
            CarsViewLayout = (LinearLayout) MarqInfoFragmentView.findViewById(R.id.photoScrollMarq);

            CarsViewLayout.invalidate();
            ListView listModels = (ListView) MarqInfoFragmentView.findViewById(R.id.lstModels);
            listModels.invalidateViews();
        }
    }

    public ArrayList<ImageItem> forModelImages(){
        ArrayList<ImageItem> result = new ArrayList<ImageItem>();
        for(int i = 0; i < CurrentAuto.getMarque().getModels().size(); i++){
            CurrentAuto.getMarque().getModels().get(i).getTitleImage().setSize(Math.round(getResources().getDimension(R.dimen.photo_height)),
                    Math.round(getResources().getDimension(R.dimen.photo_width)));
            result.add(CurrentAuto.getMarque().getModels().get(i).getTitleImage());
        }
        return result;
    }

    private void goToModelInfo(Model model){
        Intent i = new Intent(getActivity().getApplicationContext(), ModelInfoActivity.class);
        i.putExtra(Model.KEY,model);
        i.putExtra(Marque.KEY, CurrentAuto.getMarque().getName());
        ((AutoEncyclopedia)getActivity().getApplication()).addBitmapToMemoryCache(model.getTitleImage().getKey(),model.getTitleImage().getBitmap());
        i.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
    }

    @Override
    public void getModelsFinish(Marque result){
        if(CurrentAuto.getMarque().getModels()!=null) {
            GetArrayImage task = new GetArrayImage(this, this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, forModelImages());
            CarsViewLayout = (LinearLayout) MarqInfoFragmentView.findViewById(R.id.photoScrollMarq);

            for (int i = 0; i < result.getModels().size(); i++) {
                ImageDownloadView im = new ImageDownloadView(getActivity(), result.getModels().get(i).getTitleImage());
                final int pos = i;
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToModelInfo(CurrentAuto.getMarque().getModels().get(pos));
                    }
                });
                CarsViewLayout.addView(im, i);
            }

            final ListView listModels = (ListView) MarqInfoFragmentView.findViewById(R.id.lstModels);
            final ListModelAdapter listModelAdapter = new ListModelAdapter(getActivity(), R.layout.row_list_model, CurrentAuto.getMarque().getModels());
            listModels.setAdapter(listModelAdapter);

            listModels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goToModelInfo(CurrentAuto.getMarque().getModels().get(position));
                }
            });
        } else {


        }
    }

    @Override
    public void finishArrayImage() {
        isRefreshing = false;
        getActivity().invalidateOptionsMenu();
    }

    public AutoEncyclopedia getAutoEncyclopedia(){
        return (AutoEncyclopedia)getActivity().getApplication();
    }


}
