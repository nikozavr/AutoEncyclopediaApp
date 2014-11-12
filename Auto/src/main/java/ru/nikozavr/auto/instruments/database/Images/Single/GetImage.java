package ru.nikozavr.auto.instruments.database.Images.Single;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.model.ImageItem;

/**
 * Created by nikozavr on 3/2/14.
 */
public class GetImage extends AsyncTask<ImageItem, Void, ImageItem>{
    private AsyncGetImage delegate=null;
    private AsyncGetImage parent=null;

    public GetImage(AsyncGetImage del){
        super();
        delegate = del;
    }

    public GetImage(AsyncGetImage del, AsyncGetImage par){
        super();
        delegate = del;
        parent = par;

    }

    private int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected ImageItem doInBackground(ImageItem... par){
        Bitmap image = null;
        Integer Height = par[0].getHeight();
        Integer Width = par[0].getWidth();
        try{
            if(Height != 0 || Width != 0){
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(par[0].getUrl().openConnection().getInputStream(),
                        null, options);

                options.inSampleSize = calculateInSampleSize(options, Width, Height);

                options.inJustDecodeBounds = false;
                image = BitmapFactory.decodeStream(par[0].getUrl().openConnection().getInputStream(),
                        null, options);
            } else {
                image = BitmapFactory.decodeStream(par[0].getUrl().openConnection().getInputStream());
            }
            par[0].setBitmap(image);
            par[0].setDownloaded(true);
        } catch (IOException e) {

        }
        return par[0];
    }

    @Override
    protected void onPostExecute(ImageItem result){
        delegate.finishGetPic(result);
        if(parent != null)
            parent.finishGetPic(result);
    }
}
