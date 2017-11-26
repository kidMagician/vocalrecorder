package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by NSS on 8/30/2017.
 */

public class HRegisterConnecter implements HttpConnecter {

    private HttpURLConnection httpURLConnection;

    public Boolean RequestPost(String urlStr, ContentValues params){

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

            URL url = new URL(urlStr);
            Log.d("TAG", urlStr);
            httpURLConnection=(HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setRequestProperty("Authentication","token "+token);
//            httpURLConnection.setRequestProperty("","");

            String strParams = sbParams.toString();

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


        return false;
    }
}
