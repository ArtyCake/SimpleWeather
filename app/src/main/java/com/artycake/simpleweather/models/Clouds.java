
package com.artycake.simpleweather.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Clouds extends RealmObject implements Serializable {

    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

}
