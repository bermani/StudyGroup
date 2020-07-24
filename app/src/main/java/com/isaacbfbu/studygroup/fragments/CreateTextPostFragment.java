package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentCreateTextPostBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CreateTextPostFragment extends Fragment {

    public static final String TAG = "CreateTextPostFragment";

    MainActivity activity;
    FragmentCreateTextPostBinding binding;


    public CreateTextPostFragment() {
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
        binding = FragmentCreateTextPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // query to setup spinner
        ParseQuery<Course> query = ParseQuery.getQuery("Course");
        query.whereContainedIn("objectId", JSONArrayUtils.jsonArrayToArrayList(ParseUser.getCurrentUser().getJSONArray("enrolled")));
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error getting courses", e);
                    return;
                }
                ArrayAdapter<Course> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, objects);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinner.setAdapter(adapter);
            }
        });

        // submit button
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextPost post = new TextPost();
                post.setAuthor(ParseUser.getCurrentUser());
                Course selectedCourse = (Course) binding.spinner.getSelectedItem();
                post.setCourse(selectedCourse);
                post.setContent(binding.etContent.getText().toString());
                activity.setMyProgressBarVisibility(true);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        activity.setMyProgressBarVisibility(false);
                        activity.goHome();
                    }
                });
            }
        });
    }
}