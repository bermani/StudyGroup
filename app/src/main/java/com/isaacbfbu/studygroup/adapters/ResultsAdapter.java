package com.isaacbfbu.studygroup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemCourseBinding;
import com.isaacbfbu.studygroup.fragments.SearchFragment;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.utils.BindingUtils;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private static final String TAG = "ResultsAdapter";

    MainActivity context;
    List<Course> results;
    SearchFragment fragment;

    public ResultsAdapter(MainActivity context, List<Course> results, SearchFragment fragment) {
        this.context = context;
        this.results = results;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
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

        ItemCourseBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCourseBinding.bind(itemView);
        }

        public void bind(final Course course) {
            BindingUtils.bindCourseItem(context, binding, course);
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
