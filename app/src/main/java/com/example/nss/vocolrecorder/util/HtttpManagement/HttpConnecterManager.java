package com.example.nss.vocolrecorder.util.HtttpManagement;

import android.app.Application;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;
import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Token;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HFindAcounterConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HLoginConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HRegisterConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HVoiceUploadConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;

/**
 * Created by NSS on 8/30/2017.
 */

public class HttpConnecterManager extends Application {

    private Authentication token;

    public static final int LOGIN=1;
    public static final int REGISTER=2;
    public static final int FINDPASS=3;
    public static final int VOICEUPLOAD=4;

    public HttpConnecterManager(){

        token =new Token();
    }

    public HttpConnecter GetHttpConnecter(int type){

        switch (type){

            case LOGIN:

                return new HLoginConnecter(token);

            case REGISTER:

                return new HRegisterConnecter();

            case FINDPASS:

                return new HFindAcounterConnecter();
            case VOICEUPLOAD:

                return new HVoiceUploadConnecter(token);

        }

        return null;

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
