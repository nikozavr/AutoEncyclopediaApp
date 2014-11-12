package ru.nikozavr.auto.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.LruCache;

import java.net.URL;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.R;

/**
 * Created by nikozavr on 3/1/14.
 */
public class ImageItem implements Parcelable{


    public static final String KEY = "image";

    public enum Type { MARQ, AUTO };

    private Type _type;
    private Boolean _is_downloaded = false;
    private Integer _id_pic = 0;
    private URL _url = null;
    private String _title = null;
    private Bitmap _bitmap = null;

    private Integer _height = 0;
    private Integer _width = 0;

    public ImageItem(Type type, Integer id_pic, URL url, String title){
        _type = type;
        _is_downloaded = false;
        _id_pic = id_pic;
        _url = url;
        _title = title;
        _bitmap = BitmapFactory.decodeResource(AutoEncyclopedia.getAppContext().getResources(), R.drawable.ic_action_picture);
    }

    public ImageItem(Integer id_pic, URL url, String title){
        _type = Type.AUTO;
        _is_downloaded = false;
        _id_pic = id_pic;
        _url = url;
        _title = title;
        _bitmap = BitmapFactory.decodeResource(AutoEncyclopedia.getAppContext().getResources(), R.drawable.ic_action_picture);
    }

    public ImageItem(String title, URL url){
        _type = Type.AUTO;
        _is_downloaded = false;
        _url = url;
        _title = title;
        _bitmap = BitmapFactory.decodeResource(AutoEncyclopedia.getAppContext().getResources(), R.drawable.ic_action_picture);
    }

    public ImageItem(Type type, String title, URL url){
        _type = type;
        _is_downloaded = false;
        _url = url;
        _title = title;
        _bitmap = BitmapFactory.decodeResource(AutoEncyclopedia.getAppContext().getResources(), R.drawable.ic_action_picture);
    }

    public String getKey() {
        if(_type == Type.MARQ)
            return _id_pic.toString()+"M";
        else
            return _id_pic.toString()+"A";
    }

    public void repairBitmap() { _bitmap = BitmapFactory.decodeResource(AutoEncyclopedia.getAppContext().getResources(), R.drawable.ic_action_picture); }

    public Boolean isDownloaded() { return this._is_downloaded;}

    public Integer getIdPic() { return this._id_pic; }

    public URL getUrl() { return this._url; }

    public String getTitle() { return this._title; }

    public Bitmap getBitmap() {
        return _bitmap;
    }

    public Integer getHeight() { return _height; }

    public Integer getWidth() { return _width; }

    public Type getType() { return _type; }


    public void setDownloaded(Boolean downloaded) { this._is_downloaded = downloaded; }

    public void setBitmap(Bitmap image) {
        this._bitmap = image;
    }

    public void setSize(Integer height, Integer width) {
        _height = height;
        _width = width;
    }

    public void setType(Type type) { _type = type; }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(_type);
        out.writeValue(_is_downloaded);
        out.writeInt(_id_pic);
        out.writeValue(_url);
        out.writeString(_title);
    }

    public static final Parcelable.Creator<ImageItem> CREATOR
            = new Parcelable.Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    private ImageItem(Parcel in) {
        ClassLoader cl = ImageItem.class.getClassLoader();
        _type = (Type)in.readValue(cl);
        _is_downloaded = (Boolean)in.readValue(cl);
        _id_pic = in.readInt();
        _url = (URL)in.readValue(cl);
        _title = in.readString();
    }


}
