package com.isaacbfbu.studygroup.models;

import com.isaacbfbu.studygroup.utils.ParseUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("TextPost")
public class TextPost extends ParseObject {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COURSE = "course";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_REPORTS = "reports";

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
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
        return ParseUtils.getUsername(getParseUser(KEY_AUTHOR));
    }

    public JSONArray getReports() {
        return getJSONArray(KEY_REPORTS);
    }

    public void addToReports() {
        String userId = ParseUser.getCurrentUser().getObjectId();
        add(KEY_REPORTS, userId);
    }
}
