package com.example.nss.vocolrecorder.Activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.Listener.RecordService;
import com.example.nss.vocolrecorder.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;

public class RecorderActivity extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerView youTubeView;
    private FloatingActionButton btn_play;
    private TextView txt_play;

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
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        youTubeView.initialize(BuildConfig.YOUTUBE_API_KEY, this);

        youtube_id= MySharedPreference.getPrefVocalUrl(RecorderActivity.this);
        youtube_name =MySharedPreference.getPrefVocalName(RecorderActivity.this);

        txt_play.setText(youtube_name);

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

            txt_play.setText("Recoding...");

            startService(intent);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } else {

            isPlay = false;

            pauseYoutube();

            Intent intent = new Intent(RecorderActivity.this, RecordService.class);

            txt_play.setText("tap to the start recording");

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
            player.cueVideo(youtube_id);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
