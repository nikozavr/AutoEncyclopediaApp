package ru.nikozavr.auto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.R;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Model;

/**
 * Created by nikozavr on 4/1/14.
 */
public class ListModelAdapter extends ArrayAdapter<Model>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Model> data = new ArrayList<Model>();

    public ListModelAdapter(Context context, int layoutResourceId,
                          ArrayList<Model> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.Date = (TextView) row.findViewById(R.id.txtYear);
            holder.Name = (TextView) row.findViewById(R.id.txtName);
            holder.image = (ImageView) row.findViewById(R.id.imageModel);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position).getTitleImage();
        holder.Name.setText(data.get(position).getName());
        holder.Date.setText(data.get(position).getProdDate().toString().substring(0,4) + " - " + AutoEncyclopedia.convertdate(data.get(position).getOutDate()));
        holder.image.setImageBitmap(item.getBitmap());
        return row;
    }

    static class ViewHolder {
        TextView Name;
        TextView Date;
        ImageView image;
    }
}
