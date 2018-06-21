package com.example.nss.vocolrecorder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.icu.text.AlphabeticIndex;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.item.FeedBackItem;
import com.example.nss.vocolrecorder.service.HomeWorRecordService;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class FeedBackPlayFragment extends DialogFragment {

    private MediaPlayer mediaPlayer;

    private Boolean isPlay;
    private Boolean isRecording;

    private TextView txt_subject;
    private TextView txt_teacher_nick;
    private TextView txt_savedDate;
    private SeekBar seekBar;
    private TextView txt_current_progress;
    private TextView txt_file_length;
    private FloatingActionButton fab_play;
    private FloatingActionButton fab_record;
    private TextView txt_record;

    private static final String ARG_ITEM = "feedback_item";
    private static final String LOG_TAG ="feedBackPlayFragment";

    long minutes = 0;
    long seconds = 0;

    private Handler handler = new Handler();

    private FeedBackItem item;

    public FeedBackPlayFragment() {
        // Required empty public constructor
    }

    public static FeedBackPlayFragment newInstance(FeedBackItem item) {
        FeedBackPlayFragment fragment = new FeedBackPlayFragment();
        Bundle b =new Bundle();
        b.putParcelable(ARG_ITEM,item);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            item=getArguments().getParcelable(ARG_ITEM);

            long itemDuration = item.getLength();
            minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                    - TimeUnit.MINUTES.toSeconds(minutes);
        }
        isPlay =false;
        isRecording=false;
    }

    private void play(){

        if(!isPlay){
            if(mediaPlayer ==null) {
                startPlaying();
            }else {
                resumePlaying();
            }
            isPlay =true;

        }else{
            if(mediaPlayer !=null) {

                pausePlaying();
            }

            isPlay =false;

        }

    }

    private void record(){

        Intent i =new Intent(getActivity(),HomeWorRecordService.class);

        if(!isRecording){
            fab_record.setImageResource(R.drawable.ic_media_stop);
            txt_record.setText(getText(R.string.recording_button));
            getActivity().startService(i);

            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            isRecording =true;

        }else{
            fab_record.setImageResource(R.drawable.ic_mic_white_36dp);
            txt_record.setText(getText(R.string.before_recording_button));

            getActivity().stopService(i);

            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            isRecording =false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_feed_back_play, container, false);

        txt_subject=(TextView) v.findViewById(R.id.txt_subject);
        txt_savedDate=(TextView)v.findViewById(R.id.txt_savedDate);
        txt_teacher_nick =(TextView)v.findViewById(R.id.txt_teacher_nick);
        fab_play =(FloatingActionButton)v.findViewById(R.id.fab_play);
        txt_file_length =(TextView)v.findViewById(R.id.txt_file_length);
        txt_current_progress=(TextView)v.findViewById(R.id.txt_current_progress);
        seekBar =(SeekBar)v.findViewById(R.id.seekbar);
        fab_record=(FloatingActionButton)v.findViewById(R.id.fab_record);
        txt_record=(TextView)v.findViewById(R.id.txt_record);

        initButton();
        initView();
        initSeekBar();
        return  v;
    }
    private void initButton(){
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                play();
            }
        });

        fab_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer !=null){
            stopPlaying();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer !=null){
            stopPlaying();
        }
    }

    private void initSeekBar(){

        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.primary),getResources().getColor(R.color.primary));

        seekBar.getProgressDrawable().setColorFilter(colorFilter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer !=null && b) {

                    mediaPlayer.seekTo(i);
                    handler.removeCallbacks(runnable);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);

                    txt_current_progress.setText(String.format("%02d:%02d", minutes,seconds));


                    updateSeekbar();
                }else if(mediaPlayer==null&&b){
                        preparePlayingFromPoint(i);
                        updateSeekbar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer !=null){
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(seekBar.getProgress());
                    updateSeekbar();
                }
            }
        });
    }

    private void initView(){

        txt_teacher_nick.setText(item.getTeacher_nick());
        txt_savedDate.setText(item.getSavedDate());
        txt_subject.setText(item.getSubject());
        txt_file_length.setText(String.format("%02d:%02d",minutes,seconds));
    }


    private void startPlaying(){

        fab_play.setImageResource(R.drawable.ic_media_pause);
        mediaPlayer = new MediaPlayer();

        try {

//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(BuildConfig.SERVER_URL + item.getVoice_url());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "prepare() failed");
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });

        updateSeekbar();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void preparePlayingFromPoint(int progress){

        mediaPlayer = new MediaPlayer();

        try {

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(BuildConfig.SERVER_URL + item.getVoice_url());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(progress);


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlaying();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "prepare() failed");
        }

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopPlaying() {

        fab_play.setImageResource(R.drawable.ic_media_play);
        handler.removeCallbacks(runnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer= null;
        seekBar.setProgress(seekBar.getMax());

        isPlay =false;

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void resumePlaying(){

        fab_play.setImageResource(R.drawable.ic_media_pause);

        mediaPlayer.start();
        updateSeekbar();
    }

    private void pausePlaying(){

        fab_play.setImageResource(R.drawable.ic_media_play);

        handler.removeCallbacks(runnable);
        mediaPlayer.pause();

    }

    private Runnable runnable =new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer !=null){

                int position =mediaPlayer.getCurrentPosition();
                seekBar.setProgress(position);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(position);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(position)
                        - TimeUnit.MINUTES.toSeconds(minutes);

                txt_current_progress.setText(String.format("%02d:%02d",minutes,seconds));

                updateSeekbar();

            }
        }
    };

    private void updateSeekbar(){
        handler.postDelayed(runnable,1000);
    }


}
