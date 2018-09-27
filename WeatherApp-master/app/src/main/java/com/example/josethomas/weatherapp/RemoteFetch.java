package com.example.josethomas.weatherapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by JoseThomas on 2/16/2016.
   */
public class RemoteFetch {

   // private static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=44db6a862fba0b067b1930da0d769e98";
    private static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static JSONObject getjSON (String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API,city));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", "eb9bd4de5f20beb2a869148e010bc968");
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String temp="";
            while((temp = reader.readLine())!= null){
                json.append(temp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            if(data.getInt("cod")!=200){
                return null;
            }
            return data;
            //System.out.println(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }




    public static void main(String args[]){
        System.out.println("Hello Jose, how are you?");
        try {
            String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=44db6a862fba0b067b1930da0d769e98";
            String GOOGLE_URL = "http://www.bing.com/search?q=TIME+RENO";
            String DICTIONARY_URL ="http://www.bing.com/search?q=meaning+escape";
            String city = "Reno";
            //URL url = new URL(String.format(OPEN_WEATHER_API,city));

            Document doc = Jsoup.connect(DICTIONARY_URL).userAgent("Chrome").get();
            Elements text = doc.select("div[class=dcont]");

            System.out.println("WHAT I GET ? :");
            Elements content = doc.getElementsByClass("dc_pd");
            for(Element links: content){
                Elements dc_st = links.select("div[class=dc_st]");
                System.out.println(dc_st.text());

                Elements dc_bld = links.select("span[class=dc_bld]").select("div[class=dc_nml]").select("div[class=dc_pm]");

                for(Element dc_nml: dc_bld){
                    Elements dc_mns = dc_nml.select("div[class=dc_mn]");
                    for(Element dc_mn: dc_mns){
                        System.out.println(dc_mn.text());
                    }
                }

            }










            //System.out.print("WHAT I HAVE ? :"+text.text());

          //  String GOOGLE_URL2 = "http://www.bing.com/search?q=TIME+VIENNA";
            //Document doc2 = Jsoup.connect(GOOGLE_URL2).userAgent("Chrome").get();
           // Elements text1 = doc2.select("div[class=b_focusTextLarge]");
            //Elements text1 = doc2.select("div[class=b_focusTextLarge]");
           // System.out.print("WHAT I HAVE ? :"+text1.text());


           /* HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String temp="";
            while((temp = reader.readLine())!= null){
                json.append(temp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());*/
            //System.out.println(json);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
