package com.example.smithproducoes.utils;

import android.content.SharedPreferences;

public class SharedPreferenceManager {
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREFS_KEY = "MyPrefs";

    public void saveUrlToSharedPreferences(String key,String url) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, url);
        editor.apply();
    }
}
