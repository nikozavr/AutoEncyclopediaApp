package ru.nikozavr.auto.instruments.database.ModelInfo;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.instruments.database.JSONParser;
import ru.nikozavr.auto.model.Model;

/**
 * Created by nikozavr on 4/10/14.
 */
public class GetModelInfo extends AsyncTask<Model, Void, Model> {
    private AsyncModelInfo delegate = null;

    private String Url;

    JSONParser jsonParser = new JSONParser();

    public GetModelInfo(AsyncModelInfo del, String url){
        super();
        delegate = del;
        Url = url;
    }

    @Override
    protected Model doInBackground(Model... par) {
        String[] result = new String[5];
        int success;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AutoEncyclopedia.TAG_ID_MODEL, par[0].getIdModel().toString()));

            JSONObject json = jsonParser.makeHttpRequest(Url, "POST", params);

            success = json.getInt(AutoEncyclopedia.TAG_SUCCESS);

            if(success == 1) {
                JSONObject temp = json.getJSONObject(AutoEncyclopedia.TAG_MODEL_INFO);

                result[2] = temp.getString(AutoEncyclopedia.TAG_INFO);
                result[0] = temp.getString(AutoEncyclopedia.TAG_PROD_DATE);
                result[1] = temp.getString(AutoEncyclopedia.TAG_OUT_DATE);
            } else return null;

            par[0].setModel(result[0],result[1],result[2]);
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
    protected void onPostExecute(Model result) {
        delegate.getModelInfoFinish(result);
    }
}
