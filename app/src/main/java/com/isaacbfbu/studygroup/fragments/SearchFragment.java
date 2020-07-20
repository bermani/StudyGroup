package com.isaacbfbu.studygroup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.adapters.ResultsAdapter;
import com.isaacbfbu.studygroup.databinding.FragmentSearchBinding;
import com.isaacbfbu.studygroup.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    private FragmentSearchBinding binding;
    private MainActivity activity;
    private MenuItem miProgressBar;

    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;

    private ResultsAdapter adapter;
    private List<Course> results;



    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        miProgressBar = menu.findItem(R.id.miActionProgress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView setup
        RecyclerView rvResults = binding.rvResults;
        results = new ArrayList<>();
        adapter = new ResultsAdapter(activity, results, this);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(new LinearLayoutManager(activity));

        // Search setup
        binding.svSearch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ParseQuery<Course> query = ParseQuery.getQuery("Course");
                query.whereContains("title",s);

                setProgressBarVisibility(true);

                query.findInBackground(new FindCallback<Course>() {
                    @Override
                    public void done(List<Course> courses, ParseException e) {
                        setProgressBarVisibility(false);
                        if (e != null) {
                            Log.e(TAG, "Issue getting search results", e);
                            return;
                        }
                        adapter.clear();
                        adapter.addAll(courses);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public void setProgressBarVisibility(boolean b) {
        miProgressBar.setVisible(b);
    }
}