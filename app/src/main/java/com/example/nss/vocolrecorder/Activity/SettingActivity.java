package com.example.nss.vocolrecorder.Activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.nss.vocolrecorder.Fragment.SettingFragment;
import com.example.nss.vocolrecorder.R;

public class SettingActivity extends android.support.v7.app.AppCompatActivity  {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar= getSupportActionBar();

        if(toolbar != null){

            actionBar.setTitle("Settings");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        getFragmentManager().beginTransaction().replace(R.id.container,new SettingFragment()).commit();
    }
}
