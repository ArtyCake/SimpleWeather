
package com.artycake.simpleweather.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Sys extends RealmObject implements Serializable {

    private int population;

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sys)) return false;

        Sys sys = (Sys) o;

        return getPopulation() == sys.getPopulation();
    }

    @Override
    public int hashCode() {
        return getPopulation();
    }
}
