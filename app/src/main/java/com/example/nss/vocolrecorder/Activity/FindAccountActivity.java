package com.example.nss.vocolrecorder.Activity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FindAccountActivity extends AppCompatActivity {

    private Button btn_ok;
    private Button btn_cancel;
    private EditText edit_email;

    private HttpConnecterManager httpConnecterManager;
    private HttpAsynk httpAsynk;
    private NetworkChecker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        edit_email =(EditText)findViewById(R.id.edit_email);
        btn_ok =(Button)findViewById(R.id.btn_ok);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FindAccount();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FindAccountActivity.this.finish();

            }
        });

        networkChecker = new NetworkChecker(FindAccountActivity.this);

    }

    private void FindAccount(){

        if(validate()) {
            if (networkChecker.CheckConnected()) {

                httpAsynk = new HttpAsynk();
                ContentValues values = new ContentValues();
                String strEmail = edit_email.getText().toString();
                values.put("username", strEmail);
                httpAsynk.execute(BuildConfig.SERVER_URL+"auth/findPass", values);

            } else {
                Toast.makeText(FindAccountActivity.this,"Network not connected",Toast.LENGTH_LONG).show();

            }


        }
    }

    private Boolean validate(){

        Boolean valid = true;

        String strEmail = edit_email.getText().toString();

        if(strEmail.isEmpty()) {

            edit_email.setError("Enter UserName");

            valid =false;

        }else  if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){

            edit_email.setError("invalid email");

            valid= false;

        }

        return valid;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(httpAsynk!=null){

            httpAsynk.cancel(true);
        }

    }

    private class HttpAsynk extends AsyncTask<Object,Object,Object>{

        Boolean is_suc;

        public HttpAsynk(){

            is_suc =false;

        }

        @Override
        protected String doInBackground(Object... params) {

            httpConnecterManager = new HttpConnecterManager();

            HttpConnecter httpConnecter = httpConnecterManager.GetHttpConnecter(HttpConnecterManager.POST);

            is_suc = (boolean)httpConnecter.Request((String)params[0],(ContentValues) params[1]);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(is_suc){

                FindAccountActivity.this.finish();

            }else{
                Toast.makeText(FindAccountActivity.this,"failed",Toast.LENGTH_LONG).show();
            }
        }
    }



}
