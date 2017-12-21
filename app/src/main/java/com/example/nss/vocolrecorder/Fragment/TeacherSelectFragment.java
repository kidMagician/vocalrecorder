package com.example.nss.vocolrecorder.Fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.nss.vocolrecorder.Adapter.TeacherSelectAdapter;
import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Listener.OnListChangeListener;
import com.example.nss.vocolrecorder.item.TeacherItem;
import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.etc.NetworkChecker;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HJsonConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter.HttpConnecter;
import com.example.nss.vocolrecorder.util.HtttpManagement.HttpConnecterManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class TeacherSelectFragment extends Fragment {

    private SearchView search_teacher;
    private RecyclerView rv_teacherList;

    private AsyncTeacherSearch asyncTeacherSearch;
    private NetworkChecker networkChecker;

    private ProgressBar progress_search;

    private OnListChangeListener listChangeListener;

    public TeacherSelectFragment() {
        // Required empty public constructor
    }


    public static TeacherSelectFragment newInstance() {
        TeacherSelectFragment fragment = new TeacherSelectFragment();
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
        View v= inflater.inflate(R.layout.fragment_teacher_select, container, false);

        search_teacher = (SearchView) v.findViewById(R.id.search_teacher);
        rv_teacherList =(RecyclerView)v.findViewById(R.id.rv_teacherList);
        progress_search=(ProgressBar)v.findViewById(R.id.progress_teacher);

        search_teacher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                                  @Override
                                                  public boolean onQueryTextSubmit(String s) {

                                                      searchTeacher();

                                                      return false;
                                                  }

                                                  @Override
                                                  public boolean onQueryTextChange(String s) {
                                                      return false;
                                                  }
                                              }
        );

        networkChecker = new NetworkChecker(getContext());

        initailizeFrame();
        setRecyclerView();

        return v;

    }

    private void initailizeFrame(){

        progress_search.setVisibility(View.GONE);
        rv_teacherList.setVisibility(View.VISIBLE);
    }

    private void startProgress(){

        progress_search.setVisibility(View.VISIBLE);
        rv_teacherList.setVisibility(View.GONE);

    }

    private void endProgress(){

        progress_search.setVisibility(View.GONE);
        rv_teacherList.setVisibility(View.VISIBLE);
    }

    private void searchTeacher(){

        if(networkChecker.CheckConnected()){
            if(invalidateSearch()){

                startProgress();

                ContentValues values = new ContentValues();

                values.put("nickname",search_teacher.getQuery().toString());

                asyncTeacherSearch = new AsyncTeacherSearch();

                asyncTeacherSearch.execute(BuildConfig.SERVER_URL +"/voiceTraining/searchTeacher",values);

            }
        }else{

        }

    }


    private boolean invalidateSearch(){

        return true;
    }

    private void setOnListChangeListener(OnListChangeListener listener){
        listChangeListener =listener;
    }

    private void setRecyclerView(){

        rv_teacherList.setLayoutManager(new GridLayoutManager(getContext(),4));
        TeacherSelectAdapter adapter =new TeacherSelectAdapter(getContext());
        rv_teacherList.setAdapter(adapter);
        setOnListChangeListener(adapter);
        searchTeacher();

    }


    class AsyncTeacherSearch extends  AsyncTask<Object,Object,Object>{

        private List<TeacherItem> list_teacher;

        @Override
        protected Object doInBackground(Object... objects) {

            HttpConnecterManager httpConnecterManager = (HttpConnecterManager)getContext().getApplicationContext().getApplicationContext();

            HttpConnecter httpConnecter = (HJsonConnecter)httpConnecterManager.GetHttpConnecter(HttpConnecterManager.JSON);

            String resJson =(String)httpConnecter.Request((String) objects[0],(ContentValues) objects[1]);

            list_teacher =ConvertToList(resJson);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            listChangeListener.ChangeList(list_teacher);

            endProgress();

        }

        private List<TeacherItem> ConvertToList(String json){

            List<TeacherItem> list_teacher= new ArrayList<TeacherItem>();

            try{

                JSONArray array = new JSONArray(json);
                String nickname =array.getJSONObject(0).getString("nickname");
                TeacherItem item = new TeacherItem(nickname);
                list_teacher.add(item);

            }catch (JSONException e){

                e.printStackTrace();
            }

            return list_teacher;

        }

    }

}
