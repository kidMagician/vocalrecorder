package com.example.nss.vocolrecorder.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.R;


public class FeedBackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    public FeedBackFragment() {
        // Required empty public constructor
    }


    public static FeedBackFragment newInstance() {
        FeedBackFragment fragment = new FeedBackFragment();
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
        View v= inflater.inflate(R.layout.fragment_alert, container, false);

        setRecycleView();

        requestFeedBack();

        return v;
    }

    private void setRecycleView(){



    }

    private void requestFeedBack(){

    }





}
