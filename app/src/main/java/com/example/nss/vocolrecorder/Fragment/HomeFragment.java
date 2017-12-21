package com.example.nss.vocolrecorder.Fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nss.vocolrecorder.Adapter.PostViewAdapter;
import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;
import com.example.nss.vocolrecorder.item.PostItem;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import java.util.List;


public class HomeFragment extends android.support.v4.app.Fragment {

    private PostRequester postRequester;
    private NetworkChecker networkChecker;
    private RecyclerView rv_post;
    private PostViewAdapter postViewAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_home, container, false);

        rv_post = (RecyclerView)v.findViewById(R.id.rv_post);

        init();

        return v;

    }

    private void init(){

        requestPost();
        initRecyclerView();
    }

    private void initRecyclerView(){

        postViewAdapter = new PostViewAdapter(getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_post.setLayoutManager(llm);

        rv_post.setAdapter(postViewAdapter);

    }

    private void requestPost(){
        networkChecker = new NetworkChecker(getContext());
        if(networkChecker.CheckConnected()){

            if(postRequester==null){

                postRequester = new PostRequester();
                postRequester.execute(BuildConfig.SERVER_URL+"/voiceTraining/post",null);

            }else{

                postRequester.cancel(true);
                postRequester =new PostRequester();

                postRequester.execute(BuildConfig.SERVER_URL+"/voiceTraining/post",null);
            }

        }

    }

    class PostRequester extends AsyncTask<Object,Object,Object>{

        String json;

        @Override
        protected Object doInBackground(Object... objects) {

            HttpConnecterManager httpConnecterManager= (HttpConnecterManager)getContext().getApplicationContext();

            HttpConnecter httpConnecter =httpConnecterManager.GetHttpConnecter(HttpConnecterManager.JSON);

            json = (String)httpConnecter.Request((String)objects[0],(ContentValues)objects[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            List<PostItem> items = PostItem.convertToList(json);

            postViewAdapter.addItem(items);

            Toast.makeText(getContext(),json,Toast.LENGTH_LONG);
        }
    }



}
