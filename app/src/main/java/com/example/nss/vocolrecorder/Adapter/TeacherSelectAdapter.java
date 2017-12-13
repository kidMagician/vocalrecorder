package com.example.nss.vocolrecorder.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nss.vocolrecorder.Fragment.TeacherSelectFragment;
import com.example.nss.vocolrecorder.Listener.OnDataBaseChangeListner;
import com.example.nss.vocolrecorder.Listener.OnListChangeListener;
import com.example.nss.vocolrecorder.Listener.TeacherItem;
import com.example.nss.vocolrecorder.R;

import java.util.List;

/**
 * Created by NSS on 2017-12-05.
 */

public class TeacherSelectAdapter extends RecyclerView.Adapter<TeacherSelectAdapter.TeacherViewHolder> implements OnListChangeListener {

    private Context context;
    private List<TeacherItem> list_teacher;

    public TeacherSelectAdapter(Context context){

        this.context =context;
    }

    @Override
    public void onBindViewHolder(TeacherViewHolder holder, int position) {

        TeacherItem item=list_teacher.get(position);

        holder.txt_teacher_nick.setText(item.getTeacher_nick());
    }

    public void ChangeList(List<TeacherItem> list_teacher){

        this.list_teacher =list_teacher;

        notifyItemChanged(getItemCount());
        notifyDataSetChanged();
    }


    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher,parent,false);

        TeacherViewHolder holder = new TeacherViewHolder(v);

        return holder;
    }

    @Override
    public int getItemCount() {
        if(list_teacher !=null){

            return list_teacher.size();
        }

        return 0;
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_teacher;
        private TextView txt_teacher_nick;

        public TeacherViewHolder(View v){
            super(v);

           img_teacher= (ImageView) v.findViewById(R.id.img_teacher);
           txt_teacher_nick =(TextView)v.findViewById(R.id.txt_teacher_nick);
        }

    }

}
