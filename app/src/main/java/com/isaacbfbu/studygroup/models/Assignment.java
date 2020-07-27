package com.isaacbfbu.studygroup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Assignment")
public class Assignment extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_COURSE = "course";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DATE = "dueDate";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public Course getCourse() {
        return (Course) getParseObject(KEY_COURSE);
    }

    public void setCourse(Course course) {
        put(KEY_COURSE, course);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public String getCourseTitle() {
        return getParseObject(KEY_COURSE).getString(Course.KEY_TITLE);
    }

    public String getAuthorName() {
        return getParseUser(KEY_AUTHOR).getUsername();
    }

    public Date getDueDate() {
        return getDate(KEY_DATE);
    }

    public void setDate(Date date) {
        put(KEY_DATE, date);
    }
}
