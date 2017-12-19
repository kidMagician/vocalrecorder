package com.example.nss.vocolrecorder.Activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.AsynkFinishLisener;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;

public class LoginActivity extends AppCompatActivity implements AsynkFinishLisener {

    private static final String TAG = "LoginActivity";

    EditText edit_ID;
    EditText edit_pass;
    Button btn_login;
    Button btn_register;
    TextView txt_findAccount;

    HttpHandler authHandler;
    HttpConnecterManager httpConnecterManager;
    ProgressDialog progressDialog;

    NetworkChecker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_ID = (EditText)findViewById(R.id.edit_ID);
        edit_pass = (EditText)findViewById(R.id.edit_pass);
        btn_login = (Button)findViewById(R.id.btn_signIn);
        btn_register =(Button)findViewById(R.id.btn_signUp);
        txt_findAccount =(TextView)findViewById(R.id.txt_findAccount);

        httpConnecterManager = (HttpConnecterManager)getApplicationContext(); //new HttpConnecterManager();
        networkChecker = new NetworkChecker(LoginActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             login();
                                         }
                                     }
        );

        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(i);

            }
        });

        txt_findAccount.setLinksClickable(true);

        txt_findAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(LoginActivity.this,FindAccountActivity.class);

                startActivity(intent);
            }
        });

    }

    private void login(){

        if(validate()){

            if(networkChecker.CheckConnected()) {

                progressDialog = new ProgressDialog(LoginActivity.this);

                progressDialog.setMessage("Authenticating");

                progressDialog.show();

                progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                String str_ID = edit_ID.getText().toString();
                String str_pass = edit_pass.getText().toString();

                ContentValues authData = new ContentValues();
                authData.put("username", str_ID);
                authData.put("password", str_pass);


                if(authHandler!=null){
                    authHandler.cancel(true);
                }

                authHandler = new HttpHandler();
                authHandler.setAsynkFinishLisener(this);
                authHandler.execute(BuildConfig.SERVER_URL+"/auth/login", authData);

            }else{
                Toast.makeText(LoginActivity.this,"not connected network",Toast.LENGTH_LONG).show();

            }

        }

    }

    private Boolean validate(){

        Boolean valid = true;

        String strID = edit_ID.getText().toString();
        String strPass = edit_pass.getText().toString();

        if(strID.isEmpty()){

            edit_ID.setError("enter Email");
            valid =false;

        }else if(!Patterns.EMAIL_ADDRESS.matcher(strID).matches()){

            edit_ID.setError("Enter vaild Emil");
            valid = false;

        }

        if(strPass.isEmpty()||strPass.length()<8|| strPass.length() > 12){

            edit_pass.setError("between 4 and 12 alphanumeric characters");
            valid =false;

        }

        return valid;
    }

    @Override
    public void finishAvtivity() {

//        changeLauncher();

        finish();

    }

    private void changeLauncher(){

        String s = getApplicationContext().getPackageName();
        ComponentName cm = new ComponentName(s, s+".Activity.AliasActivity");
        ComponentName cm2 = new ComponentName(s, s+".Activity.LoginActivity");
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(cm, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(cm2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(authHandler !=null){

            authHandler.cancel(true);

        }

        if(progressDialog !=null){

            progressDialog.dismiss();

            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

    }


    class HttpHandler extends AsyncTask<Object,String,String>{

        Boolean checkSuc;

        AsynkFinishLisener asynkFinishLisener;

        public HttpHandler(){

            checkSuc =false;
        }

        @Override
        protected String doInBackground(Object... params) {

            HttpConnecter httpConnecter = httpConnecterManager.GetHttpConnecter(HttpConnecterManager.LOGIN);

            checkSuc = (Boolean)httpConnecter.Request(params[0].toString(),(ContentValues)params[1]);

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(checkSuc){

                Toast.makeText(LoginActivity.this,"seccess Login",Toast.LENGTH_LONG).show();

                Intent i=new Intent(LoginActivity.this,MainActivity.class);

                startActivity(i);

                asynkFinishLisener.finishAvtivity();

            }else{
                Toast.makeText(LoginActivity.this,"failed Login",Toast.LENGTH_LONG).show();

                if(progressDialog !=null){

                    progressDialog.dismiss();

                    progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
            }


        }

        public void setAsynkFinishLisener(AsynkFinishLisener asynkFinishLisener) {
            this.asynkFinishLisener = asynkFinishLisener;
        }
    }
}
