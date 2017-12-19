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
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;
import com.example.nss.vocolrecorder.Fragment.FeedBackFragment;
import com.example.nss.vocolrecorder.Fragment.FileViwerFragment;
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
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    public final String FRAGMENT_HOME_TAG ="fragment_home";
    public final String FRAGMENT_PRACTICE_TAG ="fragment_practice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home =(ImageButton) findViewById(R.id.btn_home);
        btn_record=(ImageButton) findViewById(R.id.btn_record);
        btn_alert=(ImageButton) findViewById(R.id.btn_alert);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

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
        initPager();

    }

    private void initPager(){



        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);

    }


    private void authenficate(){
        String tokenStr =MySharedPreference.getPrefToken(getApplicationContext());

        Token token =new Token(tokenStr);

        HttpConnecterManager httpConnecterManager= (HttpConnecterManager)getApplicationContext();
        httpConnecterManager.setAuth(token);


    }


    private void setupView(){

        pager.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);

        android.support.v4.app.Fragment homeFragment = HomeFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment,FRAGMENT_HOME_TAG).addToBackStack(FRAGMENT_HOME_TAG).commitAllowingStateLoss();


        initButton();

        btn_home.setImageResource(R.drawable.mail_black_envelope_symbol2);
    }

    private void initButton(){

        btn_home.setImageResource(R.drawable.mail_black_envelope_symbol);
        btn_alert.setImageResource(R.drawable.mail_black_envelope_symbol);
        btn_record.setImageResource(R.drawable.mail_black_envelope_symbol);
    }


    private void activateHomefragment(){

        initButton();

        pager.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);

        android.support.v4.app.Fragment homeFragment = HomeFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment,FRAGMENT_HOME_TAG).addToBackStack(FRAGMENT_HOME_TAG).commitAllowingStateLoss();

        btn_home.setImageResource(R.drawable.mail_black_envelope_symbol2);

    }

    private void activateAlertFragment(){

        initButton();

        pager.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.VISIBLE);

//        android.support.v4.app.Fragment alertFragment = ListeningFragment.newInstance();
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,alertFragment,FRAGMENT_PRACTICE_TAG).addToBackStack(FRAGMENT_PRACTICE_TAG).commitAllowingStateLoss();

        btn_alert.setImageResource(R.drawable.mail_black_envelope_symbol2);
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

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings) };

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return FileViwerFragment.newInstance(position);
                }
                case 1:{
                    return FeedBackFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}
