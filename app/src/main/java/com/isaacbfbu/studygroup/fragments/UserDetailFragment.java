package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.adapters.EnrolledCoursesAdapter;
import com.isaacbfbu.studygroup.databinding.FragmentUserDetailBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserDetailFragment extends Fragment {

    private static final String USER_PARAM = "user";

    MainActivity activity;
    FragmentUserDetailBinding binding;

    SwipeRefreshLayout swipeContainer;
    List<Course> courses;
    EnrolledCoursesAdapter adapter;

    private ParseUser user;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(ParseUser user) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_PARAM, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ParseUser currentUser = user;
        binding.tvUsername.setText(currentUser.getUsername());
        ParseFile image = currentUser.getParseFile("profilePhoto");
        String url = "";
        if (image != null) {
            url = image.getUrl();
        }
        Glide.with(activity).load(url).placeholder(R.drawable.person_24px).circleCrop().into(binding.ivProfilePhoto);

        final Fragment that = this;
        binding.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(that)
                        .crop(1f, 1f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        // RecyclerView setup
        RecyclerView rvCourses = binding.rvCourses;
        courses = new ArrayList<>();
        adapter = new EnrolledCoursesAdapter(activity, courses, this);
        rvCourses.setAdapter(adapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(activity));

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                getCourses(currentUser);
            }
        });

        getCourses(currentUser);
    }

    private void getCourses(ParseUser currentUser) {
        ParseQuery<Course> query = new ParseQuery("Course");
        query.whereContainedIn("objectId", JSONArrayUtils.jsonArrayToArrayList(currentUser.getJSONArray("enrolled")));
        activity.setMyProgressBarVisibility(true);
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> objects, ParseException e) {
                swipeContainer.setRefreshing(false);
                activity.setMyProgressBarVisibility(false);
                adapter.clear();
                adapter.addAll(objects);
            }
        });
    }
}