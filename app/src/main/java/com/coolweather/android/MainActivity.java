package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<String> list = new ArrayList<String>();

    private static HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = new File("/data/data/com.coolweather.android/shared_prefs/AllAreas.xml");
        if(!f.exists()) {
            InputStream inputStream = getResources().openRawResource(R.raw.citylist);
            String result = saveData(inputStream);
            SharedPreferences.Editor editor1 = getSharedPreferences("AllInfo", MODE_PRIVATE).edit();
            SharedPreferences.Editor editor2 = getSharedPreferences("AllAreas", MODE_PRIVATE).edit();
            int count = 1;
            for(String item : list) {
                editor1.putString(item, map.get(item));
                editor2.putString(count+"", item);
                count ++;
            }
            editor1.apply();
            editor2.apply();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static String saveData(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while((line = reader.readLine()) != null) {
                String[] items = line.split("\\s+");
                String cityID = items[0];
                String area = items[2] + " "+items[9] + " " + items[7];
                map.put(area, cityID);
                list.add(area);

            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return  sb.toString();
    }

}
