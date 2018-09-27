package com.example.josethomas.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import com.example.josethomas.weatherapp.WeatherActivity;

public class DisplaySettingsActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.josethomas.weatherapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void changeCity(View view){
        EditText editText = (EditText)findViewById(R.id.enter_city);
        String city = editText.getText().toString();

        Intent intent = new Intent(this, WeatherActivity.class);
        if(!city.isEmpty()){
            city = city.replaceAll("\\s","");
            new CityPreference(this).setCity(city);
            intent.putExtra("SOURCE", "From_Activity_DisplaySettingsActivity");
            intent.putExtra(EXTRA_MESSAGE, city);
        }else{
            //TODO: if city is given empty
        }
        startActivity(intent);
    }

}
