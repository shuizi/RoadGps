package com.rca_gps.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wafer on 2016/10/31.
 */

public class ShareP {
    public static void saveJpshId(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().putString("id", id).commit();
    }

    public static String getJpshId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences.getString("id", "");
    }

    public static void saveUserId(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().putString("userId", id).commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences.getString("userId", "");
    }

    public static void removeUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().remove("userId").commit();
    }

    public static void saveUserName(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().putString("userName", id).commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences.getString("userName", "");
    }

    public static void removeUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().remove("userName").commit();
    }

    public static void saveUserPic(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().putString("userPic", id).commit();
    }

    public static String getUserPic(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences.getString("userPic", "");
    }

    public static void removeUserPic(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Jpsh", Context.MODE_WORLD_WRITEABLE);
        sharedPreferences.edit().remove("userPic").commit();
    }
}
