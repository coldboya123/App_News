package com.example.App_News;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNews extends AsyncTask<String, Void, String> {
    Context context;
    IgetNews igetNews;
    ProgressDialog progress;

    public GetNews(Context context, IgetNews igetNews) {
        this.context = context;
        this.igetNews = igetNews;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        List<Page> list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vidoco.vn/webservice/getnews.php",
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Page page = new Page(object.getString("Id"), object.getString("Title"), object.getString("Date"), object.getString("Hometext"), object.getString("Homeimgfile"), object.getString("Hitstotal"));
                            list.add(page);
                        }
                        igetNews.OngetNews(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Toast.makeText(context, "Cant get news!" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("num", strings[0]);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progress.dismiss();
    }
}
