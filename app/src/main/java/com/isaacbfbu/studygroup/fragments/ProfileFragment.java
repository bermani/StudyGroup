package com.isaacbfbu.studygroup.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.isaacbfbu.studygroup.LoginActivity;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentProfileBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ProfileFragment extends Fragment {

    MainActivity activity;
    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ParseUser currentUser = ParseUser.getCurrentUser();
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

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.logout();
                activity.finish();
                Intent i = new Intent(activity, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);

            final ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.put("profilePhoto", new ParseFile(file));
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(activity, "Error saving profile photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(activity, "Profile photo saved successfully", Toast.LENGTH_SHORT).show();
                    String url = currentUser.getParseFile("profilePhoto").getUrl();
                    Glide.with(activity).load(url).placeholder(R.drawable.person_24px).circleCrop().into(binding.ivProfilePhoto);
                }
            });

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}