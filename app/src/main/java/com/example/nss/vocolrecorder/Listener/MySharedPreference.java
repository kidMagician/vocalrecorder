package com.example.nss.vocolrecorder.Listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by NSS on 2017-11-15.
 */

public class MySharedPreference {

    private static String PREF_VOCAL_URL = "pref_vocal_url";
    private static String PREF_VOCAL_NAME ="pref_vocal_name";

    public static void setPrefVocalUrl(Context context, String vocalUrl){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_VOCAL_URL,vocalUrl);
        editor.commit();
    }

    public static String getPrefVocalUrl(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_VOCAL_URL,"None");
    }

    public static void setPrefVocalName(Context context, String vocalName){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_VOCAL_NAME,vocalName);
        editor.commit();

    }

    public static  String getPrefVocalName(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_VOCAL_NAME,"None");
    }

//    interface setOnChangedListener{
//
//        void OnVocalChanged();
//    }

}
