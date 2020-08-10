package com.isaacbfbu.studygroup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemPostBinding;
import com.isaacbfbu.studygroup.fragments.CourseDetailFragment;
import com.isaacbfbu.studygroup.fragments.HomeFragment;
import com.isaacbfbu.studygroup.fragments.PostDetailFragment;
import com.isaacbfbu.studygroup.fragments.UserDetailFragment;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.ParseUtils;
import com.parse.ParseFile;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    MainActivity context;
    List<TextPost> results;
    Fragment fragment;

    public FeedAdapter(MainActivity context, List<TextPost> results, Fragment fragment) {
        this.context = context;
        this.results = results;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
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

        ItemPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
        }

        public void bind(final TextPost textPost) {
            binding.tvContent.setText(textPost.getContent());
            binding.tvName.setText(textPost.getAuthorName());
            binding.tvCourse.setText(textPost.getCourseTitle());
            String url = ParseUtils.getProfilePhotoURL(textPost.getAuthor());
            Glide.with(context).load(url).placeholder(R.drawable.person_24px).circleCrop().into(binding.ivProfilePhoto);
            ParseFile image = textPost.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(binding.ivPost);
            } else {
                binding.ivPost.setVisibility(View.GONE);
            }

            binding.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostDetailFragment fragment = PostDetailFragment.newInstance(textPost);
                    context.goForward(fragment);
                }
            });

            binding.ivPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostDetailFragment fragment = PostDetailFragment.newInstance(textPost);
                    context.goForward(fragment);
                }
            });

            binding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDetailFragment fragment = UserDetailFragment.newInstance(textPost.getAuthor());
                    context.goForward(fragment);
                }
            });

            binding.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDetailFragment fragment = UserDetailFragment.newInstance(textPost.getAuthor());
                    context.goForward(fragment);
                }
            });

            binding.tvCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CourseDetailFragment fragment = CourseDetailFragment.newInstance(textPost.getCourse());
                    context.goForward(fragment);
                }
            });
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
