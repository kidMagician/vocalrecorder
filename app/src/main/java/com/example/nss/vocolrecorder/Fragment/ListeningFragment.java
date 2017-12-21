package com.example.nss.vocolrecorder.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.nss.vocolrecorder.R;


public class ListeningFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyAdapter adapter;

    public ListeningFragment() {
        // Required empty public constructor
    }

    public static ListeningFragment newInstance() {
        ListeningFragment fragment = new ListeningFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_listening, container, false);

        pager = (ViewPager) v.findViewById(R.id.pager);

        adapter =new MyAdapter(getActivity().getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return v;
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
                    return FileViwerFragment.newInstance(position);
                }
                case 1:{
                    return FileViwerFragment.newInstance(position);
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
