package com.example.nss.vocolrecorder.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.etc.NetworkChecker;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HVoiceUploadConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import java.io.File;
import java.io.IOException;

public class HomeWorRecordService extends Service {

    private MediaRecorder recorder;

    private String filePath;
    private String fileName;

    private long startTimeMile;
    private long endTimeMile;
    private VoiceSender voiceSender;

    private NetworkChecker networkChecker;

    public HomeWorRecordService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        networkChecker = new NetworkChecker(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        startRecording();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(recorder!=null){

//            stopRecordring();
        }

    }

    private void startRecording(){

        setFilePath();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(filePath+fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioChannels(1);

        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(192000);

        try {
            recorder.prepare();
            recorder.start();

            startTimeMile =System.currentTimeMillis();

        }catch (IOException e){

            e.printStackTrace();

        }
    }
    private void stopRecordring(){

        recorder.stop();

        long mTIme =System.currentTimeMillis();
        endTimeMile =System.currentTimeMillis();

        recorder.reset();
        recorder.release();
        recorder =null;

//        if(voiceSender !=null){
//
//            voiceSender.cancel(true);
//
//        }

//        voiceSender = new VoiceSender();
//
//        ContentValues values = new ContentValues();
//        values.put("paramName","voiceFile");
//        values.put("filePath",filePath+fileName);
//        values.put("fileName",fileName);
//        values.put("savedDate",mTIme);
//
//        voiceSender.execute(BuildConfig.SERVER_URL+"/voiceTraining/homeworkvoiceUpload",values);

    }

    private void setFilePath(){

        File file;

        int i=0;

        filePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyVoiceRecorder/" ;

        fileName ="HomeWorkRecord.mp4";

        file = new File(filePath+fileName);

        while (file.exists()){

            i++;

            fileName = "HomeWorkRecord" +"_"+ i +".mp4";

            file = new File(filePath + fileName);

        }

    }

    class VoiceSender extends AsyncTask<Object,Object,Object>{

        @Override
        protected Object doInBackground(Object... params) {

            HttpConnecterManager httpConnecterManager = (HttpConnecterManager)getApplicationContext().getApplicationContext();

            HttpConnecter httpConnecter = (HVoiceUploadConnecter) httpConnecterManager.GetHttpConnecter(HttpConnecterManager.VOICEUPLOAD);

            httpConnecter.Request(params[0].toString(), (ContentValues) params[1]);

            return null;
        }
    }
}
