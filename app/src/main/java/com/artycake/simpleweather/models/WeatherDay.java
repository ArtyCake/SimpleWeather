
package com.artycake.simpleweather.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class WeatherDay extends RealmObject implements Serializable {

    private int dt;
    private Main main;
    @Ignore
    private java.util.List<Weather> weather = null;
    @Expose(serialize = false, deserialize = false)
    private RealmList<Weather> realmWeather = null;
    private Clouds clouds;
    private Wind wind;
    private Snow snow;
    private Sys_ sys;
    @SerializedName("dt_txt")
    private Date dtTxt;
    private Rain rain;

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public java.util.List<Weather> getWeather() {
        if (weather == null && realmWeather != null) {
            this.weather = realmWeather;
        }
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public RealmList<Weather> getRealmWeather() {
        if (this.realmWeather == null && this.weather != null) {
            this.realmWeather = new RealmList<Weather>(weather.toArray(new Weather[weather.size()]));
        }
        return realmWeather;
    }

    public void setRealmWeather(RealmList<Weather> realmWeather) {
        this.realmWeather = realmWeather;
    }

    public void setRealmWeather(List<Weather> realmWeather) {
        this.realmWeather = new RealmList<Weather>(weather.toArray(new Weather[weather.size()]));
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public Sys_ getSys() {
        return sys;
    }

    public void setSys(Sys_ sys) {
        this.sys = sys;
    }

    public Date getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(Date dtTxt) {
        this.dtTxt = dtTxt;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }
}
