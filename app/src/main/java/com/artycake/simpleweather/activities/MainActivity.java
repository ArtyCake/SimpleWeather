package com.artycake.simpleweather.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artycake.simpleweather.services.WeatherService;
import com.artycake.simpleweather.R;
import com.artycake.simpleweather.utils.AppRater;
import com.artycake.simpleweather.utils.Localization;
import com.artycake.simpleweather.utils.PreferencesService;
import com.artycake.simpleweather.utils.RealmController;
import com.artycake.simpleweather.fragments.DayWeatherFragment;
import com.artycake.simpleweather.models.DisplayWeatherDay;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Realm realm;
    private DayWeatherFragment secondDayFragment;
    private DayWeatherFragment thirdDayFragment;
    private DayWeatherFragment fourthDayFragment;
    private DayWeatherFragment fifthDayFragment;
    private TextView dayName;
    private TextView cityName;
    private TextView temperature;
    private TextView temperatureMax;
    private TextView temperatureMin;
    private ImageView weathericon;
    private TextView description;
    private PreferencesService preferencesService;
    private ServiceConnection connection;
    private boolean bound = false;
    private WeatherService weatherService;
    private WeatherService.OnUpdate onWeatherUpdate;
    private int fragmentsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesService = PreferencesService.getInstance(this);
        int cityId = preferencesService.getIntPref(PreferencesService.LOCATION_CITY_ID, -1);
        if (cityId == -1) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }
        setContentView(R.layout.activity_main);

        defineViews();
        defineFragments();
        realm = RealmController.with(this).getRealm();
        onWeatherUpdate = new WeatherService.OnUpdate() {
            @Override
            public void onUpdate() {
                updateView();
            }

            @Override
            public void onFail() {
                Toast.makeText(MainActivity.this, "Can't update weather data. Please try again later", Toast.LENGTH_LONG).show();
            }
        };
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
                weatherService = ((WeatherService.WeatherBinder) binder).getService();
                weatherService.update(onWeatherUpdate);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
        bindService(new Intent(this, WeatherService.class), connection, BIND_AUTO_CREATE);
        AppRater.app_launched(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind();
    }

    private void unbind() {
        if (!bound) {
            return;
        }
        unbindService(connection);
        bound = false;
    }

    private void defineViews() {
        dayName = (TextView) findViewById(R.id.week_day);
        cityName = (TextView) findViewById(R.id.city_name);
        temperature = (TextView) findViewById(R.id.current_temp);
        temperatureMin = (TextView) findViewById(R.id.temperature_min);
        temperatureMax = (TextView) findViewById(R.id.temperature_max);
        description = (TextView) findViewById(R.id.description);
        weathericon = (ImageView) findViewById(R.id.weather_icon);
    }

    private void defineFragments() {

        DayWeatherFragment.OnViewCreated onViewCreated = new DayWeatherFragment.OnViewCreated() {
            @Override
            public void onViewCreated() {
                fragmentsLoaded++;
                Log.d("TEST", "loaded fragments " + fragmentsLoaded);
                if (fragmentsLoaded == 3) {
                    updateView();
                }
            }
        };
        secondDayFragment = (DayWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container_second_day);
        if (secondDayFragment == null) {
            secondDayFragment = new DayWeatherFragment();
            secondDayFragment.setOnViewCreated(onViewCreated);
            getSupportFragmentManager().beginTransaction().add(R.id.container_second_day, secondDayFragment).commit();
        }
        thirdDayFragment = (DayWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container_third_day);
        if (thirdDayFragment == null) {
            thirdDayFragment = new DayWeatherFragment();
            thirdDayFragment.setOnViewCreated(onViewCreated);
            getSupportFragmentManager().beginTransaction().add(R.id.container_third_day, thirdDayFragment).commit();
        }
        fourthDayFragment = (DayWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container_fourth_day);
        if (fourthDayFragment == null) {
            fourthDayFragment = new DayWeatherFragment();
            fourthDayFragment.setOnViewCreated(onViewCreated);
            getSupportFragmentManager().beginTransaction().add(R.id.container_fourth_day, fourthDayFragment).commit();
        }
    }

    private void updateView() {
        Calendar calendar = new GregorianCalendar();
        DisplayWeatherDay day = RealmController.with(this).getDay(calendar.getTime());
        if (day == null) {
            Log.d(TAG, "No first day stored");
            return;
        }
        cityName.setText(preferencesService.getStringPref(PreferencesService.LOCATION_CITY_NAME, getString(R.string.no_city_selected)));
        dayName.setText(day.getDayName());
        Localization.Units units;
        if (preferencesService.getIntPref(PreferencesService.UNITS, PreferencesService.CELSIUS) == PreferencesService.CELSIUS) {
            units = Localization.Units.METRIC;
        } else {
            units = Localization.Units.IMPERIAL;
        }
        temperature.setText(Localization.getFormattedTemp(day.getCurrentTemp(), units));
        temperatureMax.setText(Localization.getFormattedTemp(day.getMaxTemp(), units));
        temperatureMin.setText(Localization.getFormattedTemp(day.getMinTemp(), units));
        weathericon.setImageDrawable(day.getIcon());
        description.setText(Localization.ucFirst(day.getShortDescription()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = RealmController.with(this).getDay(calendar.getTime());
        if (day == null) {
            Log.d(TAG, "No second day stored");
            return;
        }
        secondDayFragment.updateUI(day, units);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = RealmController.with(this).getDay(calendar.getTime());
        if (day == null) {
            Log.d(TAG, "No third day stored");
            return;
        }
        thirdDayFragment.updateUI(day, units);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        day = RealmController.with(this).getDay(calendar.getTime());
        if (day == null) {
            Log.d(TAG, "No fourth day stored");
            return;
        }
        fourthDayFragment.updateUI(day, units);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                if (bound) {
                    weatherService.update(onWeatherUpdate);
                }
                return true;
            }
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            case R.id.action_about: {
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
