package com.example.nss.vocolrecorder.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.Adapter.FileViewAdapter;
import com.example.nss.vocolrecorder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FileViwerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileViwerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private int position;

    private FileViewAdapter fileViewAdapter;

    public FileViwerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FileViwerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileViwerFragment newInstance(int position) {

        FileViwerFragment fragment = new FileViwerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_viwer, container,false);
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.view_recycler);

        LinearLayoutManager llm =new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        fileViewAdapter = new FileViewAdapter(getActivity());
        fileViewAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(fileViewAdapter);


        return view;
    }





}
