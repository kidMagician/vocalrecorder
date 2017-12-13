package com.example.nss.vocolrecorder.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayActivity extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerView youTubeView;
    private Button btn_select;

    private String youtube_id;
    private String youtube_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_play);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(BuildConfig.YOUTUBE_API_KEY, this);
        btn_select = (Button)findViewById(R.id.btn_select);

        Intent i = getIntent();
        youtube_id=i.getStringExtra("youtube_id");
        youtube_name =i.getStringExtra("youtube_name");

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.setPrefVocalUrl(YoutubePlayActivity.this,youtube_id);
                MySharedPreference.setPrefVocalName(YoutubePlayActivity.this,youtube_name);
            }
        });


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(youtube_id);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}
