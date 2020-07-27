package com.isaacbfbu.studygroup.fragments;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentCreateAssignmentBinding;
import com.isaacbfbu.studygroup.models.Assignment;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class CreateAssignmentFragment extends Fragment {

    public static final String TAG = "CreateAssignmentFragmen";

    MainActivity activity;
    FragmentCreateAssignmentBinding binding;

    public CreateAssignmentFragment() {
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
        binding = FragmentCreateAssignmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.datePicker.setMinDate(System.currentTimeMillis() - 1000);

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

        // submit button setup
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assignment assignment = new Assignment();
                assignment.setAuthor(ParseUser.getCurrentUser());
                Course selectedCourse = (Course) binding.spinner.getSelectedItem();
                assignment.setCourse(selectedCourse);
                assignment.setTitle(binding.etTitle.getText().toString());
                assignment.setDate(getDateFromDatePicker(binding.datePicker));
                activity.setMyProgressBarVisibility(true);
                assignment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error saving assignment", e);
                            return;
                        }
                        activity.setMyProgressBarVisibility(false);
                        activity.goHome();
                    }
                });
            }
        });
    }

    private static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}