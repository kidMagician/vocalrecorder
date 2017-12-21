package com.example.nss.vocolrecorder.Adapter;



import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.Fragment.PlayBackFragment;
import com.example.nss.vocolrecorder.Listener.DBHelper;
import com.example.nss.vocolrecorder.Listener.OnDataBaseChangeListner;
import com.example.nss.vocolrecorder.item.VoiceItem;
import com.example.nss.vocolrecorder.R;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by NSS on 9/6/2017.
 */

public class FileViewAdapter extends RecyclerView.Adapter<FileViewAdapter.RecordingViewHolder> implements OnDataBaseChangeListner {

    private final String TAG ="fileViewAdapter";

    private DBHelper db;
    private Context mContext;

    public FileViewAdapter(Context mContext){

        this.mContext=mContext;
        db = new DBHelper(mContext);
        db.SetOnDataBaseChangeLisener(this);
    }


    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);

        RecordingViewHolder recordingViewHolder = new RecordingViewHolder(v);

        return recordingViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecordingViewHolder holder, int position) {

        VoiceItem item = db.GetVoice(position);

        holder.fileName.setText(item.getFileName());

        //duration
        long itemduration = item.getLength();

        long minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(itemduration);

        long second = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(itemduration)- java.util.concurrent.TimeUnit.MINUTES.toSeconds(minutes);

        String txtDuration = Long.toString(minutes) +":" + Long.toString(second);


        //set addedTime

        holder.fileLength.setText(txtDuration);

        holder.date.setText(DateUtils.formatDateTime(
                mContext,
                item.getmTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
        ));

        holder.cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    PlayBackFragment playBackFragment = PlayBackFragment.newInstance(db.GetVoice(holder.getPosition()));

                    FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();

                    playBackFragment.show(fragmentTransaction,"dialog_playback");

                }catch (Exception e){

                    Log.d(TAG,e.toString());

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return db.getCount();
    }

    @Override
    public void onDBAddVoice(){

        notifyItemInserted(getItemCount()-1);
    }

    public static class RecordingViewHolder extends RecyclerView.ViewHolder{

        private TextView fileName;
        private  TextView fileLength;
        private TextView date;
        private CardView cardView;

        public RecordingViewHolder(View v){
            super(v);

            fileName=(TextView) v.findViewById(R.id.txt_fileName);
            fileLength=(TextView)v.findViewById(R.id.txt_length);
            date=(TextView)v.findViewById(R.id.txt_date);
            cardView =(CardView) v.findViewById(R.id.card_view);
        }

    }
}

