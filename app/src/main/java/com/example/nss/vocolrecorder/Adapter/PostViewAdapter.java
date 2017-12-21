package com.example.nss.vocolrecorder.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nss.vocolrecorder.R;
import com.example.nss.vocolrecorder.item.PostItem;

import java.util.List;

/**
 * Created by NSS on 2017-12-20.
 */

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.PostViewHolder> {

    private List<PostItem> items;
    private Context context;

    public PostViewAdapter(Context context){

        this.context = context;
    }


    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        PostItem item=items.get(position);

        holder.txt_author.setText(item.getAuthor());
        holder.txt_content.setText(item.getContent());
        holder.txt_published_date.setText(item.getPublished_date());
        holder.txt_title.setText(item.getTile());
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(context).inflate(R.layout.card_post,parent,false);

        PostViewHolder holder = new PostViewHolder(v);

        return holder;
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_title;
        private TextView txt_content;
        private TextView txt_author;
        private TextView txt_published_date;

        public PostViewHolder(View v){

            super(v);

            txt_title = (TextView) v.findViewById(R.id.txt_title);
            txt_content =(TextView)v.findViewById(R.id.txt_content);
            txt_author=(TextView)v.findViewById(R.id.txt_author);
            txt_published_date=(TextView)v.findViewById(R.id.txt_published_date);
        }
    }

    @Override
    public int getItemCount() {
        if(items !=null){
            return items.size();
        }

        return 0;
    }

    public void addItem(List<PostItem> items){

        this.items =items;

        notifyItemChanged(getItemCount());
        notifyDataSetChanged();

    }

}
