package ru.nikozavr.auto.instruments.database.AllMarq;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.instruments.database.JSONParser;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Marque;

/**
 * Created by nikozavr on 2/27/14.
 */
public class GetAllMarq extends AsyncTask<Void, Void, ArrayList<Marque>> {

    private AsyncAllMarq delegate=null;

    private String Url;

    JSONParser jsonParser = new JSONParser();

    public GetAllMarq(AsyncAllMarq del, String url){
        super();
        delegate = del;
        Url = url;
    }

    @Override
    protected ArrayList<Marque> doInBackground(Void ... par) {
        ArrayList<Marque> result = new ArrayList<Marque>();
        int success;

        try {
            JSONObject json = jsonParser.getJSONFromUrl(Url);

            success = json.getInt(AutoEncyclopedia.TAG_SUCCESS);

            if(success == 1) {
                int count = json.getInt(AutoEncyclopedia.TAG_COUNT);

                JSONArray arr = json.getJSONArray(AutoEncyclopedia.TAG_MARQS);
                JSONObject temp;

                for(int i = 0; i < count; i++) {
                    URL url;
                    temp = arr.getJSONObject(i);
                    try{
                        url = new URL(temp.getString(AutoEncyclopedia.TAG_PIC_URL));
                    }catch (MalformedURLException e){
                        continue;
                    }

                    result.add(new Marque(temp.getInt(AutoEncyclopedia.TAG_ID_MARQ),temp.getString(AutoEncyclopedia.TAG_NAME),
                            new ImageItem(ImageItem.Type.MARQ, temp.getInt(AutoEncyclopedia.TAG_ID_PIC_MARQ),url,temp.getString(AutoEncyclopedia.TAG_NAME))));
                }
            } else return null;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Marque> result) {
        delegate.finishAllMarq(result);
    }
}
