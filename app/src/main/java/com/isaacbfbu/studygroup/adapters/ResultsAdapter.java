package com.isaacbfbu.studygroup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemResultBinding;
import com.isaacbfbu.studygroup.models.Course;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    Context context;
    List<Course> results;

    public ResultsAdapter(Context context, List<Course> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemResultBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemResultBinding.bind(itemView);
        }

        public void bind(Course course) {
            binding.tvTitle.setText(course.getTitle());
        }
    }

    public void clear() {
        results.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Course> list) {
        results.addAll(list);
        notifyDataSetChanged();
    }
}
