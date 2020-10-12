package com.mapobed.tutionfeecollector.Home.Teacher.Payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapobed.tutionfeecollector.R;

import java.util.List;

public class MonthBasedAdapter extends RecyclerView.Adapter<MonthBasedAdapter.MonthBasedHolder> {
    private List<MonthBasedModelClass>list;
    MonthlyBasedInterface monthlyBasedInterface;

    public MonthBasedAdapter(List<MonthBasedModelClass> list,MonthlyBasedInterface monthlyBasedInterface) {
        this.list = list;
        this.monthlyBasedInterface=monthlyBasedInterface;
    }

    @NonNull
    @Override
    public MonthBasedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MonthBasedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.month_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MonthBasedHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.pic.setImageResource(list.get(position).getPic());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MonthBasedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView pic;
        TextView name;
        public MonthBasedHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.pic);
            name=itemView.findViewById(R.id.pic_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            monthlyBasedInterface.monthClicked(list.get(getPosition()));
        }
    }
    public interface MonthlyBasedInterface{
        void monthClicked(MonthBasedModelClass pos_of_month);
    }

}
