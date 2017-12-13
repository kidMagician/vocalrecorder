package com.example.nss.vocolrecorder.Listener;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HPostConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HVoiceUploadConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by NSS on 2017-11-15.
 */

public class MySharedPreference {

    private static String PREF_VOCAL_URL = "pref_vocal_url";
    private static String PREF_VOCAL_NAME ="pref_vocal_name";
    private static String PREF_TEACHER_NICK = "pref_teacher_nick";
    private static String PREF_MY_NICK ="pref_my_nick";
    private static String PREF_TOKEN="pref_token";

    public static void setPrefVocalUrl(final Context context, final String vocalUrl){

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

    public static void setPrefTeacherNick(Context context, String teacher_nick){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TEACHER_NICK,teacher_nick);
        editor.commit();

    }

    public static  String getPrefTeacherNick(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_TEACHER_NICK,"testteacher");
    }

    public static String getPrefMyNick(Context context){

        SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_MY_NICK,"nss");

    }

    public static String getPrefToken(Context context){

        SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_TOKEN,"");
    }









//    interface setOnChangedListener{
//
//        void OnVocalChanged();
//    }

}
