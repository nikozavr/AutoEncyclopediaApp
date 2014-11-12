package ru.nikozavr.auto.instruments.database.Images.Array;

import android.os.AsyncTask;

import java.util.ArrayList;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.instruments.database.Images.Single.AsyncGetImage;
import ru.nikozavr.auto.instruments.database.Images.Single.GetImage;
import ru.nikozavr.auto.model.ImageItem;

/**
 * Created by nikozavr on 2/27/14.
 */
public class GetArrayImage extends AsyncTask<ArrayList<ImageItem>, Void, Boolean> implements AsyncGetImage {
    private AsyncArrayImage delegate=null;
    private AsyncGetImage parent=null;

    public int Height = 0;
    public int Width = 0;

    private Integer finished = 0;

    public void finishGetPic(ImageItem result){
        finished++;
    }

    public AutoEncyclopedia getAutoEncyclopedia() { return parent.getAutoEncyclopedia(); }

    public GetArrayImage(AsyncArrayImage del){
        super();
        delegate = del;
    }

    public GetArrayImage(AsyncArrayImage del, AsyncGetImage par) {
        super();
        delegate = del;
        parent = par;
    }

    @Override
    protected Boolean doInBackground(ArrayList<ImageItem>... par) {
        Boolean result = false;
        int countPics = par[0].size();
        GetImage task;
        for(int i = 0; i < countPics; i++) {
            if(parent != null)
                task = new GetImage(this, parent);
            else
                task = new GetImage(this);
            task.executeOnExecutor(THREAD_POOL_EXECUTOR, par[0].get(i));
        }
        while(true) {
            if(countPics == finished)
                break;
            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result){
        Height = 0;
        Width = 0;
        delegate.finishArrayImage();
    }
}
