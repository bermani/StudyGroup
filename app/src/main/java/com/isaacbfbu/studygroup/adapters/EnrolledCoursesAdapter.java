package com.isaacbfbu.studygroup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemCourseBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.utils.BindingUtils;

import java.util.List;

public class EnrolledCoursesAdapter extends RecyclerView.Adapter<EnrolledCoursesAdapter.ViewHolder> {

    private static final String TAG = "EnrolledCoursesAdapter";

    MainActivity context;
    List<Course> courses;
    Fragment fragment;

    public EnrolledCoursesAdapter(MainActivity context, List<Course> courses, Fragment fragment) {
        this.context = context;
        this.courses = courses;
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
        holder.bind(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return courses.size();
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
        courses.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Course> list) {
        courses.addAll(list);
        notifyDataSetChanged();
    }


}
