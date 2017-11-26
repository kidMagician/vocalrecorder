package com.example.nss.vocolrecorder.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";

    EditText edit_ID;
    EditText edit_nickname;
    EditText edit_pass;
    EditText edit_passCheck;

    Button btn_register;
    Button btn_cancle;
    ProgressDialog progressDialog;

    RegisterHttpThread registerHttpThread;

    HttpConnecterManager httpConnecterManager;
    NetworkChecker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        edit_ID =(EditText)findViewById(R.id.edit_ID);
        edit_nickname=(EditText)findViewById(R.id.edit_nickname);
        edit_pass =(EditText)findViewById(R.id.edit_pass);
        edit_passCheck=(EditText)findViewById(R.id.edit_passCheck);

        btn_register =(Button) findViewById(R.id.btn_register);
        btn_cancle =(Button) findViewById(R.id.btn_cancel);

        httpConnecterManager = new HttpConnecterManager();
        networkChecker = new NetworkChecker(JoinActivity.this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register();

            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JoinActivity.this.finish();
            }
        });


    }

    private void Register(){

        if(!validate()){
            if(networkChecker.CheckConnected()){

                progressDialog = new ProgressDialog(JoinActivity.this);

                progressDialog.setMessage("Authenticating");
                progressDialog.show();

                String str_ID =edit_ID.getText().toString();
                String str_nickname =edit_nickname.getText().toString();
                String str_pass =edit_pass.getText().toString();

                ContentValues authData =new ContentValues();
                authData.put("username",str_ID);
                authData.put("nickname",str_nickname);
                authData.put("password",str_pass);

                registerHttpThread = new RegisterHttpThread();

                registerHttpThread.execute(BuildConfig.SERVER_URL+"/auth/join",authData);

            }
            else{

                Toast.makeText(JoinActivity.this,"not connected network",Toast.LENGTH_LONG).show();

            }

        }

    }
    private Boolean validate(){

        Boolean valid = false;

        String strID= edit_ID.getText().toString();
        String strNickname = edit_nickname.getText().toString();
        String strPass = edit_pass.getText().toString();
        String strCPass =edit_passCheck.getText().toString();

        if(strNickname.isEmpty()|| strNickname.length()<3 ){

            edit_nickname.setError("at least 3characters");
            valid =true;

        }

        if(strID.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(strID).matches()){

            edit_ID.setError("Enter validated Email");
            valid =true;

        }

        if(strPass.isEmpty()||strPass.length() < 8|| strPass.length() > 12){

            edit_pass.setError("between 4 and 12 alphanumeric characters");
            valid =true;

        }

        if(!strCPass.equals(strPass)){

            edit_passCheck.setError("not matched with pass");
            valid=true;
        }

        return valid;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(registerHttpThread!= null) {

            registerHttpThread.cancel(true);

        }

    }

    class RegisterHttpThread extends AsyncTask<Object,String,String>{

//        HRegisterConnecter httpConnecter;
        Boolean checkSuc;

        RegisterHttpThread(){

            checkSuc = false;

        }

        @Override
        protected String doInBackground(Object... params) {

//            httpConnecter =new HRegisterConnecter();

            HttpConnecter httpConnecter =httpConnecterManager.GetHttpConnecter(HttpConnecterManager.REGISTER);

            checkSuc = (Boolean)httpConnecter.RequestPost(params[0].toString(),(ContentValues)params[1]);

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            progressDialog.dismiss();

            if(checkSuc){

                JoinActivity.this.finish();

            }else{

                Toast.makeText(JoinActivity.this,"failed register",Toast.LENGTH_LONG).show();

            }

        }
    }

}
