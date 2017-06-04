package com.movie.tmdb.moviedisplay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by rohit.khurana on 6/2/2017.
 */

public class MDUtils {

    public static HashMap<String, String> parseJsonAsMap(String jsonString) {
        HashMap<String, String> retMap = new HashMap<>();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                retMap.put(key, jsonObject.optString(key));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return retMap;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
