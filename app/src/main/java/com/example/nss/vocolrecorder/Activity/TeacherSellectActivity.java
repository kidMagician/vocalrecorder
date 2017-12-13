package com.example.nss.vocolrecorder.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nss.vocolrecorder.Fragment.TeacherSelectFragment;
import com.example.nss.vocolrecorder.R;

public class TeacherSellectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sellect);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,TeacherSelectFragment.newInstance()).commit();
    }
}
