package com.artycake.simpleweather.widgets;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.activities.MainActivity;
import com.artycake.simpleweather.activities.SplashActivity;
import com.artycake.simpleweather.models.DisplayWeatherDay;
import com.artycake.simpleweather.utils.IconConverter;
import com.artycake.simpleweather.utils.Localization;
import com.artycake.simpleweather.utils.PreferencesService;
import com.artycake.simpleweather.utils.RealmController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class BigWeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.big_weather_widget);
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        PreferencesService preferencesService = PreferencesService.getInstance(context);
        if (preferencesService.getIntPref(PreferencesService.LOCATION_CITY_ID, -1) == -1) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }
        views.setViewVisibility(R.id.widget_main_content, View.VISIBLE);
        views.setViewVisibility(R.id.widget_no_city, View.GONE);
        Calendar calendar = new GregorianCalendar();
        RealmController realmController = RealmController.with((Application) context.getApplicationContext());
        DisplayWeatherDay day = realmController.getDay(calendar.getTime());
        Localization.Units units;
        if (preferencesService.getIntPref(PreferencesService.UNITS, PreferencesService.CELSIUS) == PreferencesService.CELSIUS) {
            units = Localization.Units.METRIC;
        } else {
            units = Localization.Units.IMPERIAL;
        }
        if (day != null) {
            views.setTextViewText(R.id.widget_day_name, day.getDayName());
            views.setTextViewText(R.id.widget_city, preferencesService.getStringPref(PreferencesService.LOCATION_CITY_NAME, null));
            views.setTextViewText(R.id.widget_current_description, Localization.ucFirst(Localization.getDescription(context, day.getShortDescription())));
            views.setTextViewText(R.id.widget_current_temp, Localization.getFormattedTemp(day.getCurrentTemp(), units));
            views.setImageViewResource(R.id.widget_weather_icon, day.getIconId());
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = realmController.getDay(calendar.getTime());
        if (day == null) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }

        views.setTextViewText(R.id.day_name_second, day.getDayName());
        views.setTextViewText(R.id.min_temp_second, Localization.getFormattedTemp(day.getMinTemp(), units));
        views.setTextViewText(R.id.max_temp_second, Localization.getFormattedTemp(day.getMaxTemp(), units));
        views.setImageViewResource(R.id.day_icon_second, day.getIconId());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = realmController.getDay(calendar.getTime());
        if (day == null) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }
        views.setTextViewText(R.id.day_name_third, day.getDayName());
        views.setTextViewText(R.id.min_temp_third, Localization.getFormattedTemp(day.getMinTemp(), units));
        views.setTextViewText(R.id.max_temp_third, Localization.getFormattedTemp(day.getMaxTemp(), units));
        views.setImageViewResource(R.id.day_icon_third, day.getIconId());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = realmController.getDay(calendar.getTime());
        if (day == null) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }
        views.setTextViewText(R.id.day_name_fourth, day.getDayName());
        views.setTextViewText(R.id.min_temp_fourth, Localization.getFormattedTemp(day.getMinTemp(), units));
        views.setTextViewText(R.id.max_temp_fourth, Localization.getFormattedTemp(day.getMaxTemp(), units));
        views.setImageViewResource(R.id.day_icon_fourth, day.getIconId());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

