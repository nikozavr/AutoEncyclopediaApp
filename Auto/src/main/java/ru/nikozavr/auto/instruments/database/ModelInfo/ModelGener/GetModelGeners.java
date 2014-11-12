package ru.nikozavr.auto.instruments.database.ModelInfo.ModelGener;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.instruments.database.JSONParser;
import ru.nikozavr.auto.model.Generation;
import ru.nikozavr.auto.model.ImageItem;
import ru.nikozavr.auto.model.Model;

public class GetModelGeners extends AsyncTask<Model, Void, Model> {
    public AsyncModelGeners delegate = null;
    private String Url;

    JSONParser jsonParser = new JSONParser();

    public GetModelGeners(AsyncModelGeners del, String url){
        super();
        delegate = del;
        Url = url;
    }

    @Override
    protected Model doInBackground(Model... par) {
        int success;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AutoEncyclopedia.TAG_ID_MODEL, par[0].getIdModel().toString()));

            JSONObject json = jsonParser.makeHttpRequest(Url, "POST", params);

            success = json.getInt(AutoEncyclopedia.TAG_SUCCESS);
            if (success == 1) {
                int count = json.getInt(AutoEncyclopedia.TAG_COUNT);

                JSONArray arr = json.getJSONArray(AutoEncyclopedia.TAG_GENERS);
                JSONObject temp;

                par[0].resetGenerations();

                for (int i = 0; i < count; i++) {
                    URL url;
                    temp = arr.getJSONObject(i);

                    try {
                        url = new URL(temp.getString(AutoEncyclopedia.TAG_PIC_URL));
                    } catch (MalformedURLException e) {
                        continue;
                    }

                    par[0].addGeneration(new Generation(temp.getInt(AutoEncyclopedia.TAG_ID_GENER), temp.getString(AutoEncyclopedia.TAG_NAME),
                            temp.getString(AutoEncyclopedia.TAG_PROD_DATE), temp.getString(AutoEncyclopedia.TAG_OUT_DATE),
                            new ImageItem(temp.getInt(AutoEncyclopedia.TAG_ID_PIC_AUTO), url, temp.getString(AutoEncyclopedia.TAG_NAME))));
                }
            } else return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return par[0];
    }

    @Override
    protected void onPostExecute(Model result) {
        delegate.getGenerFinish(result);
    }
}
