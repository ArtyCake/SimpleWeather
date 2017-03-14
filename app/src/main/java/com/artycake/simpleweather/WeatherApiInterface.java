package com.artycake.simpleweather;

import com.artycake.simpleweather.models.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by artycake on 2/8/17.
 */

public interface WeatherApiInterface {

    @GET("forecast")
    Call<WeatherData> getCityForecast(@Query("id") int id, @Query("units") String units, @Query("lang") String language);

    @GET("forecast")
    Call<WeatherData> getCoordForecast(@Query("lon") float longitude, @Query("lat") float latitude);
}
