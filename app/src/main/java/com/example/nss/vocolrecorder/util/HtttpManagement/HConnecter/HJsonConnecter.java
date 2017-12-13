package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;

import android.content.ContentValues;
import android.util.Log;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;

/**
 * Created by NSS on 8/9/2017.
 */

public class HJsonConnecter extends AbstarctHttpConnecter implements HttpConnecter {

    private HttpURLConnection httpURLConnection;
    private Authentication token;

    public HJsonConnecter(Authentication token){

        this.token = token;

    }


    public String Request(String urlStr,ContentValues params){

        try{

            String strParams = ConvertToString(params);

            urlStr += "?"+strParams.replace(" ","");

            URL url = new URL(urlStr);

            httpURLConnection =(HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authentication","token "+token);

            if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){

                return null;
            }

            BufferedReader br = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8")) ;

            String line;
            String page="";

            while ( (line=br.readLine())!=null){
                page +=  line;
            }

            return page;

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
