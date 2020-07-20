package com.isaacbfbu.studygroup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemFeedBinding;
import com.isaacbfbu.studygroup.fragments.HomeFragment;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.models.TextPost;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    MainActivity context;
    List<TextPost> results;
    HomeFragment fragment;

    public FeedAdapter(MainActivity context, List<TextPost> results, HomeFragment fragment) {
        this.context = context;
        this.results = results;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new FeedAdapter.ViewHolder(view);
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

        ItemFeedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFeedBinding.bind(itemView);
        }

        public void bind(TextPost textPost) {
            binding.tvContent.setText(textPost.getContent());
            binding.tvName.setText(textPost.getAuthorName());
            binding.tvCourse.setText(textPost.getCourseTitle());
        }
    }

    public void clear() {
        results.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TextPost> list) {
        results.addAll(list);
        notifyDataSetChanged();
    }
}
