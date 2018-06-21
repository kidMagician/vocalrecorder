package com.example.nss.vocolrecorder.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.nss.vocolrecorder.Activity.LoginActivity;
import com.example.nss.vocolrecorder.Activity.VocalSellectActivity;
import com.example.nss.vocolrecorder.Activity.YoutubePlayActivity;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class VocalSellectFragment extends Fragment implements VocalSelectAdapter.onVocalItemClickLisener {

    private SearchView search_vocal;
    private RecyclerView rv_vocalList;
    private ProgressBar progress_youtube;
    private View layout_find;
    private View layout_nothing;
    private View layout_notNetwork;

    private SearchyoutubeThread searchyoutubeThread;
    private NetworkChecker networkChecker;

    private Menu menu;

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

        setHasOptionsMenu(true);
        setRetainInstance(false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vocalsellect,menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu=menu;
        initSearchView(menu);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_vocal_sellect, container, false);

        search_vocal =(SearchView) v.findViewById(R.id.search_vocal);
        rv_vocalList=(RecyclerView)v.findViewById(R.id.rv_vocalList);
        progress_youtube=(ProgressBar) v.findViewById(R.id.progress_youtube);
        layout_find=(View)v.findViewById(R.id.layout_find);
        layout_nothing=(View)v.findViewById(R.id.layouy_nothing);
        layout_notNetwork =(View)v.findViewById(R.id.layout_notNetwork);

        setFrame();
        setRecycleView();


        networkChecker =new NetworkChecker(getContext());

        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if(searchyoutubeThread!=null){
            searchyoutubeThread.cancel(true);
        }
    }

    @SuppressLint("NewApi")
    private void initSearchView(Menu menu){

//        MenuItem searchMenuItem =menu.findItem(R.id.menu_search);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        search_vocal =  new SearchView(((VocalSellectActivity) getActivity()).getSupportActionBar().getThemedContext());//(SearchView) searchMenuItem.getActionView();
//        search_vocal.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        search_vocal.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//
//        MenuItemCompat.setShowAsAction(searchMenuItem, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
//        MenuItemCompat.setActionView(searchMenuItem, search_vocal);

//        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {

                search_vocal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        if(validateSearch()&&networkChecker.CheckConnected()){

                            searchyoutubeThread = new SearchyoutubeThread();

                            startProgressBar();

                            searchyoutubeThread.execute(search_vocal.getQuery().toString());

                        }else{

                            notavailableNetwork();

                        }


                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {

                        return false;
                    }
                });

//                return false;
//            }
//        });

    }


    private void setFrame(){

        progress_youtube.setVisibility(View.GONE);
        rv_vocalList.setVisibility(View.GONE);
        layout_nothing.setVisibility(View.GONE);
        layout_notNetwork.setVisibility(View.GONE);
        layout_find.setVisibility(View.VISIBLE);
    }


    private void startProgressBar(){

        rv_vocalList.setVisibility(View.GONE);
        progress_youtube.setVisibility(View.VISIBLE);
        layout_find.setVisibility(View.GONE);
        layout_notNetwork.setVisibility(View.GONE);
        layout_nothing.setVisibility(View.GONE);

    }

    private void notavailableNetwork(){

        rv_vocalList.setVisibility(View.GONE);
        progress_youtube.setVisibility(View.GONE);
        layout_find.setVisibility(View.GONE);
        layout_notNetwork.setVisibility(View.VISIBLE);
        layout_nothing.setVisibility(View.GONE);
    }

    private void visibleList(){

        rv_vocalList.setVisibility(View.VISIBLE);
        progress_youtube.setVisibility(View.GONE);
        layout_find.setVisibility(View.GONE);
        layout_notNetwork.setVisibility(View.GONE);
        layout_nothing.setVisibility(View.GONE);

    }

    private void nothingList(){

        rv_vocalList.setVisibility(View.GONE);
        progress_youtube.setVisibility(View.GONE);
        layout_find.setVisibility(View.GONE);
        layout_notNetwork.setVisibility(View.GONE);
        layout_nothing.setVisibility(View.VISIBLE);

    }


    private void setRecycleView(){

        LinearLayoutManager llm= new LinearLayoutManager(getActivity());

        rv_vocalList.setLayoutManager(llm);

        vocalSelectAdapter = new VocalSelectAdapter(getActivity());
        vocalSelectAdapter.setOnclickLisener(this);

        rv_vocalList.setAdapter(vocalSelectAdapter);

    }

    public void RequestToYoutube(String queryTerm){
        try {

            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

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

    @Override
    public void OnClickVideo(int position) {

        Intent i = new Intent(getContext(), YoutubePlayActivity.class);

        i.putExtra("youtube_id",searchResultList.get(position).getId().getVideoId());
        i.putExtra("youtube_name",searchResultList.get(position).getSnippet().getTitle());

        startActivity(i);
    }

    class SearchyoutubeThread extends AsyncTask<Object,Object,Object> {

        private boolean isRunable =false;

        @Override
        protected Object doInBackground(Object... params) {

                RequestToYoutube((String)params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (searchResultList != null) {
                vocalSelectAdapter.setYoutubeList(searchResultList);

                if(searchResultList.size()>0){

                    visibleList();

                }else{

                    nothingList();
                }

            }
        }
    }

}
