package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;
import android.os.Environment;
import android.util.Log;

import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Authentication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by NSS on 9/11/2017.
 */

public class HVoiceUploadConnecter implements HttpConnecter {

    private Authentication token;

    private final String TAG ="HVoiceUploadConnecter";

    public HVoiceUploadConnecter(Authentication auth){

        token =auth;

    }

    @Override
    public Object Request(String urlStr, ContentValues  params) {

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        String parmName = (String)params.get("paramName");
        String filePath = (String)params.get("filePath");
        String fileName = (String)params.get("fileName");
        try {

            FileInputStream fileInputStream =new FileInputStream(filePath);

            URL url = new URL(urlStr);

            HttpURLConnection conn =(HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept","text/html, application/xhtml+xml, image/jxr, */*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "ko, en-US; q=0.7, en; q=0.3");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            conn.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("Authentication","token "+token);

            DataOutputStream dataOutputStream = new DataOutputStream( conn.getOutputStream());

            dataOutputStream.writeBytes(twoHyphens + boundary + crlf);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    parmName + "\"; filename=\"" +
                    fileName + "\"" + crlf);
            dataOutputStream.writeBytes(crlf);

            int bufferSize, bytesvailable, bytesRead;

            int maxBufferSize = 1*1024*1024;;

            bytesvailable = fileInputStream.available();

            bufferSize = Math.min(bytesvailable,maxBufferSize);

            byte[] buffer =new byte[bufferSize];

            bytesRead =fileInputStream.read(buffer,0,bufferSize);

            while (bytesRead>0){

                dataOutputStream.write(buffer,0,bufferSize);

                bytesvailable = fileInputStream.available();

                bufferSize = Math.min(bytesvailable,maxBufferSize);

                buffer =new byte[bufferSize];

                bytesRead =fileInputStream.read(buffer,0,bufferSize);

            }

            fileInputStream.close();

            dataOutputStream.writeBytes(crlf);

            for(Map.Entry<String,Object> parameter: params.valueSet()) {

                if(parameter.getKey() !="paramName" &&parameter.getKey() !="filePath"&&parameter.getKey() !="fileName"){

                    dataOutputStream.writeBytes(twoHyphens + boundary + crlf);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                            parameter.getKey() + "\"" + crlf);
                    dataOutputStream.writeBytes("Content-Type: text/plain" + crlf);
                    dataOutputStream.writeBytes(crlf);
                    dataOutputStream.writeBytes(parameter.getValue().toString());

                    dataOutputStream.writeBytes(crlf);

                }


            }

            dataOutputStream.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);

            dataOutputStream.flush();
            dataOutputStream.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line;
                String responsStr = "";

                while ((line = br.readLine()) != null) {
                    responsStr += line;
                }

                Log.d(TAG,responsStr);

            }

        }catch (MalformedURLException e){

            e.printStackTrace();

        }catch (IOException e){

            e.printStackTrace();

        }

        return null;
    }

}
