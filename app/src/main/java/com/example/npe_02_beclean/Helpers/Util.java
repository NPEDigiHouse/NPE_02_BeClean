package com.example.npe_02_beclean.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Util {
    // shared preferences attr
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String USER_ID_KEY = "user_id_key";

    public static void saveUserIdToLocal(Activity activity, String userId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public static String getUserIdLocal(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID_KEY, "");
    }
}
