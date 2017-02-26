package com.artycake.simpleweather.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.WeatherApiInterface;
import com.artycake.simpleweather.models.City;
import com.artycake.simpleweather.models.WeatherData;
import com.artycake.simpleweather.models.WeatherDay;
import com.artycake.simpleweather.utils.DateDeserializer;
import com.artycake.simpleweather.utils.PreferencesService;
import com.artycake.simpleweather.utils.RealmController;
import com.artycake.simpleweather.widgets.BigWeatherWidget;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService extends Service {

    private static final String TAG = WeatherService.class.getSimpleName();
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private WeatherBinder binder = new WeatherBinder();
    private Retrofit retrofit;
    private PreferencesService preferencesService;
    private Realm realm;
    private Runnable updateTask;
    private Handler updateHandler;

    public WeatherService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setRetrofit();
        preferencesService = PreferencesService.getInstance(this);
        realm = RealmController.with(this).getRealm();

        updateTask = new Runnable() {
            @Override
            public void run() {
                updateWeather(null);
                updateHandler.postDelayed(updateTask, 60 * 60 * 1000);
            }
        };
        updateHandler = new Handler();
        updateTask.run();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void update(@Nullable OnUpdate onUpdate) {
        long lastUpdateTimestamp = preferencesService.getLongPref(PreferencesService.LAST_UPDATE_TIMESTAMP, -1);
        long now = (new Date()).getTime();
        if (preferencesService.getBoolPref(PreferencesService.LOCATION_CHANGED, false)) {
            preferencesService.putPreferences(PreferencesService.LOCATION_CHANGED, false);
            updateWeather(onUpdate);
            return;
        }
        if (preferencesService.getBoolPref(PreferencesService.UNITS_CHANGED, false)) {
            preferencesService.putPreferences(PreferencesService.UNITS_CHANGED, false);
            updateWeather(onUpdate);
            return;
        }
        if (lastUpdateTimestamp == -1 || lastUpdateTimestamp < now - PreferencesService.UPDATE_PERIOD) {
            updateWeather(onUpdate);
            return;
        }
        if (onUpdate != null) {
            onUpdate.onUpdate();
            updateWidgets();
        }
    }

    private void updateWidgets() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        ComponentName widgetComponent = new ComponentName(this, BigWeatherWidget.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        Intent intent = new Intent(this, BigWeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        sendBroadcast(intent);
    }

    public interface OnUpdate {
        void onUpdate();

        void onFail();
    }

    public class WeatherBinder extends Binder {
        public WeatherService getService() {
            return WeatherService.this;
        }
    }

    private void updateWeather(final OnUpdate onUpdate) {
        RealmController.with(this).clearAllWeather();
        WeatherApiInterface apiInterface = retrofit.create(WeatherApiInterface.class);
        int units = preferencesService.getIntPref(PreferencesService.UNITS, -1);
        String unitsName = "metric";
        if (units == PreferencesService.FAHRENHEIT) {
            unitsName = "imperial";
        }
        Call<WeatherData> call = apiInterface.getCityForecast(preferencesService.getIntPref(PreferencesService.LOCATION_CITY_ID, -1), unitsName);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, String.valueOf(response.errorBody()));
                    if (onUpdate != null) {
                        onUpdate.onFail();
                    }
                    return;
                }
                WeatherData weatherData = response.body();
                java.util.List<WeatherDay> days = weatherData.getList();
                for (WeatherDay day : days) {
                    Log.d("TEST_r", String.valueOf(day.getWeather()));
                    day.setRealmWeather(day.getWeather());
                    realm.beginTransaction();
                    realm.copyToRealm(day);
                    realm.commitTransaction();
                    Log.d("TEST", "day added " + day.getDtTxt());
                }
                preferencesService.putPreferences(PreferencesService.LAST_UPDATE_TIMESTAMP, (new Date()).getTime());
                updateWidgets();
                if (onUpdate != null) {
                    onUpdate.onUpdate();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.d(TAG, "Failure: " + t.getMessage());
                if (onUpdate != null) {
                    onUpdate.onFail();
                }
            }
        });
    }

    private void setRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("APPID", getResources().getString(R.string.apiKey))
                                .build();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class, new DateDeserializer())
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void getCityByCoords(float longtitude, float latitude, final OnUpdate onUpdate) {
        WeatherApiInterface apiInterface = retrofit.create(WeatherApiInterface.class);
        Call<WeatherData> call = apiInterface.getCoordForecast(longtitude, latitude);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, String.valueOf(response.errorBody()));
                    if (onUpdate != null) {
                        onUpdate.onFail();
                    }
                    return;
                }
                WeatherData weatherData = response.body();
                City city = weatherData.getCity();
                if (city == null) {
                    if (onUpdate != null) {
                        onUpdate.onFail();
                    }
                    return;
                }
                preferencesService.putPreferences(PreferencesService.LOCATION_CITY_ID, city.getId());
                preferencesService.putPreferences(PreferencesService.LOCATION_CITY_NAME, city.getDisplayName());
                if (onUpdate != null) {
                    onUpdate.onUpdate();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                if (onUpdate != null) {
                    onUpdate.onFail();
                }
            }
        });
    }
}
