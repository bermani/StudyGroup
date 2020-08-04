package com.isaacbfbu.studygroup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemAssignmentBinding;
import com.isaacbfbu.studygroup.databinding.ItemCourseBinding;
import com.isaacbfbu.studygroup.models.Assignment;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.utils.BindingUtils;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private static final String TAG = "CalendarAdapter";

    MainActivity context;
    List<Assignment> events;
    Fragment fragment;

    public CalendarAdapter(MainActivity context, List<Assignment> events, Fragment fragment) {
        this.context = context;
        this.events = events;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemAssignmentBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAssignmentBinding.bind(itemView);
        }

        public void bind(final Assignment event) {
            BindingUtils.bindEventItem(context, binding, event);
        }
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Assignment> list) {
        events.addAll(list);
        notifyDataSetChanged();
    }


}
