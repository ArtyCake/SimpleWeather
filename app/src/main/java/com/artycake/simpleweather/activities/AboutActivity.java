package com.artycake.simpleweather.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.artycake.simpleweather.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView iconLink = (TextView) findViewById(R.id.about_icon_link);
        TextView apiLink = (TextView) findViewById(R.id.about_api_link);
        iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.freepik.com/free-vector/weather-icons-set_709126.htm"));
                startActivity(browserIntent);
            }
        });
        apiLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://openweathermap.org/"));
                startActivity(browserIntent);
            }
        });
    }
}
