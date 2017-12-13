package com.example.nss.vocolrecorder.util.HtttpManagement;

import android.app.Application;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;
import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Token;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HJsonConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HLoginConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HPostConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HVoiceUploadConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;

/**
 * Created by NSS on 8/30/2017.
 */


public class HttpConnecterManager extends Application {

    private Authentication token;

    public static final int LOGIN=1;
    public static final int VOICEUPLOAD=2;
    public static final int JSON=3;
    public static final int POST=4;

    public HttpConnecterManager(){

        token =new Token("");
    }

    public HttpConnecter GetHttpConnecter(int type){

        switch (type){

            case LOGIN:

                return new HLoginConnecter(getApplicationContext() , token);

            case VOICEUPLOAD:

                return new HVoiceUploadConnecter(token);

            case JSON:
                return new HJsonConnecter(token);

            case POST:
                return new HPostConnecter(token);
        }

        return null;

    }

    public void setAuth(Authentication auth){

        this.token =auth;

    }


    public void DestroySession(){

        token.SetValue("");

    }

    public static class Auth{

        static public final String email ="email";
        static public final String username="username";
        static public final String pass="password";

    }



}
