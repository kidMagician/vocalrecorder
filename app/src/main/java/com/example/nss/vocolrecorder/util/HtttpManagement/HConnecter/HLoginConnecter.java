package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;
import android.util.Log;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Created by NSS on 8/30/2017.
 */

public class HLoginConnecter implements HttpConnecter {

    private HttpURLConnection httpURLConnection;

    private Authentication token;

    public HLoginConnecter(Authentication token){

        this.token = token;
    }


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


            OutputStream os = httpURLConnection.getOutputStream();

            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8")) ;

                String line;
                String jsonToken="";

                while ( (line=br.readLine())!=null){
                    jsonToken +=  line;
                }

                if(jsonToken !=""){
                    try{
                        JSONParser jsonParser =new JSONParser();

                        JSONObject tokenObject = (JSONObject) jsonParser.parse(jsonToken);

                        String strToken = (String)tokenObject.get("token");

                        token.SetValue(strToken);


                    }catch (ParseException e){

                        e.printStackTrace();

                    }

                }else{
                    return false;
                }

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
