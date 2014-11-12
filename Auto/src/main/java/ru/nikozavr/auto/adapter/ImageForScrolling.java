package ru.nikozavr.auto.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

import ru.nikozavr.auto.model.Model;

/**
 * Created by nikozavr on 4/1/14.
 */
public class ImageForScrolling extends ImageView {
    private Model _model;

    public ImageForScrolling(Context context, Model model){
        super(context);
        _model = model;
        setImageBitmap(_model.getTitleImage().getBitmap());
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        setImageBitmap(_model.getTitleImage().getBitmap());
        super.onDraw(canvas);
    }

    public Model getModel() { return _model; }
}
