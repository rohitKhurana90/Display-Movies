package com.movie.tmdb.moviedisplay.server;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rohit.khurana on 6/4/2017.
 */

public class MDSettingsManager {


    public static final String APP_SETTINGS = "Application_Settings";
    public static final String REQUEST_TOKEN = "request_token";
    public static final String SESSION_ID = "session_id";
    public static final String AUTHENTICATED = "authenticated";
    public static final String TOKEN_EXPIRY = "token_expiry_time";

    private MDSettingsManager(){}

    private static MDSettingsManager mdSettingsManager;

    public static synchronized  MDSettingsManager getInstance(){
        if(mdSettingsManager==null){
            mdSettingsManager = new MDSettingsManager();
        }
        return mdSettingsManager;
    }

    public SharedPreferences getSharedPreferences(Context context,String name){
        SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return preferences;
    }

    public String getStringValue(SharedPreferences preferences,String key){
        return preferences.getString(key,null);
    }

    public boolean getBooleanValue(SharedPreferences preferences,String key){
        return preferences.getBoolean(key,false);
    }
}
