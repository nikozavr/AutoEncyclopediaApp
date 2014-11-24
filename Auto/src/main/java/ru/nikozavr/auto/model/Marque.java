package ru.nikozavr.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by nikozavr on 3/24/14.
 */
public class Marque extends Object implements Parcelable{

    public static final String KEY = "marque";

    private Integer _id_marq = 0;
    private String _name = null;
    private String _country = null;
    private String _info = null;
    private Date _date_prod = null;
    private Date _date_out = null;
    private URL _contact = null;
    private ImageItem _image = null;
    private ArrayList<Model> _models = null;

    public Marque(String name) { _name = name; }

    public Marque(Integer id_marq, String name, ImageItem image) {
        _id_marq = id_marq;
        _name = name;
        _image = image;
    }

    public Marque(String name, ImageItem image) { _name = name; _image = image; }

    public void setMarque(String country, String info, String date_prod, String contact) {
        _country = country;
        _info = info;

        _date_prod = Date.valueOf(date_prod);

        try {
            _contact = new URL(contact);
        } catch (MalformedURLException e){

        }
    }

    public void setMarque(String country, String info, String date_prod, String date_out,
                          String contact) {
        _country = country;
        _info = info;

        _date_prod = Date.valueOf(date_prod);

        if(date_out != "0000-00-00") {
            _date_out = Date.valueOf(date_out);
        }
        try {
            _contact = new URL(contact);
        } catch (MalformedURLException e){

        }
    }

    public void setImage(ImageItem image) { _image = image; }

    public void setModels(String[] models) { }

    public void addModel(Model model) { _models.add(model); }


    public void resetModels() { _models = new ArrayList<Model>(); }


    public Integer getIdMarq() { return _id_marq; }

    public String getName() { return _name; }

    public String getCountry() { return _country; }

    public String getInfo() { return _info; }

    public Date getDateProd() { return _date_prod; }

    public Date getDateOut() { return _date_out; }

    public URL getContact() { return _contact; }

    public ImageItem getImage() { return _image; }

    public ArrayList<Model> getModels() { return _models; }


    public void reset() {
        _country = null;
        _info = null;
        _date_prod = null;
        _date_out = null;
        _contact = null;
        resetModels();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_id_marq);
        out.writeString(_name);
        out.writeString(_country);
        out.writeString(_info);
        out.writeValue(_date_prod);
        out.writeValue(_date_out);
        out.writeValue(_contact);
        out.writeParcelable(_image, 0);
    }

    public static final Parcelable.Creator<Marque> CREATOR
            = new Parcelable.Creator<Marque>() {
        public Marque createFromParcel(Parcel in) {
            return new Marque(in);
        }

        public Marque[] newArray(int size) {
            return new Marque[size];
        }
    };



    private Marque(Parcel in) {
        ClassLoader cl = Marque.class.getClassLoader();
        _id_marq = in.readInt();
        _name = in.readString();
        _country = in.readString();
        _info = in.readString();
        _date_prod = (Date)in.readValue(cl);
        _date_out = (Date)in.readValue(cl);
        _contact = (URL)in.readValue(cl);
        _image = (ImageItem)in.readParcelable(cl);
    }

    public String getID(){
        return ((Integer)hashCode()).toString();
    }
}