package com.example.nss.vocolrecorder.Fragment;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nss.vocolrecorder.Adapter.VocalSelectAdapter;
import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;


import java.io.IOException;
import java.util.List;


public class VocalSellectFragment extends Fragment {

    private SearchView search_vocal;
    private RecyclerView rv_vocalList;

    private Button btn_test;

    private SearchyoutubeThread searchyoutubeThread;
    private NetworkChecker networkChecker;

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    private List<SearchResult> searchResultList;

//    List<SearchResult> searchResultList;

    VocalSelectAdapter vocalSelectAdapter;

    public VocalSellectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_vocal_sellect, container, false);

        search_vocal =(SearchView) v.findViewById(R.id.search_vocal);
        btn_test=(Button)v.findViewById(R.id.btn_test);
        rv_vocalList=(RecyclerView)v.findViewById(R.id.rv_vocalList);

        setRecycleView();

        search_vocal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                if(validateSearch()&&networkChecker.CheckConnected()){

                    searchyoutubeThread.execute();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });


        btn_test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                setVocal("urltest","testName2");
            }
        });

        networkChecker =new NetworkChecker(getContext());

        searchyoutubeThread = new SearchyoutubeThread();

        ;

        return v;
    }

    private void setRecycleView(){

        LinearLayoutManager llm= new LinearLayoutManager(getActivity());

        rv_vocalList.setLayoutManager(llm);

        vocalSelectAdapter = new VocalSelectAdapter(getActivity());

        rv_vocalList.setAdapter(vocalSelectAdapter);

    }

    public void RequestToYoutube(){
        try {

            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            String queryTerm = search_vocal.getQuery().toString();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            String apiKey = BuildConfig.YOUTUBE_API_KEY;
            search.setKey(apiKey);
            search.setQ(queryTerm);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            searchResultList = searchResponse.getItems();

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private boolean validateSearch(){


        return true;
    }



    private void setVocal(String url,String vocalName){

        MySharedPreference.setPrefVocalUrl(getActivity(),url);
        MySharedPreference.setPrefVocalName(getActivity(),vocalName);
        Toast.makeText(getContext(),"working well",Toast.LENGTH_LONG);
    }

    private String getVocalList(){

        String listJson ="";

        return listJson;
    }


    class SearchyoutubeThread extends AsyncTask<Object,Object,Object> {

        @Override
        protected Object doInBackground(Object... params) {

            RequestToYoutube();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (searchResultList != null) {
                vocalSelectAdapter.setYoutubeList(searchResultList);
            }
        }
    }


}
