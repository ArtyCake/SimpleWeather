package com.artycake.simpleweather.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.models.City;

import java.util.Locale;

/**
 * Created by artycake on 2/14/17.
 */

public class CityHolder extends RecyclerView.ViewHolder {
    private TextView cityName;

    public CityHolder(View itemView) {
        super(itemView);
        cityName = (TextView) itemView.findViewById(R.id.city_name);
    }

    public void updateUI(City city) {
        cityName.setText(String.format(Locale.getDefault(), "%s, %s", city.getName(), city.getCountry()));
    }
}
