package com.example.nss.vocolrecorder.Fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nss.vocolrecorder.Adapter.FeedBackViewAdapter;
import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.item.FeedBackItem;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class FeedBackFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private NetworkChecker networkChecker;
    private FeedBackRequester feedBackRequester;
    private RecyclerView rv_feedback;
    private FeedBackViewAdapter adapter;
    private View layout_nothing;
    private View layout_notConnecting;
    private ProgressBar progress;

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
        layout_notConnecting =(View)v.findViewById(R.id.layout_notNetwork);
        layout_nothing = (View)v.findViewById(R.id.layouy_nothing);
        progress=(ProgressBar)v.findViewById(R.id.progress);

        setRecycleView();

        requestFeedBack();

        return v;
    }

    private void initView(){

        layout_notConnecting.setVisibility(View.GONE);
        layout_nothing.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        rv_feedback.setVisibility(ViewPager.GONE);
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

        initView();

        if(networkChecker.CheckConnected()) {

            progress.setVisibility(View.VISIBLE);

            if (feedBackRequester != null) {

                feedBackRequester.cancel(true);

            } else {

                feedBackRequester = new FeedBackRequester();
                feedBackRequester.execute(BuildConfig.SERVER_URL + "/voiceTraining/feedBack", null);

            }
        }else{

            layout_notConnecting.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if(feedBackRequester !=null){

            feedBackRequester.cancel(true);
        }



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

            initView();

            if(strJson!=null) {

                List<FeedBackItem> items = FeedBackItem.convertToList(strJson);

                if(items.size()>0){

                    adapter.setFeedBackItem(items);
                    rv_feedback.setVisibility(View.VISIBLE);
                }else{

                    layout_nothing.setVisibility(View.VISIBLE);
                }


            }else{
                layout_notConnecting.setVisibility(View.VISIBLE);
            }

        }


    }

}
