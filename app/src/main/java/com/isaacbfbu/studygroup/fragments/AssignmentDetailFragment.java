package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.models.Assignment;

public class AssignmentDetailFragment extends Fragment {

    private static final String ASSIGNMENT = "assignment";

    private Assignment assignment;

    public AssignmentDetailFragment() {
        // Required empty public constructor
    }

    public static AssignmentDetailFragment newInstance(Assignment assignment) {
        AssignmentDetailFragment fragment = new AssignmentDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ASSIGNMENT, assignment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assignment = getArguments().getParcelable(ASSIGNMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignment_detail, container, false);
    }
}