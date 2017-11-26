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

/**
 * Created by NSS on 8/9/2017.
 */

public class HJsonConnecter implements HttpConnecter {

    private HttpURLConnection httpURLConnection;
    private String token;

    public HJsonConnecter(String token){

        this.token = token;
    }


    public String RequestPost(String urlStr, ContentValues params){

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
            httpURLConnection.setRequestProperty("Authentication","token "+token);

            String strParams = sbParams.toString();

            System.out.println(strParams);

            OutputStream os = httpURLConnection.getOutputStream();

            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED){

                BufferedReader br = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8")) ;

                String line;
                String page="";

                while ( (line=br.readLine())!=null){
                    page +=  line;
                }

                return page;
            }
            else{

                return null;
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

    public String RequestGet(String urlStr){

        try{
            URL url = new URL(urlStr);

            httpURLConnection =(HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

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
