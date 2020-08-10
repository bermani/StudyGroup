package com.isaacbfbu.studygroup.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.databinding.FragmentPostDetailBinding;
import com.isaacbfbu.studygroup.models.TextPost;
import com.isaacbfbu.studygroup.utils.ParseUtils;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostDetailFragment extends Fragment {

    private static final String POST_PARAM = "post";

    MainActivity activity;
    FragmentPostDetailBinding binding;
    AlertDialog.Builder alertDialogBuilder;

    private TextPost post;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(TextPost post) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST_PARAM, post);
        fragment.setArguments(args);
        return fragment;
    }

    private void configureAlertDialogBuilder() {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        if (post.getAuthor().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            alertDialogBuilder.setTitle("are you sure you want to delete?");
            alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.setMyProgressBarVisibility(true);
                    post.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(activity, "There was an error when attempting to delete your post", Toast.LENGTH_SHORT).show();
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
                    post.addToReports();
                    activity.setMyProgressBarVisibility(true);
                    post.saveInBackground(new SaveCallback() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (getArguments() != null) {
            post = getArguments().getParcelable(POST_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureAlertDialogBuilder();

        binding.itemPost.tvContent.setText(post.getContent());
        binding.itemPost.tvName.setText(post.getAuthorName());
        binding.itemPost.tvCourse.setText(post.getCourseTitle());
        String url = ParseUtils.getProfilePhotoURL(post.getAuthor());
        Glide.with(activity).load(url).placeholder(R.drawable.person_24px).circleCrop().into(binding.itemPost.ivProfilePhoto);
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(activity).load(image.getUrl()).into(binding.itemPost.ivPost);
        } else {
            binding.itemPost.ivPost.setVisibility(View.GONE);
        }

        binding.itemPost.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetailFragment fragment = UserDetailFragment.newInstance(post.getAuthor());
                activity.goForward(fragment);
            }
        });

        binding.itemPost.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetailFragment fragment = UserDetailFragment.newInstance(post.getAuthor());
                activity.goForward(fragment);
            }
        });

        binding.itemPost.tvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseDetailFragment fragment = CourseDetailFragment.newInstance(post.getCourse());
                activity.goForward(fragment);
            }
        });


        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.create().show();
            }
        });
        if (post.getAuthor().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            binding.btnReport.setText("Delete");
        }
    }
}