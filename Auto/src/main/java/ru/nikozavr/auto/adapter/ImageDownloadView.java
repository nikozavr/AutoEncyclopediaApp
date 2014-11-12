package ru.nikozavr.auto.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.nikozavr.auto.R;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Model;

/**
 * Created by Никита on 14.04.2014.
 */
public class ImageDownloadView extends ImageView {
    private ImageItem _image;

    public ImageDownloadView(Context context, ImageItem image){
        super(context);
        _image = image;
        setImageBitmap(image.getBitmap());
        Integer height = Math.round(getResources().getDimension(R.dimen.photo_height));
        Integer width = Math.round(getResources().getDimension(R.dimen.photo_width));
        setLayoutParams(new ViewGroup.LayoutParams(width, height));
        setPadding(0,0,Math.round(getResources().getDimension(R.dimen.photo_between)),0);
    }

    @Override
    public void draw(Canvas canvas) {
        setImageBitmap(_image.getBitmap());
        setScaleType(ScaleType.FIT_CENTER);
        super.draw(canvas);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}
