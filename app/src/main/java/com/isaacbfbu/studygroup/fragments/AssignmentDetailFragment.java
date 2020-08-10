package com.isaacbfbu.studygroup.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentAssignmentDetailBinding;
import com.isaacbfbu.studygroup.databinding.ItemAssignmentBinding;
import com.isaacbfbu.studygroup.models.Assignment;
import com.isaacbfbu.studygroup.utils.BindingUtils;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AssignmentDetailFragment extends Fragment {

    private static final String ASSIGNMENT = "assignment";

    private Assignment assignment;
    MainActivity activity;
    AlertDialog.Builder alertDialogBuilder;

    private FragmentAssignmentDetailBinding binding;

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
        activity = (MainActivity) getActivity();
        if (getArguments() != null) {
            assignment = getArguments().getParcelable(ASSIGNMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAssignmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void configureAlertDialogBuilder() {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        if (assignment.getAuthor().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            alertDialogBuilder.setTitle("are you sure you want to delete?");
            alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.setMyProgressBarVisibility(true);
                    assignment.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(activity, "There was an error when attempting to delete your assignment", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Your post was deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                            activity.setMyProgressBarVisibility(false);
                            activity.goHome();
                        }
                    });
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
        } else {
            alertDialogBuilder.setTitle("why are you reporting this post?");
            alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    assignment.addToReports();
                    activity.setMyProgressBarVisibility(true);
                    assignment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(activity, "There was an error recording your report", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Your report was recorded successfully", Toast.LENGTH_SHORT).show();
                            }
                            activity.setMyProgressBarVisibility(false);
                            activity.goHome();

                        }
                    });
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            alertDialogBuilder.setMultiChoiceItems(new String[]{"it's offensive", "it's inappropriate", "it's irrelevant to the course"}, null, null);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureAlertDialogBuilder();

        BindingUtils.bindEventItem((MainActivity) getActivity(), binding.itemAssignment, assignment);
        binding.tvDescription.setText(assignment.getDescription());
        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.create().show();
            }
        });
        if (assignment.getAuthor().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            binding.btnReport.setText("Delete");
        }
    }
}