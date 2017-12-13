package com.example.nss.vocolrecorder.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nss.vocolrecorder.Fragment.VocalSellectFragment;
import com.example.nss.vocolrecorder.R;

import java.util.HashSet;
import java.util.Set;

public class VocalSellectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_setting);

        VocalSellectFragment vocalSellectFragment = new VocalSellectFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,vocalSellectFragment).commit();

    }


}
