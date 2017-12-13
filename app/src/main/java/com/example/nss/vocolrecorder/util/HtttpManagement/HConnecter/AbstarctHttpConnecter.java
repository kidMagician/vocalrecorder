package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;

import java.util.Map;

/**
 * Created by NSS on 8/30/2017.
 */

public class AbstarctHttpConnecter {

    public String ConvertToString(ContentValues params){
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

        return sbParams.toString();

    }



}
