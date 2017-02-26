
package com.artycake.simpleweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class City extends RealmObject implements Serializable {

    @SerializedName("_id")
    private int id;
    private String name;
    private Coord coord;
    private String country;
    private int population;
    private Sys sys;
    @Ignore
    private String displayName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getDisplayName() {
        return String.format(Locale.getDefault(), "%s, %s", getName(), getCountry());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (getId() != city.getId()) return false;
        if (getPopulation() != city.getPopulation()) return false;
        if (!getName().equals(city.getName())) return false;
        if (getCoord() != null && !getCoord().equals(city.getCoord())) return false;
        if (!getCountry().equals(city.getCountry())) return false;
        if (getSys() != null && !getSys().equals(city.getSys())) return false;
        return getDisplayName().equals(city.getDisplayName());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        if (getCoord() != null) {
            result = 31 * result + getCoord().hashCode();
        }
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getPopulation();
        if (getSys() != null) {
            result = 31 * result + getSys().hashCode();
        }
        result = 31 * result + getDisplayName().hashCode();
        return result;
    }
}
