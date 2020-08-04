package com.isaacbfbu.studygroup.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentCreateImagePostBinding;
import com.isaacbfbu.studygroup.databinding.FragmentCreateTextPostBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class CreateImagePostFragment extends Fragment {

    public static final String TAG = "CreateImagePostFragment";

    MainActivity activity;
    FragmentCreateImagePostBinding binding;

    private File selectedFile;


    public CreateImagePostFragment() {
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
        binding = FragmentCreateImagePostBinding.inflate(inflater, container, false);
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

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateImagePostFragment.this)
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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
                post.setImage(new ParseFile(selectedFile));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);
            Glide.with(activity).load(file).into(binding.imageView);
            binding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            selectedFile = file;
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}