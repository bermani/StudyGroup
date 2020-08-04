package com.isaacbfbu.studygroup.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.R;
import com.isaacbfbu.studygroup.adapters.CalendarAdapter;
import com.isaacbfbu.studygroup.adapters.FeedAdapter;
import com.isaacbfbu.studygroup.databinding.FragmentCalendarBinding;
import com.isaacbfbu.studygroup.models.Assignment;
import com.isaacbfbu.studygroup.utils.JSONArrayUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalendarFragment extends Fragment {

    MainActivity activity;
    FragmentCalendarBinding binding;

    List<Assignment> results;
    CalendarAdapter adapter;

    Map<String, List<Assignment>> map;

    final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarFragment() {
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
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // calendarView
        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(CalendarDay.today());
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.circle));
                view.addSpan(new ForegroundColorSpan(Color.RED));
            }
        });
        binding.calendarView.setSelectedDate(CalendarDay.today());

        // RecyclerView setup
        RecyclerView rvEvents = binding.rvEvents;
        results = new ArrayList<>();
        adapter = new CalendarAdapter(activity, results, this);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(activity));

        map = new HashMap();
        getPosts();

        binding.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String selectedDate = localDateFormatter.format(date.getDate());
                if (selected) {
                    adapter.clear();
                    if (map.containsKey(selectedDate)) {
                        adapter.addAll(map.get(selectedDate));
                    }
                }
            }
        });
    }

    private void getPosts() {
        ParseQuery<Assignment> query = ParseQuery.getQuery("Assignment");

        List<String> list = JSONArrayUtils.jsonArrayToArrayList(ParseUser.getCurrentUser().getJSONArray("enrolled"));

        query.whereContainedIn(Assignment.KEY_COURSE, list);
        query.include("author");

        activity.setMyProgressBarVisibility(true);
        query.findInBackground(new FindCallback<Assignment>() {
            @Override
            public void done(List<Assignment> objects, ParseException e) {

                final Set<String> dates = new HashSet<>();
                for (Assignment assignment : objects) {
                    String dueDate = dateFormatter.format(assignment.getDueDate());
                    dates.add(dueDate);
                    if (!map.containsKey(dueDate)) {
                        map.put(dueDate, new ArrayList<Assignment>());
                    }
                    map.get(dueDate).add(assignment);
                }

                binding.calendarView.addDecorator(new DayViewDecorator() {

                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        String dateString = localDateFormatter.format(day.getDate());
                        return dates.contains(dateString);
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new DotSpan(5, Color.RED));
                    }
                });
                activity.setMyProgressBarVisibility(false);
            }
        });
    }
}