package com.artycake.simpleweather.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.artycake.simpleweather.models.City;
import com.artycake.simpleweather.models.DisplayWeatherDay;
import com.artycake.simpleweather.models.Weather;
import com.artycake.simpleweather.models.WeatherDay;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by artycake on 2/8/17.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;
    private final Context context;

    public RealmController(Application application) {
        Realm.init(application.getApplicationContext());
        RealmConfiguration realmConfiguration = getConfiguration();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        context = application.getApplicationContext();
    }

    private static RealmConfiguration getConfiguration() {
        return new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Service service) {

        if (instance == null) {
            instance = new RealmController(service.getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //clear all objects from Book.class
    public void clearAllWeather() {

        final RealmResults<WeatherDay> weatherDays = realm.where(WeatherDay.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                weatherDays.deleteAllFromRealm();
            }
        });
    }

    public void clearAllCities() {

        final RealmResults<City> cities = realm.where(City.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                cities.deleteAllFromRealm();
            }
        });
    }

    public RealmResults<WeatherDay> getDays() {

        return realm.where(WeatherDay.class).findAll();
    }

    //query a single item with the given id
    public DisplayWeatherDay getDay(Date date) {
        boolean today = false;
        Calendar calendar = new GregorianCalendar();
        if (date.equals(calendar.getTime())) {
            today = true;
        }
        calendar.setTime(date);
        Date from = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (!today) {
            from = calendar.getTime();
        }
        Log.d("TEST", "getDay for " + from);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date till = calendar.getTime();
        DisplayWeatherDay displayWeatherDay = new DisplayWeatherDay();
        if (today) {
            WeatherDay day = realm.where(WeatherDay.class)
                    .greaterThanOrEqualTo("dtTxt", from)
                    .lessThanOrEqualTo("dtTxt", till)
                    .findFirst();
            if (day == null) {
                day = realm.where(WeatherDay.class).findFirst();
                Log.d("TEST", "Received after null");
                if (day == null) {
                    Log.d("TEST", "No days stored at all");
                    return null;
                }
                displayWeatherDay.setMinTemp(day.getMain().getTempMin());
                displayWeatherDay.setMaxTemp(day.getMain().getTempMax());
            } else {
                RealmResults<WeatherDay> days = realm.where(WeatherDay.class)
                        .greaterThan("dtTxt", from)
                        .lessThan("dtTxt", till)
                        .findAll();
                if (days.size() == 0) {
                    Log.d("TEST", "No days stored at all");
                    return null;
                }
                double maxTemp = 0;
                double minTemp = Double.POSITIVE_INFINITY;
                for (WeatherDay _day : days) {
                    if (_day.getMain().getTempMax() > maxTemp) {
                        maxTemp = _day.getMain().getTempMax();
                    }
                    if (_day.getMain().getTempMin() < minTemp) {
                        minTemp = _day.getMain().getTempMin();
                    }
                }
                displayWeatherDay.setMinTemp(minTemp);
                displayWeatherDay.setMaxTemp(maxTemp);
            }

            Log.d("TEST", String.valueOf(day));
            displayWeatherDay.setDate(from);
            displayWeatherDay.setCurrentTemp(day.getMain().getTemp());
            displayWeatherDay.setShortDescription(day.getRealmWeather().get(0).getDescription());
            displayWeatherDay.setIcon(context, day.getRealmWeather().get(0).getId());
            Log.d("TEST", String.valueOf(day.getMain().getTemp()));
        } else {
            RealmResults<WeatherDay> days = realm.where(WeatherDay.class)
                    .greaterThan("dtTxt", from)
                    .lessThan("dtTxt", till)
                    .findAll();
            if (days.size() == 0) {
                Log.d("TEST", "no data found for " + date);
                return null;
            }
            double maxTemp = 0;
            double minTemp = Double.POSITIVE_INFINITY;
            for (WeatherDay day : days) {
                if (day.getMain().getTempMax() > maxTemp) {
                    maxTemp = day.getMain().getTempMax();
                }
                if (day.getMain().getTempMin() < minTemp) {
                    minTemp = day.getMain().getTempMin();
                }
            }
            displayWeatherDay.setMaxTemp(maxTemp);
            displayWeatherDay.setMinTemp(minTemp);
            displayWeatherDay.setDate(days.get(0).getDtTxt());
            displayWeatherDay.setShortDescription(days.get(0).getRealmWeather().get(0).getDescription());
            displayWeatherDay.setIcon(context, days.get(0).getRealmWeather().get(0).getId());
        }
        return displayWeatherDay;
    }

    public RealmResults<City> getCities() {

        return realm.where(City.class).findAll();
    }

    public RealmResults<City> searchCities(String query) {

        return realm.where(City.class).contains("name", query, Case.INSENSITIVE).findAll();
    }
}
