package com.artycake.simpleweather.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.adapters.CityAdapter;
import com.artycake.simpleweather.models.City;
import com.artycake.simpleweather.utils.PreferencesService;
import com.artycake.simpleweather.utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class LocationActivity extends AppCompatActivity {

    private EditText locationName;
    private ImageButton requestLocation;
    private RecyclerView suggestions;
    private LocationManager locationManager;
    private static final int LOCATION_REQUEST = 998;
    private CityAdapter adapter;
    private View mainContainer;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final PreferencesService preferencesService = PreferencesService.getInstance(this);
        locationName = (EditText) findViewById(R.id.location_name);
        requestLocation = (ImageButton) findViewById(R.id.request_location);
        suggestions = (RecyclerView) findViewById(R.id.suggestions);
        mainContainer = findViewById(R.id.main_container);
        progressBar = findViewById(R.id.progress_bar);
        adapter = new CityAdapter();
        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onClick(City city) {
                Log.d("TEST", city.getId() + "/" + city.getDisplayName());
                preferencesService.putPreferences(PreferencesService.LOCATION_CITY_ID, city.getId());
                preferencesService.putPreferences(PreferencesService.LOCATION_CITY_NAME, city.getDisplayName());
                preferencesService.putPreferences(PreferencesService.LOCATION_CHANGED, true);
                finish();
            }
        });
        suggestions.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        suggestions.setLayoutManager(layoutManager);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askLocation();
            }
        });
        locationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 3) {
                    adapter.replaceAll(new ArrayList<City>());
                    return;
                }
                RealmResults<City> filteredCities = RealmController.with(LocationActivity.this).searchCities(s.toString());
                Log.d("TEST", "for " + s + " found " + filteredCities.size());
                adapter.replaceAll(filteredCities);
                suggestions.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
        }
    }

    private void askLocation() {
        if (canAskLocation()) {
            retrieveLocation();
        }
    }

    private void retrieveLocation() {
        Log.d("TEST", String.valueOf(getLastKnownLocation()));
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            try {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return bestLocation;
    }

    private boolean canAskLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "I need access to your location", Toast.LENGTH_LONG).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            }
            return false;
        }
        return true;
    }
}
