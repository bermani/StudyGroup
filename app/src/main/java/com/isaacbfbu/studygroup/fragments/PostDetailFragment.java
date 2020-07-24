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
import com.isaacbfbu.studygroup.databinding.FragmentPostDetailBinding;
import com.isaacbfbu.studygroup.models.TextPost;

public class PostDetailFragment extends Fragment {

    private static final String POST_PARAM = "post";

    MainActivity activity;
    FragmentPostDetailBinding binding;

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

        binding.tvName.setText(post.getAuthorName());
        binding.tvContent.setText(post.getContent());
    }
}