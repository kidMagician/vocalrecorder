package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;
import android.util.Log;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by NSS on 2017-12-06.
 */

public class HPostConnecter extends AbstarctHttpConnecter implements  HttpConnecter {

    private HttpURLConnection httpURLConnection;
    private Authentication token;

    public HPostConnecter(Authentication token){

        this.token = token;

    }

    @Override
    public Object Request(String urlStr, ContentValues params) {

            try{

                URL url = new URL(urlStr);
                Log.d("TAG", urlStr);
                httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Authentication","token "+token);

                String strParams = ConvertToString(params);

                System.out.println(strParams);

                OutputStream os = httpURLConnection.getOutputStream();

                os.write(strParams.getBytes("UTF-8"));
                os.flush();
                os.close();

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED){

                    return true;
                }
                else{

                    return false;
                }


            }catch(ProtocolException e){

                e.printStackTrace();

            }catch (MalformedURLException e){

                e.printStackTrace();

            }catch (IOException e){

                e.printStackTrace();

            }finally {

                if(httpURLConnection!=null){

                    httpURLConnection.disconnect();
                }
            }

        return null;
    }
}
