package com.artycake.simpleweather.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.artycake.simpleweather.services.WeatherService;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, WeatherService.class));
    }
}
