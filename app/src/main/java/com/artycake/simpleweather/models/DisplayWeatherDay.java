package com.artycake.simpleweather.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.artycake.simpleweather.utils.IconConverter;
import com.artycake.simpleweather.utils.Localization;
import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by artycake on 2/12/17.
 */

public class DisplayWeatherDay {
    private String dayName;
    private Drawable icon;
    private int iconId;
    private int minTemp;
    private int maxTemp;
    private int currentTemp;
    private String shortDescription;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Context context, int icon) {
        this.icon = IconConverter.getInstance(context).getIconDrawable(icon);
        this.iconId = IconConverter.getInstance(context).getIconDrawableId(icon);
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = Double.valueOf(minTemp).intValue();
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = Double.valueOf(maxTemp).intValue();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDate(Date date) {
        this.dayName = Localization.getFormattedDate(date);
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = Double.valueOf(currentTemp).intValue();
    }
}
