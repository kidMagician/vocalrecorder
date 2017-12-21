package com.example.nss.vocolrecorder.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.R;


public class FeedBackPlayFragment extends DialogFragment {

    private MediaPlayer mediaPlayer;

    private Boolean isPlay;

    public FeedBackPlayFragment() {
        // Required empty public constructor
    }


    public static FeedBackPlayFragment newInstance() {
        FeedBackPlayFragment fragment = new FeedBackPlayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void play(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_feed_back_play, container, false);

        return  v;
    }




}
