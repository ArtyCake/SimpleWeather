package com.artycake.simpleweather.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artycake.simpleweather.R;

/**
 * Created by artycake on 2/23/17.
 */

public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 1;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 10;//Min number of launches

    public static void app_launched(Context context) {
        PreferencesService preferencesService = PreferencesService.getInstance(context);
        if (preferencesService.getBoolPref(PreferencesService.DONT_SHOW_RATE, false)) {
            return;
        }
        int launchCount = preferencesService.getIntPref(PreferencesService.LAUNCH_COUNT, 0);
        long firstLaunchTime = preferencesService.getLongPref(PreferencesService.FIRST_LAUNCH_TIME, -1);
        if (firstLaunchTime == -1) {
            firstLaunchTime = System.currentTimeMillis();
            preferencesService.putPreferences(PreferencesService.FIRST_LAUNCH_TIME, firstLaunchTime);
        }
        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= firstLaunchTime +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context);
                launchCount = 0;
            }
        }
        preferencesService.putPreferences(PreferencesService.LAUNCH_COUNT, launchCount + 1);
    }

    private static void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String appName = context.getResources().getString(R.string.app_name);
        builder.setTitle(context.getResources().getString(R.string.rate_dialog_title, appName));
        builder.setMessage(context.getResources().getString(R.string.rate_content, appName));
        builder.setPositiveButton(R.string.rate_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(R.string.rate_skip_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.rate_hide_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferencesService.getInstance(context).putPreferences(PreferencesService.DONT_SHOW_RATE, true);
                dialog.dismiss();
            }
        });
        builder.show();
    }
}