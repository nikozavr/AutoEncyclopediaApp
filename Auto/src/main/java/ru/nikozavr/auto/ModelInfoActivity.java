package ru.nikozavr.auto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.nikozavr.auto.adapter.ImageDownloadView;
import ru.nikozavr.auto.adapter.ImageForScrolling;
import ru.nikozavr.auto.adapter.ListGenersAdapter;
import ru.nikozavr.auto.adapter.ListModelAdapter;
import ru.nikozavr.auto.instruments.database.GenerInfo.AsyncGenerInfo;
import ru.nikozavr.auto.instruments.database.GenerInfo.GetGenerInfo;
import ru.nikozavr.auto.instruments.database.Images.Array.AsyncArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Array.GetArrayImage;
import ru.nikozavr.auto.instruments.database.Images.Single.AsyncGetImage;
import ru.nikozavr.auto.instruments.database.Images.Single.GetImage;
import ru.nikozavr.auto.instruments.database.ModelInfo.AsyncModelInfo;
import ru.nikozavr.auto.instruments.database.ModelInfo.GetModelInfo;
import ru.nikozavr.auto.instruments.database.ModelInfo.ModelGener.AsyncModelGeners;
import ru.nikozavr.auto.instruments.database.ModelInfo.ModelGener.GetModelGeners;
import ru.nikozavr.auto.model.Generation;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Marque;
import ru.nikozavr.auto.model.Model;


public class ModelInfoActivity /*extends Activity implements AsyncGetImage, AsyncModelInfo, AsyncModelGeners, AsyncArrayImage, AsyncGenerInfo*/{
/*
    private Model model;

    private Boolean isRefreshing;

    private LinearLayout CarsViewLayout;

    private ArrayList<ImageForScrolling> ImagesForScrolling;

    private void reset(){
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewModel);
        scrollView.setVisibility(ScrollView.INVISIBLE);

        ListView lstModels = (ListView) findViewById(R.id.lstGenerations);
        lstModels.setVisibility(ListView.INVISIBLE);

        CarsViewLayout.removeViews(0,ImagesForScrolling.size());

        for(int i = 0; i < ImagesForScrolling.size(); i++){
            ImagesForScrolling.get(i).setImageBitmap(null);
        }

        ImagesForScrolling.clear();
        model = null;
    }

    private void getInfo(){
        isRefreshing = true;
        invalidateOptionsMenu();

        model = (Model)getIntent().getParcelableExtra(Model.KEY);

        Bitmap image = ((AutoEncyclopedia)getApplication()).getBitmapFromMemCache(model.getTitleImage().getKey());
        if(image != null){
            model.getTitleImage().setBitmap(image);
        } else {
            model.getTitleImage().repairBitmap();
        }

        TextView txtName = (TextView) findViewById(R.id.lblNameModel);
        txtName.setText(model.getName());

        String marqueName = (String)getIntent().getStringExtra(Marque.KEY);
        TextView txtMarq = (TextView) findViewById(R.id.lblModelMarq);
        txtMarq.setVisibility(TextView.VISIBLE);
        txtMarq.setText(getString(R.string.marq) + " " + marqueName);

        TextView txtYear = (TextView) findViewById(R.id.lblModelYear);
        txtYear.setVisibility(TextView.VISIBLE);
        txtYear.setText(getString(R.string.years) + " " + model.getProdDate().toString().substring(0,4) +
                " - " + AutoEncyclopedia.convertdate(model.getOutDate()));

        ImageView imgView = (ImageView) findViewById(R.id.imModel);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setImageBitmap(model.getTitleImage().getBitmap());
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullimage = new Intent(getApplicationContext(),FullScreenImageActivity.class);
                fullimage.putExtra(ImageItem.KEY,model.getTitleImage());
                fullimage.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(fullimage);
            }
        });


        if(!model.getTitleImage().isDownloaded()){
            GetImage task = new GetImage(this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model.getTitleImage());
        }

        GetModelInfo task = new GetModelInfo(this, getString(R.string.url_get_model_info));

        setProgressBarIndeterminateVisibility(true);
        task.execute(model);

        GetModelGeners task2 = new GetModelGeners(this, getString(R.string.url_get_model_geners));
        task2.execute(model);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_model_info);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnChange = (Button) findViewById(R.id.btnModelChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewModel);
                ListView lstModels = (ListView) findViewById(R.id.lstGenerations);
                if (scrollView.getVisibility() == ScrollView.VISIBLE) {
                    scrollView.setVisibility(ScrollView.INVISIBLE);
                    lstModels.setVisibility(ListView.VISIBLE);
                    btnChange.setText(getString(R.string.info_model));
                    lstModels.invalidateViews();
                } else {
                    scrollView.setVisibility(ScrollView.VISIBLE);
                    lstModels.setVisibility(ListView.INVISIBLE);
                    btnChange.setText(getString(R.string.all_geners));
                }
            }
        });

        getInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.model_info, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        if(isRefreshing){
            menu.findItem(R.id.action_model_refresh).setVisible(false);
            setProgressBarIndeterminateVisibility(true);
        } else {
            menu.findItem(R.id.action_model_refresh).setVisible(true);
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
            case R.id.action_model_refresh:
                item.setVisible(false);
                reset();
                getInfo();
                return true;
            case R.id.action_model_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishGetPic(ImageItem result) {
        ImageView imgView = (ImageView) findViewById(R.id.imModel);
        imgView.setImageBitmap(result.getBitmap());
    }

    @Override
    public void getModelInfoFinish(Model result) {
        if(result != null) {
            TextView txtDownloading = (TextView) findViewById(R.id.lblDownloadModel);
            txtDownloading.setVisibility(TextView.INVISIBLE);

            TextView txtYear = (TextView) findViewById(R.id.lblModelYear);
            txtYear.setVisibility(TextView.VISIBLE);
            txtYear.setText(getString(R.string.years) + " " + model.getProdDate().toString().substring(0,4) +
                    " - " + AutoEncyclopedia.convertdate(model.getOutDate()));

            if(model.getInfo() != null) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfoModel);
                txtInfo.setText(model.getInfo());
            }

            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewModel);
            scrollView.setVisibility(ScrollView.VISIBLE);

            Button btnChange = (Button) findViewById(R.id.btnModelChange);
            btnChange.setVisibility(Button.VISIBLE);

            setProgressBarIndeterminateVisibility(true);
        } else {
            Toast.makeText(this, getString(R.string.error_server),
                    Toast.LENGTH_SHORT).show();
            setProgressBarIndeterminateVisibility(false);
        }
    }

    public ArrayList<ImageItem> forGenerImages(){
        ArrayList<ImageItem> result = new ArrayList<ImageItem>();
        for(int i = 0; i < model.getGeners().size(); i++){
            model.getGeners().get(i).getTitleImage().setSize(250, 0);
            result.add(model.getGeners().get(i).getTitleImage());
        }
        return result;
    }

    @Override
    public void getGenerFinish(Model result) {
        try{
            if(result.getGeners() != null) {
                Button btnChange = (Button) findViewById(R.id.btnModelChange);
                btnChange.setVisibility(Button.VISIBLE);
                if (result.getGeners().size() == 1) {
                    btnChange.setText(getString(R.string.all_mod));
                    GetGenerInfo task = new GetGenerInfo(this,getString(R.string.url_get_gener_info));
                } else {
                    btnChange.setText(getString(R.string.all_geners));

                }
            }
        }catch (NullPointerException e){

        }
    }

    @Override
    public void getGenerInfoFinish(Generation result) {

    }

    @Override
    public void finishArrayImage() {
        isRefreshing = false;
        invalidateOptionsMenu();
    }

    public AutoEncyclopedia getAutoEncyclopedia(){
        return (AutoEncyclopedia)getApplication();
    }
*/
}
