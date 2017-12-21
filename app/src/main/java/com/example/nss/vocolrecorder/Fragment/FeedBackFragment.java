package com.example.nss.vocolrecorder.Fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.Adapter.FeedBackViewAdapter;
import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.item.FeedBackItem;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;


public class FeedBackFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private NetworkChecker networkChecker;
    private FeedBackRequester feedBackRequester;
    private RecyclerView rv_feedback;
    private FeedBackViewAdapter adapter;


    public FeedBackFragment() {
        // Required empty public constructor
    }


    public static FeedBackFragment newInstance() {
        FeedBackFragment fragment = new FeedBackFragment();
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
        View v= inflater.inflate(R.layout.fragment_alert, container, false);
        rv_feedback=(RecyclerView) v.findViewById(R.id.rvFeedBack);

        setRecycleView();

        requestFeedBack();

        return v;
    }

    private void setRecycleView(){

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_feedback.setLayoutManager(llm);

        adapter = new FeedBackViewAdapter(getActivity());

        rv_feedback.setAdapter(adapter);
    }

    private void requestFeedBack(){

        networkChecker =new NetworkChecker(getActivity());

        if(networkChecker.CheckConnected())

            if(feedBackRequester !=null){

                feedBackRequester.cancel(true);

            }else{

                feedBackRequester = new FeedBackRequester();
                feedBackRequester.execute(BuildConfig.SERVER_URL+"/voiceTraining/feedBack",null);

            }



    }

    @Override
    public void onDestroy() {

        feedBackRequester.cancel(true);

        super.onDestroy();
    }

    class FeedBackRequester extends AsyncTask<Object,Object,Object>{

        String strJson;

        @Override
        protected Object doInBackground(Object... objects) {

            HttpConnecterManager httpConnecterManager = (HttpConnecterManager)getContext().getApplicationContext();

            HttpConnecter httpConnecter= httpConnecterManager.GetHttpConnecter(HttpConnecterManager.JSON);

            strJson= (String)httpConnecter.Request(objects[0].toString(),(ContentValues)objects[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            adapter.setFeedBackItem(FeedBackItem.convertToList(strJson));
        }


    }

}
