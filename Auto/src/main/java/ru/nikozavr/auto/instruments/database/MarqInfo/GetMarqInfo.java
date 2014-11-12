package ru.nikozavr.auto.instruments.database.MarqInfo;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.instruments.database.JSONParser;
import ru.nikozavr.auto.model.Marque;

/**
 * Created by nikozavr on 3/2/14.
 */
public class GetMarqInfo extends AsyncTask<Marque, Void, Marque> {
    private AsyncMarqInfo delegate = null;

    private String Url;

    JSONParser jsonParser = new JSONParser();

    public GetMarqInfo(AsyncMarqInfo del, String url){
        super();
        delegate = del;
        Url = url;
    }

    @Override
    protected Marque doInBackground(Marque... par) {
        String[] result = new String[5];
        int success;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AutoEncyclopedia.TAG_NAME, par[0].getName()));

            JSONObject json = jsonParser.makeHttpRequest(Url, "POST", params);

            success = json.getInt(AutoEncyclopedia.TAG_SUCCESS);

            if(success == 1) {
                JSONObject temp = json.getJSONObject(AutoEncyclopedia.TAG_MARQ_INFO);

                result[0] = temp.getString(AutoEncyclopedia.TAG_COUNTRY);
                result[1] = temp.getString(AutoEncyclopedia.TAG_INFO);
                result[2] = temp.getString(AutoEncyclopedia.TAG_PROD_DATE);
                result[3] = temp.getString(AutoEncyclopedia.TAG_OUT_DATE);
                result[4] = temp.getString(AutoEncyclopedia.TAG_WEB);
            } else return null;

            par[0].setMarque(result[0],result[1],result[2],result[3],result[4]);
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        return par[0];
    }
    @Override
    protected void onPostExecute(Marque result) {
        delegate.getMarqInfoFinish(result);
    }

}
