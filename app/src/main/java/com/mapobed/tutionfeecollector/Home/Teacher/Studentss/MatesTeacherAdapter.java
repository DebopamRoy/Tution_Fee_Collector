package com.mapobed.tutionfeecollector.Home.Teacher.Studentss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapobed.tutionfeecollector.R;

import java.util.List;

public class MatesTeacherAdapter extends RecyclerView.Adapter<MatesTeacherAdapter.MatesTeacherHolder>  {
    private List<MatesTeacherModelClass>list;
    private StudentTeacherInterface teacherInterface;

    public MatesTeacherAdapter(List<MatesTeacherModelClass> list, StudentTeacherInterface teacherInterface) {
        this.list = list;
        this.teacherInterface = teacherInterface;
    }

    @NonNull
    @Override
    public MatesTeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatesTeacherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mates_teacher_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MatesTeacherHolder holder, int position) {
        holder.img.setImageResource(list.get(position).getImg());
        holder.title.setText(list.get(position).getNamee());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MatesTeacherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView title;
        public MatesTeacherHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.uoc_img);
            title=itemView.findViewById(R.id.uoc_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            teacherInterface.onClick(list.get(getAdapterPosition()));
        }
    }
    public interface StudentTeacherInterface{
        void onClick(MatesTeacherModelClass modelClass);
    }
}
