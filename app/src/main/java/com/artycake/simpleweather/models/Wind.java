
package com.artycake.simpleweather.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Wind  extends RealmObject  implements Serializable {

    private double speed;
    private double deg;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

}
