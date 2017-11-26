package com.example.nss.vocolrecorder.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;;

import com.example.nss.vocolrecorder.Fragment.FileViwerFragment;
import com.example.nss.vocolrecorder.Fragment.RecoderFragment;
import com.example.nss.vocolrecorder.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs= (PagerSlidingTabStrip) findViewById(R.id.tabs);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);

        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);

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
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return RecoderFragment.newInstance(position);
                }
                case 1:{
                    return FileViwerFragment.newInstance(position);
                }
                default:

                    return null;
            }
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
