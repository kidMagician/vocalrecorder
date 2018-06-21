package com.example.nss.vocolrecorder.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nss.vocolrecorder.Fragment.FeedBackPlayFragment;
import com.example.nss.vocolrecorder.item.FeedBackItem;
import com.example.nss.vocolrecorder.R;

import java.util.List;

/**
 * Created by NSS on 2017-12-12.
 */

public class FeedBackViewAdapter extends RecyclerView.Adapter<FeedBackViewAdapter.FeedBackHolder> {

    private Context context;
    private List<FeedBackItem> feedBackItemList;

    private final String FRAGMENT_FEEDBACK_PLAY_TAG = "fragemnt_feedback_play";
    private final String LOG_TAG = "feedBackViewAdapter";

    public FeedBackViewAdapter(Context context){

        this.context = context;
    }

    public void setFeedBackItem(List<FeedBackItem> feedBackItems){

        feedBackItemList = feedBackItems;

        notifyItemChanged(getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(FeedBackHolder holder, final int position) {

        FeedBackItem item = getItem(position);

        holder.txt_teacher_nick.setText("not yet");
        holder.txt_subject.setText(item.getSubject());
        holder.txt_savedDate.setText(item.getSavedDate());

        holder.card_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FeedBackPlayFragment feedBackPlayFragment = FeedBackPlayFragment.newInstance(getItem(position));

                    FragmentTransaction fragmentTransaction =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

                    feedBackPlayFragment.show(fragmentTransaction,FRAGMENT_FEEDBACK_PLAY_TAG);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                }
            }
        });
    }

    private FeedBackItem getItem(int i){

        return feedBackItemList.get(i);
    }

    @Override
    public FeedBackHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_feedback,parent,false);

        FeedBackHolder holder = new FeedBackHolder(v);

        return holder;
    }

    @Override
    public int getItemCount() {
        if(feedBackItemList!=null){

            return feedBackItemList.size();

        }

        return 0;

    }

    class FeedBackHolder extends RecyclerView.ViewHolder{

        TextView txt_subject;
        TextView txt_teacher_nick;
        TextView txt_savedDate;
        CardView card_feedback;

        public FeedBackHolder(View v){
            super(v);

            txt_subject =(TextView) v.findViewById(R.id.txt_subject);
            txt_savedDate=(TextView)v.findViewById(R.id.txt_savedDate);
            txt_teacher_nick=(TextView) v.findViewById(R.id.txt_teacher_nick);
            card_feedback =(CardView)v.findViewById(R.id.card_feedback);
        }

    }

}
