package ru.nikozavr.auto.model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by nikozavr on 3/24/14.
 */
public class Generation {
    public static final String KEY = "generation";

    private Integer _id_gener = 0;
    private String _name = null;
    private String _info = null;
    private Date _date_prod = null;
    private Date _date_out = null;
    private ImageItem _title_image = null;

    private ArrayList<ImageItem> _images = null;
    private TechChar _tech_char;

    public Generation(Integer id_gener, String name, String date_prod, String date_out, ImageItem title_image){
        _id_gener = id_gener;
        _name = name;
        _title_image = title_image;
        _date_prod = Date.valueOf(date_prod);

        if(!date_out.equals("0000-00-00")) {
            _date_out = Date.valueOf(date_out);
        }
    }

    public Integer getIdGener() { return _id_gener; }

    public ImageItem getTitleImage() { return _title_image; }

    public String getName() { return _name; }

    public Date getProdDate() { return _date_prod; }

    public Date getOutDate() { return _date_prod; }
}
