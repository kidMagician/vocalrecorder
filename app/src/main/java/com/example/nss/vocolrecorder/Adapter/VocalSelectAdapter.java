package com.example.nss.vocolrecorder.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nss.vocolrecorder.BuildConfig;
import com.example.nss.vocolrecorder.Fragment.YoutubePlayFragment;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;
import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by NSS on 2017-11-15.
 */

public class VocalSelectAdapter extends RecyclerView.Adapter<VocalSelectAdapter.VocalVIewHolder>{

    private Context context;

    List<SearchResult> searchResultList;

    SearchResult item;

    public VocalSelectAdapter(Context context){

        this.context = context;

    }
    public void setYoutubeList(List<SearchResult> searchResultList){

        this.searchResultList =searchResultList;

        notifyItemInserted(getItemCount()-1);

    }

    @Override
    public void onBindViewHolder(VocalVIewHolder holder, int position) {

        item= searchResultList.get(position);

        holder.txt_name.setText(item.getSnippet().getTitle());

        Picasso.with(context).load(item.getSnippet().getThumbnails().getDefault().getUrl()).into(holder.img_thumnail);

        holder.btn_select_vocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.setPrefVocalUrl(context,item.getId().getVideoId());
                MySharedPreference.setPrefVocalName(context,item.getSnippet().getTitle());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                YoutubePlayFragment youtubeFragment=(YoutubePlayFragment)((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.youtubeFragment);
//                youtubeFragment.initialize(BuildConfig.YOUTUBE_API_KEY,
//                        new YoutubePlayer.OnInitializedListener() {
//                            @Override
//                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                                                YouTubePlayer youTubePlayer, boolean b) {
//                                // do any work here to cue video, play video, etc.
//                                youTubePlayer.cueVideo("5xVh-7ywKpE");
//                            }
//                            @Override
//                            public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                                                YouTubeInitializationResult youTubeInitializationResult) {
//
//                            }
//                        });

            }
        });
    }

    @Override
    public VocalVIewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.vocal_search,parent,false);

        VocalVIewHolder vocalVIewHolder = new VocalVIewHolder(v);

        return vocalVIewHolder;
    }

    public static class VocalVIewHolder extends RecyclerView.ViewHolder {

        private ImageView img_thumnail;
        private TextView txt_name;
        private Button btn_select_vocal;
        private View cardView;

        public VocalVIewHolder(View v){
            super(v);

            img_thumnail = (ImageView)v.findViewById(R.id.img_thumbnail);
            txt_name =(TextView)v.findViewById(R.id.txt_video_name);
            cardView =(View)v.findViewById(R.id.card_view);
            btn_select_vocal =(Button)v.findViewById(R.id.btn_selectVocal);
        }

    }

    @Override
    public int getItemCount() {
        if(searchResultList !=null){

            return searchResultList.size();

        }

        return 0;

    }
}
