package com.isaacbfbu.studygroup.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JSONArrayUtils {
    public static List<String> jsonArrayToArrayList(JSONArray array) {
        ArrayList<String> result = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); ++i) {
                try {
                    result.add(array.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean arrayContains(JSONArray array, String value) {
        if (array != null) {
            for (int i = 0; i < array.length(); ++i) {
                try {
                    if (array.getString(i).equals(value)) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
