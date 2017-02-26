
package com.artycake.simpleweather.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Coord extends RealmObject implements Serializable {

    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coord)) return false;

        Coord coord = (Coord) o;

        if (Double.compare(coord.getLon(), getLon()) != 0) return false;
        return Double.compare(coord.getLat(), getLat()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLon());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
