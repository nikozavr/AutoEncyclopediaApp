package ru.nikozavr.auto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.util.DisplayMetrics;
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
import java.util.List;

import ru.nikozavr.auto.adapter.ImageDownloadView;
import ru.nikozavr.auto.adapter.ImageForScrolling;
import ru.nikozavr.auto.adapter.ListModelAdapter;
import ru.nikozavr.auto.fragments.MarquesFragment;
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

public class MarqInfoActivity extends MainActivity implements AsyncGetImage, AsyncMarqInfo, AsyncMarqModels, AsyncArrayImage {

    private DictionaryCountries trans;

    private LinearLayout CarsViewLayout;

    private Boolean isRefreshing;


    private void reset(){
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewMarq);
        scrollView.setVisibility(ScrollView.INVISIBLE);

        ListView lstModels = (ListView) findViewById(R.id.lstModels);
        lstModels.setVisibility(ListView.INVISIBLE);

        TextView txtDownloading = (TextView) findViewById(R.id.lblDownloadMarq);
        txtDownloading.setVisibility(TextView.VISIBLE);

        Button btnChange = (Button) findViewById(R.id.btnMarqChange);
        btnChange.setVisibility(View.INVISIBLE);
        btnChange.setText(R.string.all_models);

        try{
            CarsViewLayout.removeViews(0,CurrentAuto.getMarque().getModels().size());
        } catch (NullPointerException e){

        }
    }


    private void getInfo() {
        isRefreshing = true;
        invalidateOptionsMenu();
        TextView txtName = (TextView) findViewById(R.id.lblMarqName);
        txtName.setText(CurrentAuto.getMarque().getName());
        ImageView imgView = (ImageView) findViewById(R.id.imMarq);
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

        setProgressBarIndeterminateVisibility(true);
        task.execute(CurrentAuto.getMarque());

        GetMarqModels task2 = new GetMarqModels(this, getString(R.string.url_get_marq_models));
        task2.execute(CurrentAuto.getMarque());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trans = new DictionaryCountries(getResources());

        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_marq_info);

//        getActionBar().setDisplayHomeAsUpEnabled(true);

       LinearLayout web = (LinearLayout) findViewById(R.id.layoutWeb);
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

        final Button btnChange = (Button) findViewById(R.id.btnMarqChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewMarq);
                ListView lstModels = (ListView) findViewById(R.id.lstModels);
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
    }

    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marq_info, menu);
        //this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        if(isRefreshing){
            menu.findItem(R.id.action_marq_refresh).setVisible(false);
            setProgressBarIndeterminateVisibility(true);
        } else {
            menu.findItem(R.id.action_marq_refresh).setVisible(true);
            setProgressBarIndeterminateVisibility(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
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
            TextView txtDownloading = (TextView) findViewById(R.id.lblDownloadMarq);
            txtDownloading.setVisibility(TextView.INVISIBLE);

            TextView txtContry = (TextView) findViewById(R.id.lblMarqCountry);
            txtContry.setVisibility(TextView.VISIBLE);
            txtContry.setText(getString(R.string.country) + " " + trans.Translate(CurrentAuto.getMarque().getCountry()));

            TextView txtYear = (TextView) findViewById(R.id.lblMarqYear);
            txtContry.setVisibility(TextView.VISIBLE);
            String date = CurrentAuto.getMarque().getDateProd().toLocaleString().split("г.")[0];
            txtYear.setVisibility(TextView.VISIBLE);
            txtYear.setText(getString(R.string.year) + " " + date);


            TextView txtInfo = (TextView) findViewById(R.id.txtInfoMarq);
            if(CurrentAuto.getMarque().getInfo() != null) {

                txtInfo.setText(CurrentAuto.getMarque().getInfo());
            } else {
                txtInfo.setText(R.string.error_info);
            }

            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewMarq);
            scrollView.setVisibility(ScrollView.VISIBLE);

            Button btnChange = (Button) findViewById(R.id.btnMarqChange);
            btnChange.setVisibility(Button.VISIBLE);

            TextView txtWeb = (TextView) findViewById(R.id.txtWeb);
            txtWeb.setText(CurrentAuto.getMarque().getContact().toString());

            setProgressBarIndeterminateVisibility(true);
        } else {
            Toast.makeText(this, getString(R.string.error_server),
                    Toast.LENGTH_SHORT).show();
            setProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    public void finishGetPic(ImageItem result) {
        if(result.getType() == ImageItem.Type.MARQ) {
            ImageView imgView = (ImageView) findViewById(R.id.imMarq);
            imgView.setImageBitmap(result.getBitmap());
        } else {
            CarsViewLayout = (LinearLayout) findViewById(R.id.photoScrollMarq);

            CarsViewLayout.invalidate();
            ListView listModels = (ListView) findViewById(R.id.lstModels);
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
        Intent i = new Intent(getApplicationContext(), ModelInfoActivity.class);
        i.putExtra(Model.KEY,model);
        i.putExtra(Marque.KEY, CurrentAuto.getMarque().getName());
        ((AutoEncyclopedia)getApplication()).addBitmapToMemoryCache(model.getTitleImage().getKey(),model.getTitleImage().getBitmap());
        i.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
    }

    @Override
    public void getModelsFinish(Marque result){
        if(CurrentAuto.getMarque().getModels()!=null) {
            GetArrayImage task = new GetArrayImage(this, this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, forModelImages());
            CarsViewLayout = (LinearLayout) findViewById(R.id.photoScrollMarq);

            for (int i = 0; i < result.getModels().size(); i++) {
                ImageDownloadView im = new ImageDownloadView(this, result.getModels().get(i).getTitleImage());
                final int pos = i;
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToModelInfo(CurrentAuto.getMarque().getModels().get(pos));
                    }
                });
                CarsViewLayout.addView(im, i);
            }

            final ListView listModels = (ListView) findViewById(R.id.lstModels);
            final ListModelAdapter listModelAdapter = new ListModelAdapter(this, R.layout.row_list_model, CurrentAuto.getMarque().getModels());
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
        invalidateOptionsMenu();
    }

    public AutoEncyclopedia getAutoEncyclopedia(){
        return (AutoEncyclopedia)getApplication();
    }

}
