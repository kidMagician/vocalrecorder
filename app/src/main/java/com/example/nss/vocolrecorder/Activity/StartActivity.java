package com.example.nss.vocolrecorder.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nss.vocolrecorder.Listener.MySharedPreference;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i;

        if(MySharedPreference.getPrefToken(getApplicationContext())==""){

            i = new Intent(StartActivity.this,LoginActivity.class);

        }else{
            i= new Intent(StartActivity.this,MainActivity.class);

        }

        startActivity(i);

        finish();

    }
}
