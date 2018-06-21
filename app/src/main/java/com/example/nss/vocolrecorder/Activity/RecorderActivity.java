package com.example.nss.vocolrecorder.Activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.service.RecordService;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;

public class RecorderActivity extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerView youTubeView;
    private FloatingActionButton btn_play;
    private TextView txt_play;
    private TextView txt_videoname;
    private ImageButton btn_select;


    YouTubePlayer player_youtube;

    private boolean isPlay;

    private String youtube_id;
    private String youtube_name;

    public RecorderActivity(){

        isPlay =false;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        btn_play = (FloatingActionButton)findViewById(R.id.btn_play);
        txt_play = (TextView) findViewById(R.id.txt_play);
        txt_videoname=(TextView) findViewById(R.id.txt_video_name);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        btn_select=(ImageButton) findViewById(R.id.btn_select_vocal);

        initYoutube();
        initButton();

    }




    private void initYoutube(){

        youTubeView.initialize(BuildConfig.YOUTUBE_API_KEY, this);
        youtube_id= MySharedPreference.getPrefVocalUrl(RecorderActivity.this);
        youtube_name =MySharedPreference.getPrefVocalName(RecorderActivity.this);

        if(youtube_id ==null && youtube_name==null){

            txt_videoname.setText(getText(R.string.warning_select_video));

        }else{

            txt_videoname.setText(youtube_name);

        }
    }

    private void initButton(){

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RecorderActivity.this, VocalSellectActivity.class);

                startActivity(i);

            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Play();
            }
        });

    }



    private void Play() {

        if (!isPlay) {

            isPlay = true;

            playYoutube();

            Intent intent = new Intent(RecorderActivity.this, RecordService.class);

            Toast.makeText(RecorderActivity.this, "recorfing start", Toast.LENGTH_LONG);

            File folder = new File(Environment.getExternalStorageDirectory() + "/MyVoiceRecorder");

            if (!folder.exists()) {

                folder.mkdir();
            }

            txt_play.setText(getText(R.string.recording_button));

            startService(intent);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } else {

            isPlay = false;

            pauseYoutube();

            Intent intent = new Intent(RecorderActivity.this, RecordService.class);

            txt_play.setText(R.string.before_recording_button);

            stopService(intent);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

    }

    private void playYoutube(){

        if(!player_youtube.isPlaying()) {

            player_youtube.play();
        }
    }
    private void pauseYoutube(){

        if(player_youtube.isPlaying()){

            player_youtube.pause();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player_youtube =player;

            if(youtube_id!=null){
                player.cueVideo(youtube_id);
            }

        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
