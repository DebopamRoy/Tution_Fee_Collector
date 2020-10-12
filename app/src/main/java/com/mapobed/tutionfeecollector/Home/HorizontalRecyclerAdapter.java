package com.mapobed.tutionfeecollector.Home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapobed.tutionfeecollector.R;

import java.util.List;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.StudentHorizontalRecyclerHolder> {
    private List<HorizontalRecyclerModelClass> list;
    private HomeInterface homeInterface;
    public HorizontalRecyclerAdapter(List<HorizontalRecyclerModelClass> list, HomeInterface homeInterface) {
        this.list = list;
        this.homeInterface = homeInterface;
    }

    @NonNull
    @Override
    public StudentHorizontalRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentHorizontalRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_home_rec_view_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHorizontalRecyclerHolder holder, int position) {
        holder.tv.setText(list.get(position).getId());
        holder.iv.setImageResource(list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentHorizontalRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView tv;

        public StudentHorizontalRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.stu_home_image);
            tv = itemView.findViewById(R.id.stu_home_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            homeInterface.onOptionClick(list.get(getAdapterPosition()));
        }
    }
    public interface HomeInterface {
        void onOptionClick(HorizontalRecyclerModelClass modelClass);
    }
}
