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
import com.isaacbfbu.studygroup.databinding.FragmentCourseDetailBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.BindingUtils;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseDetailFragment extends Fragment {

    private static final String COURSE_PARAM = "course";

    MainActivity activity;
    FragmentCourseDetailBinding binding;
    SwipeRefreshLayout swipeContainer;

    List<TextPost> results;
    FeedAdapter adapter;

    private Course course;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    public static CourseDetailFragment newInstance(Course course) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(COURSE_PARAM, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (getArguments() != null) {
            course = getArguments().getParcelable(COURSE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BindingUtils.bindCourseItem(activity, binding.itemCourse, course);
        // RecyclerView setup
        RecyclerView rvResults = binding.rvPosts;
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

        List<String> list = Arrays.asList(course.getObjectId());

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
                    Log.e("CourseDetailFragment", "Issue getting posts", e);
                    return;
                }
                adapter.addAll(objects);
                swipeContainer.setRefreshing(false);
                activity.setMyProgressBarVisibility(false);
            }
        });
    }
}