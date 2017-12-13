package com.example.nss.vocolrecorder.util.HtttpManagement.Auth;

/**
 * Created by NSS on 8/30/2017.
 */

public class Token implements Authentication {

    private String  token;

    public Token(String token){

        this.token =token;

    }


    @Override
    public void SetValue(Object value) {

        this.token =(String)value;

    }

    @Override
    public Object GetValue() {

        return this.token;
    }


}
