package com.artycake.simpleweather.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by artycake on 2/22/17.
 */

public class Localization {
    private final static Locale russian = new Locale("ru", "RU");
    private final static List<Locale> availableLocales = new ArrayList<>(Arrays.asList(new Locale[]{russian}));

    public static String getWeatherLanguage() {
        if (getCurrentLocale().equals(russian)) {
            return "ru";
        }

        return "en";
    }

    public enum Units {METRIC, IMPERIAL}

    public static Locale getCurrentLocale() {
        Locale defaultLocale = Locale.getDefault();
        if (availableLocales.contains(defaultLocale)) {
            return defaultLocale;
        }
        return Locale.ENGLISH;
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM", getCurrentLocale());
        return sdf.format(date);
    }

    public static String getFormattedTemp(int temp, Units units) {
        if (units == Units.METRIC) {
            return String.format(getCurrentLocale(), "%d°C", temp);
        } else {
            return String.format(getCurrentLocale(), "%d°F", temp);
        }
    }

    public static String ucFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String getDescription(Context context, String description) {
        String underscored = TextUtils.replace(description, new String[]{" "}, new String[]{"_"}).toString();
        int identifier = context.getResources().getIdentifier(underscored, "string", context.getPackageName());
        try {
            return context.getResources().getString(identifier);
        } catch (Resources.NotFoundException e) {
            return description;
        }
    }
}
