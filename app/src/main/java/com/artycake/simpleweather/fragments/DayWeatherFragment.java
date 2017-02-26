package com.artycake.simpleweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.models.DisplayWeatherDay;
import com.artycake.simpleweather.utils.Localization;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayWeatherFragment extends Fragment {

    private TextView dayName;
    private ImageView icon;
    private TextView temperatureMin;
    private TextView temperatureMax;
    private OnViewCreated onViewCreated;

    public DayWeatherFragment() {

    }

    public void setOnViewCreated(OnViewCreated onViewCreated) {
        this.onViewCreated = onViewCreated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_weather, container, false);
        dayName = (TextView) view.findViewById(R.id.day_name);
        icon = (ImageView) view.findViewById(R.id.weather_icon);
        temperatureMin = (TextView) view.findViewById(R.id.temperature_min);
        temperatureMax = (TextView) view.findViewById(R.id.temperature_max);
        temperatureMin.setText("--°C");
        temperatureMax.setText("--°C");
        if (onViewCreated != null) {
            onViewCreated.onViewCreated();
        }
        return view;
    }

    public void updateUI(DisplayWeatherDay day, Localization.Units units) {
        if (dayName == null) {
            Log.d("TEST", "views on fragment not defined yet");
            return;
        }
        dayName.setText(day.getDayName());
        icon.setImageDrawable(day.getIcon());
        temperatureMin.setText(Localization.getFormattedTemp(day.getMinTemp(), units));
        temperatureMax.setText(Localization.getFormattedTemp(day.getMaxTemp(), units));
    }

    public interface OnViewCreated {
        void onViewCreated();
    }
}
