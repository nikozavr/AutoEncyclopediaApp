package ru.nikozavr.auto;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ru.nikozavr.auto.instruments.database.Images.Single.AsyncGetImage;
import ru.nikozavr.auto.instruments.database.Images.Single.GetImage;
import ru.nikozavr.auto.model.ImageItem;

public class FullScreenImageActivity extends Activity implements AsyncGetImage{

    private ImageItem image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        image = (ImageItem)getIntent().getParcelableExtra(ImageItem.KEY);
        if(image != null) {
            getActionBar().setTitle(image.getTitle());
            if(!image.isDownloaded()){
                GetImage task = new GetImage(this);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, image);
            } else {
                ImageView imgView = (ImageView) findViewById(R.id.imageFullScreen);
                ProgressBar progress = (ProgressBar) findViewById(R.id.progress);

                imgView.setImageBitmap(image.getBitmap());
                progress.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marq_info, menu);
        //this.menu = menu;
        return true;
    }

    public void onBackPressed(){
        finish();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishGetPic(ImageItem result) {
        ImageView imgView = (ImageView) findViewById(R.id.imageFullScreen);
        imgView.setImageBitmap(result.getBitmap());
    }

    public AutoEncyclopedia getAutoEncyclopedia(){
        return (AutoEncyclopedia)getApplication();
    }
}
