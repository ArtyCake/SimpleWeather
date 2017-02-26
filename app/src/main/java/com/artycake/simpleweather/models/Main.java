
package com.artycake.simpleweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class Main extends RealmObject implements Serializable {

    private double temp;
    @SerializedName("temp_min")
    private double tempMin;
    @SerializedName("temp_max")
    private double tempMax;
    private double pressure;
    @SerializedName("sea_level")
    private double seaLevel;
    @SerializedName("grnd_level")
    private double groundLevel;
    private int humidity;
    private int tempKf;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(double groundLevel) {
        this.groundLevel = groundLevel;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTempKf() {
        return tempKf;
    }

    public void setTempKf(int tempKf) {
        this.tempKf = tempKf;
    }

}
