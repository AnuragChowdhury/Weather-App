package com.example.josethomas.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    Handler handler;
    Typeface weatherFont;
    TextView weatherIconTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons.ttf");

        weatherIconTxtView = (TextView)findViewById(R.id.weather_icon);
        weatherIconTxtView.setTypeface(weatherFont);

        String city = null;
        Intent intent = getIntent();

        if((intent != null) && (intent.getExtras()!= null)){
            String source = intent.getExtras().getString("SOURCE");
            if(null != source && source.equals("From_Activity_DisplaySettingsActivity")){
                city = intent.getStringExtra(DisplaySettingsActivity.EXTRA_MESSAGE);
                //updateWeatherData(city);
            }
        }

        if(city == null || city.isEmpty()){
            city = new CityPreference(this).getCity();
        }
        updateWeatherData(city);


    }
    public WeatherActivity(){
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DisplaySettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeatherData(final String city){

        new Thread() {
            public void run(){
                final JSONObject json = RemoteFetch.getjSON(city);
                if(json == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this,
                                    WeatherActivity.this.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();

                            TextView cityField = (TextView)findViewById(R.id.city_field);
                            TextView tempField = (TextView)findViewById(R.id.current_temp_field);
                            TextView updatedField = (TextView)findViewById(R.id.updated_field);
                            TextView detailsField = (TextView)findViewById(R.id.details_field);
                            //TextView weatherIconTxtView = (TextView)findViewById(R.id.weather_icon);

                            cityField.setText("");
                            updatedField.setText("");
                            tempField.setText("");
                            detailsField.setText("No information found. Try again...");
                            detailsField.setTextColor(Color.RED);
                            weatherIconTxtView.setText(R.string.weather_no_report);
                            weatherIconTxtView.setTextSize(100);
                            //weatherIconTxtView.setTextColor(Color.RED);


                        }
                    });
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }

        }.start();
    }

    private void renderWeather(JSONObject json){
        TextView cityField = (TextView)findViewById(R.id.city_field);
        TextView tempField = (TextView)findViewById(R.id.current_temp_field);
        TextView updatedField = (TextView)findViewById(R.id.updated_field);
        TextView detailsField = (TextView)findViewById(R.id.details_field);

        try {
            JSONObject main = json.getJSONObject("main");
            Double temp = main.getDouble("temp");
            //temp = temp-273.15;

            //textView.setText(temperature);
            tempField.setText(String.format("%.2f", temp)+ " ℃");
            cityField.setText(json.getString("name")+", "+json.getJSONObject("sys").getString("country"));

            //setting updated time from json
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedDateTime = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last Updated at " + updatedDateTime);

            //setting weatherIcon
            Long currentTime = json.getLong("dt")*1000;
            Long sunrise = json.getJSONObject("sys").getLong("sunrise")*1000;
            Long sunset = json.getJSONObject("sys").getLong("sunset")*1000;
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            int weatherId = details.getInt("id");
            setWeatherIcon(weatherId, sunrise, sunset, currentTime);

            //setting Details
            String detailsText = details.getString("description").toUpperCase(Locale.US)+ "\n"
                                 +"Humidity: "+main.getString("humidity")+"%" + "\n"
                                 +"Pressure: "+main.getString("pressure")+" hPa";
            detailsField.setText(detailsText);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setWeatherIcon(int weatherId, Long sunrise, Long sunset, Long currentTime){
        String weatherIcon = "";

        if(weatherId==800){
            if(currentTime >= sunrise && currentTime<sunset){
                weatherIcon = getString(R.string.weather_sunny);
            }else{
                weatherIcon = getString(R.string.weather_clear_night);
            }
        }else if(weatherId >=200 && weatherId < 300){
            weatherIcon = getString(R.string.weather_thunder);
        } else if(weatherId >=300 && weatherId < 400){
            weatherIcon = getString(R.string.weather_drizzle);
        }else if(weatherId >=500 && weatherId < 600){
            weatherIcon = getString(R.string.weather_rain);
        }else if(weatherId >=600 && weatherId < 700){
            weatherIcon = getString(R.string.weather_snow);
        }else if(weatherId >=700 && weatherId < 800){
            weatherIcon = getString(R.string.weather_fog);
        }else if(weatherId >800 && weatherId < 900){
            weatherIcon = getString(R.string.weather_cloudy);
        }

        //TextView weatherIconTxtView = (TextView)findViewById(R.id.weather_icon);
        //weatherIconTxtView.setTypeface(weatherFont);
        weatherIconTxtView.setText(weatherIcon);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if((intent != null) && (intent.getExtras()!= null)){
            String source = intent.getExtras().getString("SOURCE");
            if(null != source && source.equals("From_Activity_DisplaySettingsActivity")){
                String city = intent.getStringExtra(DisplaySettingsActivity.EXTRA_MESSAGE);
                updateWeatherData(city);
            }
        }
       /* else{
            String city = new CityPreference(WeatherActivity.this).getCity();
            updateWeatherData(city);
        }*/

    //}
}