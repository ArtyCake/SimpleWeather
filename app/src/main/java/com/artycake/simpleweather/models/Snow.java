
package com.artycake.simpleweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class Snow  extends RealmObject  implements Serializable {

    @SerializedName("3h")
    private double last3h;

    public double getLast3h() {
        return last3h;
    }

    public void setLast3h(double _3h) {
        this.last3h = _3h;
    }

}
