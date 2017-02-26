package com.artycake.simpleweather.utils;

import android.app.Dialog;
import android.content.Context;
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
        Log.d("APPRATER", launchCount + ", " + (firstLaunchTime - System.currentTimeMillis()));
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
        final Dialog dialog = new Dialog(context);
        String appName = context.getResources().getString(R.string.app_name);
        dialog.setTitle(context.getResources().getString(R.string.rate_dialog_title, appName));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);
        TextView textView = new TextView(context);
        textView.setText(context.getResources().getString(R.string.rate_content, appName));
        textView.setWidth(240);
        textView.setPadding(10, 0, 10, 10);
        linearLayout.addView(textView);
        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button rateButton = new Button(context);
        rateButton.setText(context.getResources().getString(R.string.rate_btn));
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.dismiss();
            }
        });
        buttonsLayout.addView(rateButton);
        Button skipButton = new Button(context);
        skipButton.setText(context.getResources().getString(R.string.rate_skip_btn));
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonsLayout.addView(skipButton);
        Button hideButton = new Button(context);
        hideButton.setText(context.getResources().getString(R.string.rate_hide_btn));
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesService.getInstance(context).putPreferences(PreferencesService.DONT_SHOW_RATE, true);
                dialog.dismiss();
            }
        });
        buttonsLayout.addView(hideButton);
        linearLayout.addView(buttonsLayout);
        dialog.setContentView(linearLayout);
        dialog.show();
    }
}