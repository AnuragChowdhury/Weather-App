package com.example.josethomas.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.preference.PreferenceManager;

/**
 * Created by JoseThomas on 2/17/2016.
 */
public class CityPreference {
    SharedPreferences prefs;

    /*public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }*/

    public CityPreference(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getCity(){

        String city = prefs.getString("city", "Gujarat,IN");
        if(city.isEmpty()){
             city = "Gujarat,IN";
        }

        return city;
        //return prefs.getString("city", "Gujarat,IN");
    }
    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
