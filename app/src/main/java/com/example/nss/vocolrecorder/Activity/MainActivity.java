package com.example.nss.vocolrecorder.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;
import com.example.nss.vocolrecorder.Fragment.FeedBackFragment;
import com.example.nss.vocolrecorder.Fragment.FileViwerFragment;
import com.example.nss.vocolrecorder.Fragment.HomeFragment;
import com.example.nss.vocolrecorder.Fragment.ListeningFragment;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Token;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import com.example.nss.vocolrecorder.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private ImageButton btn_record;
    private ImageButton btn_alert;

    private android.support.v4.app.Fragment fragment_home;
    private android.support.v4.app.Fragment fragment_feedback;

    public final String FRAGMENT_HOME_TAG ="fragment_home";
    public final String FRAGMENT_FEEDBACK_TAG ="fragment_feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home =(ImageButton) findViewById(R.id.btn_home);
        btn_record=(ImageButton) findViewById(R.id.btn_record);
        btn_alert=(ImageButton) findViewById(R.id.btn_alert);

        initButton();
        authenficate();
        setupToolbar();
        initView();

    }

    private void initButton(){

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHome();
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_record.setImageResource(R.drawable.record_click);

                Intent i = new Intent(MainActivity.this,RecorderActivity.class);

                startActivity(i);

                btn_record.setImageResource(R.drawable.record_unclick);

            }
        });

        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFeedback();
            }
        });


        initButtonicon();

        btn_home.setImageResource(R.drawable.home_click);
    }


    private void authenficate(){
        String tokenStr =MySharedPreference.getPrefToken(getApplicationContext());

        Token token =new Token(tokenStr);

        HttpConnecterManager httpConnecterManager= (HttpConnecterManager)getApplicationContext();
        httpConnecterManager.setAuth(token);

    }

    private void initView(){

        android.support.v4.app.Fragment homeFragment = HomeFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment,FRAGMENT_HOME_TAG).commit();

    }

    private void initButtonicon(){

        btn_home.setImageResource(R.drawable.home_unclick);
        btn_alert.setImageResource(R.drawable.alert_unclick);
        btn_record.setImageResource(R.drawable.record_unclick);
    }
    private void switchToHome(){


        android.support.v4.app.Fragment homeFragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment,FRAGMENT_HOME_TAG).commit();

        initButtonicon();

        btn_home.setImageResource(R.drawable.home_click);

    }

    private void switchToFeedback(){

        initButtonicon();

        btn_alert.setImageResource(R.drawable.alert_click);

        android.support.v4.app.Fragment feedBackFragment = FeedBackFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,feedBackFragment,FRAGMENT_FEEDBACK_TAG).commit();

    }

    private void setupToolbar(){
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);

        if(toolbar !=null){

            setSupportActionBar(toolbar);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.setting_bar:

                Intent intent =new Intent(MainActivity.this,SettingActivity.class);

                startActivity(intent);

                return true;
            case R.id.logout:

                MySharedPreference.clearToken(getApplicationContext());

                intent =new Intent(MainActivity.this,LoginActivity.class);

                startActivity(intent);

                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
