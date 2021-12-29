package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class KmActivity extends AppCompatActivity {

    ListView listViewCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);

        listViewCity = findViewById(R.id.listViewCities);

//        List<City> cityList = loadJSONFromAsset();
//        ArrayAdapter<City> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityList);
//        listViewCity.setAdapter(adapter);

        Intent intent = getIntent();
        List<String> listCities = intent.getStringArrayListExtra("list");
        Log.d("log", listCities.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCities);
        listViewCity.setAdapter(adapter);
    }


}