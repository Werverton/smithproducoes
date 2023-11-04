package com.example.smithproducoes.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smithproducoes.model.Video;
import com.example.smithproducoes.utils.GettingIdFromUrlWithRegex;
import com.example.smithproducoes.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetJsonData {
    SharedPreferences sharedPreferences;
    Video video;
    List<Video> listDeUrls = new ArrayList<>();
    SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();
    private Context context;
    private static final String SHARED_PREFS_KEY = "MyPrefs";
    private static final String URL_KEY = "url_key";
    private static final String VERSION_KEY = "version_key";
    private static final String TAG = "GetJsonData";
    public GetJsonData(Context context) {
        this.context = context;
    }

    public void getJsonData() {

        String URL = sharedPreferences.getString(URL_KEY, "");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {

            try {
                sharedPreferenceManager.saveUrlToSharedPreferences(VERSION_KEY, response.getString("version"));
                Log.i(TAG,"Version : "+ response.getString("version"));
                JSONArray videos = response.getJSONArray("videos");

                for (int i = 0; i < videos.length(); i++) {
                    JSONObject videoObject = videos.getJSONObject(i);
                    video = new Video(
                            videoObject.getString("url"),
                            //videoObject.getString("fileId"),
                            videoObject.getString("name"));
                    listDeUrls.add(video);

                    Log.i(TAG,"Video objeto: "+video.toString());

                }

                for (Video video1 : listDeUrls){
                    video1.setFileId(GettingIdFromUrlWithRegex.gettingId(video1.getUrl()));
                }

                //downloadVideo(listDeUrls.get(downloadCount).getFileId(), listDeUrls.get(downloadCount).getName());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.d(TAG,"onErrorResponse"+error.getMessage()));
        requestQueue.add(jsonObjectRequest);
    }
}
