package com.isaacbfbu.studygroup.utils;

import android.util.Log;
import android.view.View;

import com.isaacbfbu.studygroup.MainActivity;
import com.isaacbfbu.studygroup.databinding.ItemAssignmentBinding;
import com.isaacbfbu.studygroup.databinding.ItemCourseBinding;
import com.isaacbfbu.studygroup.fragments.AssignmentDetailFragment;
import com.isaacbfbu.studygroup.fragments.CourseDetailFragment;
import com.isaacbfbu.studygroup.models.Assignment;
import com.isaacbfbu.studygroup.models.Course;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

public class BindingUtils {
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a, MMM d");

    public static void bindCourseItem(final MainActivity context, final ItemCourseBinding binding, final Course course) {
        binding.tvTitle.setText(course.getTitle());
        binding.tvEnrolled.setText(course.getEnrolledCount() + " enrolled");

        binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goForward(CourseDetailFragment.newInstance(course));
            }
        });

        if (JSONArrayUtils.arrayContains(ParseUser.getCurrentUser().getJSONArray("enrolled"), course.getObjectId())) {
            binding.btnEnroll.setText("unenroll");
        } else {
            binding.btnEnroll.setText("enroll");
        }

        binding.btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user = ParseUser.getCurrentUser();
                if (JSONArrayUtils.arrayContains(user.getJSONArray("enrolled"), course.getObjectId())) {
                    ParseUser.getCurrentUser().removeAll("enrolled", Arrays.asList(course.getObjectId()));
                } else {
                    ParseUser.getCurrentUser().addUnique("enrolled", course.getObjectId());
                }
                context.setMyProgressBarVisibility(true);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        context.setMyProgressBarVisibility(false);
                        if (JSONArrayUtils.arrayContains(user.getJSONArray("enrolled"), course.getObjectId())) {
                            binding.btnEnroll.setText("unenroll");
                        } else {
                            binding.btnEnroll.setText("enroll");
                        }
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("courseId", course.getObjectId());
                        ParseCloud.callFunctionInBackground("updateCountEnrolled", params, new FunctionCallback<Integer>() {
                            @Override
                            public void done(Integer count, ParseException e) {
                                if (e != null) {
                                    Log.i("Binding to Course", "Error saving enrolled", e);
                                } else {
                                    binding.tvEnrolled.setText(count + " enrolled");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public static void bindEventItem(final MainActivity context, final ItemAssignmentBinding binding, final Assignment event) {
        binding.tvTitle.setText(event.getTitle());
        binding.tvAuthor.setText("posted by " + event.getAuthorName());
        binding.tvDueDate.setText("due at " + dateFormatter.format(event.getDueDate()));

        binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goForward(AssignmentDetailFragment.newInstance(event));
            }
        });

        if (JSONArrayUtils.arrayContains(ParseUser.getCurrentUser().getJSONArray("subscribedAssignments"), event.getObjectId())) {
            binding.btnSubscribe.setText("unsubscribe");
        } else {
            binding.btnSubscribe.setText("subscribe");
        }

        binding.btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user = ParseUser.getCurrentUser();
                if (JSONArrayUtils.arrayContains(user.getJSONArray("subscribedAssignments"), event.getObjectId())) {
                    ParseUser.getCurrentUser().removeAll("subscribedAssignments", Arrays.asList(event.getObjectId()));
                } else {
                    ParseUser.getCurrentUser().addUnique("subscribedAssignments", event.getObjectId());
                }
                context.setMyProgressBarVisibility(true);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        context.setMyProgressBarVisibility(false);
                        if (JSONArrayUtils.arrayContains(user.getJSONArray("subscribedAssignments"), event.getObjectId())) {
                            binding.btnSubscribe.setText("unsubscribe");
                        } else {
                            binding.btnSubscribe.setText("subscribe");
                        }
                    }
                });
            }
        });
    }
}
