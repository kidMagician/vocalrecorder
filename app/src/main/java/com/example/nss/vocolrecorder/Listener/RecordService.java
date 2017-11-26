package com.example.nss.vocolrecorder.Listener;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HVoiceUploadConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;
import com.example.nss.vocolrecorder.etc.NetworkChecker;

import java.io.File;
import java.io.IOException;

public class RecordService extends Service {

    MediaRecorder mediaRecorder;

    private String filePath;
    private String fileName;

    private DBHelper dbHelper;

    private long startTimeMile;
    private long endTimeMile;

    private NetworkChecker networkChecker;
    private HttpHandler httpHandler;

    public RecordService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        StartRecord();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if(mediaRecorder!=null){

            StopRecord();
        }

        super.onDestroy();
    }

    private void StartRecord(){

        SetFilePath();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(filePath+fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(192000);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

            startTimeMile =System.currentTimeMillis();

        }catch (IOException e){

            e.printStackTrace();

        }
    }

    private void StopRecord(){


        mediaRecorder.stop();

        endTimeMile =System.currentTimeMillis();

        mediaRecorder.release();
        mediaRecorder=null;

        long mTIme =System.currentTimeMillis();

        dbHelper.AddVoice(filePath+fileName,fileName,endTimeMile - startTimeMile,mTIme);

        networkChecker =new NetworkChecker(getApplicationContext());

        if(networkChecker.CheckConnected()){

            ContentValues values = new ContentValues();

            values.put("filePath",filePath+fileName);
            values.put("fileName",fileName);
            values.put("length",endTimeMile-startTimeMile);
            values.put("mTime",mTIme);

            httpHandler = new HttpHandler();
            httpHandler.execute("http://192.168.10.164:8000/voiceTraining/voiceUpload",values);

        }else{

        Toast.makeText(getApplicationContext(),"not connected network",Toast.LENGTH_LONG).show();

    }

    }

    private void SetFilePath(){

        File file;

        int i=0;

        filePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyVoiceRecorder/" ;

        fileName ="good.mp4";

        file = new File(filePath+fileName);

        while (file.exists()){

            i++;

            fileName = "good" +"_"+ i +".mp4";

            file = new File(filePath + fileName);

        }
    }
    class HttpHandler extends AsyncTask<Object,String,String> {

        Boolean checkSuc;

        public HttpHandler() {

            checkSuc = false;
        }

        @Override
        protected String doInBackground(Object... params) {

            HttpConnecterManager httpConnecterManager = (HttpConnecterManager)getApplicationContext().getApplicationContext(); //new HttpConnecterManager();

            HttpConnecter httpConnecter = (HVoiceUploadConnecter) httpConnecterManager.GetHttpConnecter(HttpConnecterManager.VOICEUPLOAD);

            httpConnecter.RequestPost(params[0].toString(), (ContentValues) params[1]);


            return null;
        }

    }

}
