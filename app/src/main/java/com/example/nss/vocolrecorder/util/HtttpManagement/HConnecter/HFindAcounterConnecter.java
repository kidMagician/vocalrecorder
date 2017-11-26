package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by NSS on 2017-10-26.
 */

public class HFindAcounterConnecter implements HttpConnecter {

    private static final String TAG ="HFindAcounterConnecter";

    private HttpURLConnection httpURLConnection;

    @Override
    public Object RequestPost(String urlStr, ContentValues params) {

        StringBuffer sbParams = new StringBuffer();

        if(params ==null){

            sbParams.append("");

        }else{

            boolean isAnd = false;

            String key;
            String value;

            for(Map.Entry<String,Object> parameter: params.valueSet()){

                key = parameter.getKey();
                value= parameter.getValue().toString();

                if(isAnd){
                    sbParams.append("&");
                }

                sbParams.append(key).append("=").append(value);

                if(!isAnd){
                    if(params.size()>=2){

                        isAnd=true;
                    }

                }
            }

        }

        try{

            URL url =new URL(urlStr);
            httpURLConnection  =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            OutputStream os = httpURLConnection.getOutputStream();
            String strParams = sbParams.toString();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){

                return true;
            }

        }catch (MalformedURLException e){

            e.printStackTrace();
            Log.d(TAG,e.toString());

        }catch (IOException e){

            e.printStackTrace();
            Log.d(TAG,e.toString());
        }


        return false;
    }
}
