package com.isaacbfbu.studygroup.utils;

import com.parse.ParseFile;
import com.parse.ParseUser;

public class ParseUtils {
    public static String getUsername(ParseUser user) {
        if (user.getString("displayName") == null) {
            return user.getUsername();
        } else {
            return user.getString("displayName");
        }
    }

    public static String getProfilePhotoURL(ParseUser user) {
        if (user.getString("facebookProfileURL") != null) {
            return user.getString("facebookProfileURL");
        } else {
            ParseFile image = user.getParseFile("profilePhoto");
            String url = "";
            if (image != null) {
                url = image.getUrl();
            }
            return url;
        }
    }
}
