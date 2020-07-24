package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentCourseDetailBinding;
import com.isaacbfbu.studygroup.databinding.FragmentUserDetailBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.parse.ParseUser;

public class CourseDetailFragment extends Fragment {

    private static final String COURSE_PARAM = "course";

    MainActivity activity;
    FragmentCourseDetailBinding binding;

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

        binding.tvName.setText(course.getTitle());
    }
}