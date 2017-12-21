package com.example.nss.vocolrecorder.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nss.vocolrecorder.item.VoiceItem;
import com.example.nss.vocolrecorder.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link PlayBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PlayBackFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "voice_item";

    private android.os.Handler mHandler= new android.os.Handler();

    private VoiceItem item;

    private TextView txt_fileName;
    private TextView txt_createdDdate;
    private SeekBar seek_play;
    private TextView txt_current_playtime;
    private  TextView txt_playLength;
    private Button btn_play;

    private MediaPlayer player;
    private boolean isPlaying;

    private long lengh_m =0;
    private long lengh_s =0;

    private String TAG ="PlayBackFragment";

    public PlayBackFragment() {
        // Required empty public constructor
    }


    public static PlayBackFragment newInstance(VoiceItem voiceItem) {
        PlayBackFragment fragment = new PlayBackFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, voiceItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item =getArguments().getParcelable(ARG_PARAM);

        long length = item.getLength();

        lengh_m= TimeUnit.MILLISECONDS.toMinutes(length);
        lengh_s = TimeUnit.MILLISECONDS.toSeconds(length) - TimeUnit.MINUTES.toSeconds(lengh_m);

        isPlaying =false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v =  getActivity().getLayoutInflater().inflate(R.layout.fragment_play_back,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        txt_fileName=(TextView)v.findViewById(R.id.txt_fileName);
        txt_createdDdate=(TextView)v.findViewById(R.id.txt_createDate);
        txt_playLength=(TextView)v.findViewById(R.id.txt_playLength);
        txt_current_playtime=(TextView)v.findViewById(R.id.txt_current_playtime);
        seek_play =(SeekBar)v.findViewById(R.id.seek_play);
        btn_play =(Button)v.findViewById(R.id.btn_play);

        txt_fileName.setText(item.getFileName());

        txt_playLength.setText(String.format("%02d:%02d",lengh_m,lengh_s));


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Play();

            }
        });

        seek_play.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(player!=null && fromUser ==true){
                            player.seekTo(progress);

                            long currentPosition =player.getCurrentPosition();

                            long minuts =TimeUnit.MILLISECONDS.toMinutes(currentPosition);
                            long seconds =TimeUnit.MICROSECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(minuts);

                            txt_current_playtime.setText(String.format("%02d:%02d",minuts,seconds));

                            updateSeekbar();
                        }else if(player ==null && fromUser==true){

                            startFromPoint(progress);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if(player !=null) {
                            mHandler.removeCallbacks(mRunable);
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        if(player !=null){

                            player.seekTo(seekBar.getProgress());

                            long currentPosition =player.getCurrentPosition();

                            long minuts =TimeUnit.MILLISECONDS.toMinutes(currentPosition);
                            long seconds =TimeUnit.MICROSECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(minuts);

                            txt_current_playtime.setText(String.format("%02d:%02d",minuts,seconds));

                            updateSeekbar();
                        }

                    }
                }


        );


        builder.setView(v);

        return builder.create();//super.onCreateDialog(savedInstanceState);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(player !=null){

            StopPlaying();
        }
    }

    public void Play(){
        if(!isPlaying){

            if(player==null){

                StartPlaying();

            }else{

                ResumePlaying();
            }

            isPlaying=true;

        }else {

            if(player!=null){

                    PausePlaying();
            }

            isPlaying=false;
        }

    }

    private void StartPlaying(){

        player = new MediaPlayer();

        try{

            player.setDataSource(item.getFilePath());
            player.prepare();
            seek_play.setMax(item.getLength());

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });

        }catch (IOException e){
            Log.d(TAG,e.toString());
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                StopPlaying();

            }
        });

        updateSeekbar();

    }

    private void startFromPoint(int position){

        player =new MediaPlayer();
        try{
            player.setDataSource(item.getFilePath());

            seek_play.setMax(item.getLength());
            player.seekTo(position);

            player.prepare();

            player.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            StopPlaying();
                        }
                    }
            );

        }catch (IOException e){
            Log.d(TAG,e.toString());
        }

    }


    private void StopPlaying(){

        player.stop();
        mHandler.removeCallbacks(mRunable);
        player.reset();
        player.release();

        player =null;

    }

    private void PausePlaying(){

        mHandler.removeCallbacks(mRunable);
        player.pause();

    }

    private void ResumePlaying(){

        player.start();
        updateSeekbar();
    }

    private Runnable mRunable =new Runnable() {
        @Override
        public void run() {

            int mCurrentPosition = player.getCurrentPosition();

            seek_play.setProgress(mCurrentPosition);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)-TimeUnit.MINUTES.toSeconds(minutes);

            txt_current_playtime.setText(String.format("%02d:%02d",minutes,seconds));

            updateSeekbar();
        }
    };

    private void updateSeekbar(){

        mHandler.postDelayed(mRunable,1000);
    }

}
