package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.adapters.FeedAdapter;
import com.isaacbfbu.studygroup.databinding.FragmentHomeBinding;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    MainActivity activity;
    FragmentHomeBinding binding;
    SwipeRefreshLayout swipeContainer;

    List<TextPost> results;
    FeedAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // RecyclerView setup
        RecyclerView rvResults = binding.rvResults;
        results = new ArrayList<>();
        adapter = new FeedAdapter(activity, results, this);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(new LinearLayoutManager(activity));

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                getPosts();
            }
        });

        getPosts();
    }

    private void getPosts() {
        ParseQuery<TextPost> query = ParseQuery.getQuery("TextPost");

        List<String> list = JSONArrayUtils.jsonArrayToArrayList(ParseUser.getCurrentUser().getJSONArray("enrolled"));

        query.whereContainedIn(TextPost.KEY_COURSE, list);
        query.addDescendingOrder("createdAt");
        query.whereNotEqualTo("reports", ParseUser.getCurrentUser().getObjectId());

        query.include(TextPost.KEY_AUTHOR);
        query.include(TextPost.KEY_COURSE);

        activity.setMyProgressBarVisibility(true);
        query.findInBackground(new FindCallback<TextPost>() {
            @Override
            public void done(List<TextPost> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting posts", e);
                    return;
                }
                adapter.addAll(objects);
                swipeContainer.setRefreshing(false);
                activity.setMyProgressBarVisibility(false);
            }
        });
    }
}