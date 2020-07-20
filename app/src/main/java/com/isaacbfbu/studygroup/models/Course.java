package com.isaacbfbu.studygroup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Course")
public class Course extends ParseObject {

    public static final String KEY_TITLE = "title";

    public Course() {}

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String toString() {
        return getTitle();
    }
}
