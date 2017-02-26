
package com.artycake.simpleweather.models;

import java.io.Serializable;

public class WeatherData  implements Serializable {

    private City city;
    private String cod;
    private double message;
    private int cnt;
    private java.util.List<WeatherDay> list = null;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WeatherDay> getList() {
        return list;
    }

    public void setList(java.util.List<WeatherDay> list) {
        this.list = list;
    }

}
