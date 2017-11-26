package com.example.nss.vocolrecorder.etc;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

/**
 * Created by NSS on 8/19/2017.
 */

public class NetworkChecker {

    private ConnectivityManager connMng;

    public NetworkChecker(Context cont){

        Context context =cont;
        connMng =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    public boolean CheckConnected()
    {
        NetworkInfo netInfo = connMng.getActiveNetworkInfo();

        if(netInfo.isConnected()){

            return true;

        }else{

            return false;
        }

    }

}
