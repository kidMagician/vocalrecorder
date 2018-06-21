package com.example.nss.vocolrecorder.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.nss.vocolrecorder.Activity.RecorderActivity;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.service.RecordService;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecoderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoderFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private int position;

    private FloatingActionButton btn_play;
    private TextView txt_play;
    private VideoView video_vocal;

    private Button btn_test;

    private String tag = "in RecoderFragment";

    private boolean isPlay;


    public RecoderFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecoderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoderFragment newInstance(int position) {
        RecoderFragment fragment = new RecoderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);

        isPlay = false;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recoder, container, false);

        btn_play = (FloatingActionButton) view.findViewById(R.id.btn_play);
        txt_play = (TextView) view.findViewById(R.id.txt_play);
        video_vocal = (VideoView) view.findViewById(R.id.video_vocal);
        btn_test = (Button)view.findViewById(R.id.btn_testRecord);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), RecorderActivity.class);

                startActivity(i);
            }
        });

        Uri videoUri = Uri.parse("http://www.youtube.com/embed/srMFb6zpx2Y");

        video_vocal.setVideoURI(videoUri);

        final MediaController mediaController = new MediaController(getContext());
        video_vocal.setMediaController(mediaController);
        video_vocal.start();



        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Play();
            }
        });

        return view;
    }


    private void Play() {

        if (!isPlay) {

            isPlay = true;

            Intent intent = new Intent(getActivity(), RecordService.class);

            Toast.makeText(getActivity(), "recorfing start", Toast.LENGTH_LONG);

            File folder = new File(Environment.getExternalStorageDirectory() + "/MyVoiceRecorder");

            if (!folder.exists()) {

                folder.mkdir();
            }

            txt_play.setText(getText(R.string.recording_button));

            getActivity().startService(intent);

            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } else {

            isPlay = false;

            Intent intent = new Intent(getActivity(), RecordService.class);

            txt_play.setText(getText(R.string.before_recording_button));

            getActivity().stopService(intent);

            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        }

    }
}