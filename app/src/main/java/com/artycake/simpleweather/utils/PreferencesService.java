package com.artycake.simpleweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by artycake on 2/13/17.
 */
public class PreferencesService {
    private SharedPreferences preferences;

    private static PreferencesService ourInstance = null;
    public static final String LOCATION_CITY_ID = "location_city_id";
    public static final String LOCATION_CITY_NAME = "location_city_name";
    public static final String LAST_UPDATE_TIMESTAMP = "last_update_timestamp";
    public static final String LOCATION_CHANGED = "location_changed";
    public static final String UNITS_CHANGED = "units_changed";
    public static final String FIRST_LAUNCH = "first_launch";
    public static final String UNITS = "units";
    public static final String DONT_SHOW_RATE = "dont_show_rate";
    public static final String LAUNCH_COUNT = "launch_count";
    public static final String FIRST_LAUNCH_TIME = "first_launch_time";
    public static final int CELSIUS = 779;
    public static final int FAHRENHEIT = 454;
    public static final long UPDATE_PERIOD = 900000;

    public static PreferencesService getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new PreferencesService(context);
        }
        return ourInstance;
    }

    private PreferencesService(Context context) {
        preferences = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
    }

    public void putPreferences(String name, String value) {
        preferences.edit().putString(name, value).apply();
    }

    public void putPreferences(String name, int value) {
        preferences.edit().putInt(name, value).apply();
    }

    public void putPreferences(String name, long value) {
        preferences.edit().putLong(name, value).apply();
    }

    public void putPreferences(String name, boolean value) {
        preferences.edit().putBoolean(name, value).apply();
    }

    public String getStringPref(String name, @Nullable String defaultValue) {
        return preferences.getString(name, defaultValue);
    }

    public int getIntPref(String name, int defaultValue) {
        return preferences.getInt(name, defaultValue);
    }

    public long getLongPref(String name, long defaultValue) {
        return preferences.getLong(name, defaultValue);
    }

    public boolean getBoolPref(String name, boolean defaultValue) {
        return preferences.getBoolean(name, defaultValue);
    }
}
