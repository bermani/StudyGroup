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
import com.isaacbfbu.studygroup.databinding.FragmentCreateCourseBinding;
import com.isaacbfbu.studygroup.databinding.FragmentCreateTextPostBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class CreateCourseFragment extends Fragment {

    MainActivity activity;
    FragmentCreateCourseBinding binding;

    public CreateCourseFragment() {
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
        binding = FragmentCreateCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // button logic
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Course course = new Course();
                course.setTitle(binding.etTitle.getText().toString());
                activity.setMyProgressBarVisibility(true);
                course.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        final ParseUser user = ParseUser.getCurrentUser();
                        user.addUnique("enrolled", course.getObjectId());
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                activity.setMyProgressBarVisibility(false);
                                activity.goHome();
                            }
                        });
                    }
                });
            }
        });
    }
}