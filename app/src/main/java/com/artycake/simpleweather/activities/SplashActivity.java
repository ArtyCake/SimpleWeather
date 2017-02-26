package com.artycake.simpleweather.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.models.City;
import com.artycake.simpleweather.utils.DateDeserializer;
import com.artycake.simpleweather.utils.PreferencesService;
import com.artycake.simpleweather.utils.RealmController;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

public class SplashActivity extends AppCompatActivity {

    private PreferencesService preferencesService;
    private final static String TAG = "SplashScreenTAG";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferencesService = PreferencesService.getInstance(this);
        progressBar = (ProgressBar) findViewById(R.id.splash_progress);
        if (preferencesService.getBoolPref(PreferencesService.FIRST_LAUNCH, true)) {
            fillCities();
            return;
        }
        goToApp();
    }

    private void goToApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void fillCities() {
        Log.d(TAG, "startCopying");
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.cities);
            File file = new File(this.getFilesDir(), Realm.DEFAULT_REALM_NAME);
            progressBar.setMax(100);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            long alreadyCopied = 0;
            int size = inputStream.available();
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
                alreadyCopied += bytesRead;
                int progress = Double.valueOf(100 * alreadyCopied / size).intValue();
                progressBar.setProgress(progress);
            }
            outputStream.close();
            Log.d(TAG, "stopCopying");
        } catch (IOException e) {
            e.printStackTrace();
        }
        preferencesService.putPreferences(PreferencesService.FIRST_LAUNCH, false);
        goToApp();
    }
}
