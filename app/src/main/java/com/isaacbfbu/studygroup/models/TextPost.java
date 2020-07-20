package com.isaacbfbu.studygroup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TextPost")
public class TextPost extends ParseObject {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COURSE = "course";
    public static final String KEY_AUTHOR = "author";

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
        return getParseObject(KEY_COURSE).getString(Course.KEY_TITLE);
    }

    public String getAuthorName() {
        return getParseUser(KEY_AUTHOR).getUsername();
    }
}
