
package com.artycake.simpleweather.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Sys_  extends RealmObject  implements Serializable {

    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

}
