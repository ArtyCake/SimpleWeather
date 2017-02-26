package com.artycake.simpleweather.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.utils.PreferencesService;

public class SettingsActivity extends AppCompatActivity {

    private TextView location;
    private PreferencesService preferencesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferencesService = PreferencesService.getInstance(this);
        location = (TextView) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, LocationActivity.class));
            }
        });
        RadioGroup unitsGroup = (RadioGroup) findViewById(R.id.units_group);
        int units = preferencesService.getIntPref(PreferencesService.UNITS, PreferencesService.CELSIUS);
        if (units == PreferencesService.CELSIUS) {
            ((RadioButton) findViewById(R.id.units_celsius)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.units_fahrenheit)).setChecked(true);
        }
        unitsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.units_celsius) {
                    preferencesService.putPreferences(PreferencesService.UNITS, PreferencesService.CELSIUS);
                } else {
                    preferencesService.putPreferences(PreferencesService.UNITS, PreferencesService.FAHRENHEIT);
                }
                preferencesService.putPreferences(PreferencesService.UNITS_CHANGED, true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cityId = preferencesService.getIntPref(PreferencesService.LOCATION_CITY_ID, -1);
        ActionBar actionBar = getSupportActionBar();
        if (cityId == -1) {
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }
        } else {
            location.setText(preferencesService.getStringPref(PreferencesService.LOCATION_CITY_NAME, ""));
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(upIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
