package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    MainActivity activity;

    public AddFragment() {
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
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        binding.ivText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goForward(new CreateTextPostFragment());
            }
        });

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goForward(new CreateImagePostFragment());
            }
        });

        binding.ivClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goForward(new CreateCourseFragment());
            }
        });
        binding.ivAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goForward(new CreateAssignmentFragment());
            }
        });
    }
}