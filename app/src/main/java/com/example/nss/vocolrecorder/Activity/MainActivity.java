package com.example.nss.vocolrecorder.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

;

import com.example.nss.vocolrecorder.Fragment.FeedBackFragment;
import com.example.nss.vocolrecorder.Fragment.HomeFragment;
import com.example.nss.vocolrecorder.Fragment.ListeningFragment;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.util.HtttpManagement.Auth.Token;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private ImageButton btn_record;
    private ImageButton btn_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home =(ImageButton) findViewById(R.id.btn_home);
        btn_record=(ImageButton) findViewById(R.id.btn_record);
        btn_alert=(ImageButton) findViewById(R.id.btn_alert);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateHomefragment();
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,RecorderActivity.class);
                startActivity(i);
            }
        });

        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateAlertFragment();
            }
        });
        authenficate();
        setupToolbar();
        setupView();

    }

    private void authenficate(){
        String tokenStr =MySharedPreference.getPrefToken(getApplicationContext());

        Token token =new Token(tokenStr);

        HttpConnecterManager httpConnecterManager= (HttpConnecterManager)getApplicationContext();
        httpConnecterManager.setAuth(token);


    }


    private void setupView(){

        Fragment homeFragment = HomeFragment.newInstance();

        getFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

    }


    private void activateHomefragment(){

        Fragment homeFragment = HomeFragment.newInstance();

        getFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();



    }

    private void activateAlertFragment(){

//        Fragment alertFragment = FeedBackFragment.newInstance();
//
//        getFragmentManager().beginTransaction().replace(R.id.container,alertFragment).commit();
        android.support.v4.app.Fragment alertFragment = ListeningFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,alertFragment).commit();


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
