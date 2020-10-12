package com.mapobed.tutionfeecollector.Home.Student.Mates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class MateStudentAdapter extends RecyclerView.Adapter<MateStudentAdapter.MateStudentHolder> implements Filterable {
    private List<StudentMatesModelClass> list;
    private List<StudentMatesModelClass> listFiltered;

    private clicker clicker;
    public MateStudentAdapter(List<StudentMatesModelClass> list,clicker clicker) {
        this.list = list;
        this.clicker=clicker;
        listFiltered =new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MateStudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MateStudentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mate_student_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MateStudentHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.college.setText(list.get(position).getCollege());
        holder.phone.setText(list.get(position).getPhone());
        holder.email.setText(list.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MateStudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, college,email,phone;

        public MateStudentHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mate_name);
            college = itemView.findViewById(R.id.mate_college);
            email=itemView.findViewById(R.id.mate_email);
            phone=itemView.findViewById(R.id.mate_phone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clicker.clickked(list.get(getAdapterPosition()));
        }
    }
    public interface clicker{
        void clickked(StudentMatesModelClass matesTeacherModelClass);
    }

    @Override
    public  Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StudentMatesModelClass> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (StudentMatesModelClass item : list) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
