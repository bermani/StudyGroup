package com.isaacbfbu.studygroup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.ItemResultBinding;
import com.isaacbfbu.studygroup.fragments.SearchFragment;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
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

        public void bind(final Course course) {
            binding.tvTitle.setText(course.getTitle());
            if (JSONArrayUtils.arrayContains(ParseUser.getCurrentUser().getJSONArray("enrolled"), course.getObjectId())) {
                binding.btnEnroll.setText("unenroll");
            } else {
                binding.btnEnroll.setText("enroll");
            }
            binding.btnEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ParseUser user = ParseUser.getCurrentUser();
                    if (JSONArrayUtils.arrayContains(user.getJSONArray("enrolled"), course.getObjectId())) {
                        ParseUser.getCurrentUser().removeAll("enrolled", Arrays.asList(course.getObjectId()));
                    } else {
                        ParseUser.getCurrentUser().addUnique("enrolled", course.getObjectId());
                    }
                    context.setMyProgressBarVisibility(true);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            context.setMyProgressBarVisibility(false);
                            if (JSONArrayUtils.arrayContains(user.getJSONArray("enrolled"), course.getObjectId())) {
                                binding.btnEnroll.setText("unenroll");
                            } else {
                                binding.btnEnroll.setText("enroll");
                            }
                        }
                    });
                }
            });
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
