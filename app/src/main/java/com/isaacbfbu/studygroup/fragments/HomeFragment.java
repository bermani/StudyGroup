package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.adapters.FeedAdapter;
import com.isaacbfbu.studygroup.databinding.FragmentHomeBinding;
import com.isaacbfbu.studygroup.models.TextPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    MainActivity activity;
    FragmentHomeBinding binding;

    List<TextPost> results;
    FeedAdapter adapter;

    public HomeFragment() {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // RecyclerView setup
        RecyclerView rvResults = binding.rvResults;
        results = new ArrayList<>();
        adapter = new FeedAdapter(activity, results, this);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(new LinearLayoutManager(activity));

        // query
        ParseQuery<TextPost> query = ParseQuery.getQuery("TextPost");

        List<String> list = jsonArrayToArrayList(ParseUser.getCurrentUser().getJSONArray("enrolled"));

        query.whereContainedIn(TextPost.KEY_COURSE, list);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<TextPost>() {
            @Override
            public void done(List<TextPost> objects, ParseException e) {
                adapter.addAll(objects);
            }
        });
    }

    private List<String> jsonArrayToArrayList(JSONArray array) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            try {
                result.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}