package ru.nikozavr.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by nikozavr on 3/24/14.
 */
public class Model implements Parcelable{

    public static final String KEY = "model";

    private Integer _id_model = 0;
    private String _name = null;
    private String _info = null;
    private Date _date_prod = null;
    private Date _date_out = null;
    private ImageItem _title_image = null;

    private ArrayList<ImageItem> _images = null;
    private ArrayList<Generation> _generations = null;

    public Model(Integer id_model, String name, ImageItem title_image){
        _id_model = id_model;
        _name = name;
        _title_image = title_image;
    }

    public Model(Integer id_model, String name, String date_prod, String date_out, ImageItem title_image){
        _id_model = id_model;
        _name = name;
        _title_image = title_image;
        _date_prod = Date.valueOf(date_prod);

        if(!date_out.equals("0000-00-00")) {
            _date_out = Date.valueOf(date_out);
        }
    }

    public void setModel(String date_prod, String date_out, String info){
        _date_prod = Date.valueOf(date_prod);

        if(!date_out.equals("0000-00-00")) {
            _date_out = Date.valueOf(date_out);
        }

        _info = info;
    }

    public Integer getIdModel() { return _id_model; }

    public ImageItem getTitleImage() { return _title_image; }

    public String getName() { return _name; }

    public Date getProdDate() { return _date_prod; }

    public Date getOutDate() { return _date_out; }

    public String getInfo() { return _info; }

    public ArrayList<Generation> getGeners() { return _generations; }

    public void addGeneration(Generation generation) { _generations.add(generation); };

    public void resetGenerations() { _generations = new ArrayList<Generation>(); }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_id_model);
        out.writeString(_name);
        out.writeString(_info);
        out.writeValue(_date_prod);
        out.writeValue(_date_out);
        out.writeParcelable(_title_image, 0);
    }

    public static final Parcelable.Creator<Model> CREATOR
            = new Parcelable.Creator<Model>() {
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    private Model(Parcel in) {
        ClassLoader cl = Model.class.getClassLoader();
        _id_model = in.readInt();
        _name = in.readString();
        _info = in.readString();

        _date_prod = (Date)in.readValue(cl);
        _date_out = (Date)in.readValue(cl);
        _title_image = (ImageItem)in.readParcelable(cl);
    }

}
