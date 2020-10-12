package com.mapobed.tutionfeecollector.Home.Teacher.Payments.StudentByMonth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapobed.tutionfeecollector.R;

import java.util.List;

public class PaidStudentAdapter extends RecyclerView.Adapter<PaidStudentAdapter.PaidStudentHolder> {
    private List<PaidStudentModelClass>list;

    public PaidStudentAdapter(List<PaidStudentModelClass> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PaidStudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaidStudentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.paid_student_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaidStudentHolder holder, int position) {
        holder.name.setText(list.get(position).getPaid_name());
        holder.username.setText(list.get(position).getPaid_user_name());
        holder.mode.setText(list.get(position).getPaid_mode());
        holder.date.setText(list.get(position).getPaid_date());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaidStudentHolder extends RecyclerView.ViewHolder {
        TextView name,username,mode,date;
        public PaidStudentHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.paid_stu_name);
            username=itemView.findViewById(R.id.paid_stu_user_name);
            mode=itemView.findViewById(R.id.paid_stu_mode);
            date=itemView.findViewById(R.id.paid_stu_payment_date);

        }
    }
}
