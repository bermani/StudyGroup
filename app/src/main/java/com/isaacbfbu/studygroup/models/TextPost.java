package com.isaacbfbu.studygroup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TextPost")
public class TextPost extends ParseObject {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COURSE = "course";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_COURSE_TITLE = "courseTitle";
    public static final String KEY_AUTHOR_NAME = "authorName";

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public String getCourseId() {
        return getParseObject(KEY_COURSE).getObjectId();
    }

    public void setCourseId(Course course) {
        put(KEY_COURSE, course);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public String getCourseTitle() {
        return getString(KEY_COURSE_TITLE);
    }

    public void setCourseTitle(String title) {
        put(KEY_COURSE_TITLE, title);
    }

    public String getAuthorName() {
        return getString(KEY_AUTHOR_NAME);
    }

    public void setAuthorName(String name) {
        put(KEY_AUTHOR_NAME, name);
    }
}
