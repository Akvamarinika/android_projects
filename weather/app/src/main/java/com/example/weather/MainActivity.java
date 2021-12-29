package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCity;
    Button buttonFind;
    EditText editTextInputKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCity = findViewById(R.id.spinnerCities);
        buttonFind = findViewById(R.id.buttonFindCities);
        editTextInputKm = findViewById(R.id.editTextInputKm);

        List<City> cityListAll = loadJSONFromAsset(); // all

        List<City> partCities = selectPartCitiesList(cityListAll, "RU"); //selected

        Log.i("log", String.valueOf(partCities.size()));

        ArrayAdapter<City> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partCities);
        spinnerCity.setAdapter(adapter);
        //adapter.notifyDataSetChanged();


        buttonFind.setOnClickListener(view -> {
            String inputKm = editTextInputKm.getText().toString().trim();
            int position = spinnerCity.getSelectedItemPosition();

            if (!inputKm.isEmpty()){

                try {
                    double km = Double.parseDouble(inputKm);

                    List<String> nearestCities = findNearestCities(partCities, partCities.get(position), km);
                    Intent intent = new Intent(getApplicationContext(), KmActivity.class);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) nearestCities);

                    startActivity(intent);

                } catch (NumberFormatException ex){
                    Toast.makeText(getApplicationContext(), "Не удалось преобразовать KM к Double!", Toast.LENGTH_SHORT).show();
                    Log.w("log", ex.getMessage());
                }

            } else {
                Toast.makeText(getApplicationContext(), "Не введены КМ!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private List<City> selectPartCitiesList(List<City> cityList, String country){
        List<City> partCities = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            partCities = cityList.stream()
                    .filter(city -> city.getCountry().equals(country))
                    .collect(Collectors.toList());
        }

        return partCities;
    }

    public List<City> loadJSONFromAsset() {
        List<City> cityList = new ArrayList<>();

        try(InputStream inputStream = getAssets().open("city.json");
            InputStreamReader reader = new InputStreamReader(inputStream);) {

            ObjectMapper objectMapper = new ObjectMapper();
            cityList = objectMapper.readValue(reader, new TypeReference<List<City>>(){});
            Log.i("log", "CityList: " + cityList.toString());

        } catch (IOException ex){
            Log.w("log", ex.getMessage());
        }

        return cityList;
    }

    public List<String> findNearestCities(List<City> cities, City citySelect, double km){
        List<String> nearestCities = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            nearestCities = cities.stream()
                    .distinct()
                    .filter(city -> {
                        double dist = calcDistanceHaversine(city.getCoord().getLat(), city.getCoord().getLon(),
                            citySelect.getCoord().getLat(), citySelect.getCoord().getLon());
                            Log.i("log", "city: " + city.getName() + " km: " + dist);
                        return dist < km && dist != 0.0;
                    })
                    .map(City::getName)
                    .collect(Collectors.toList());
        }

        return nearestCities;
    }

    private double calcDistanceHaversine(double lat1, double lon1, double lat2, double lon2){
        double p = Math.PI / 180;
        double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2 + Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(a));
    }

}




//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            List<String> citiesName = partCities.stream()
//                    .map(City::getName)
//                    .collect(Collectors.toList());
//
//            Log.i("log", citiesName.toString());
//
//            ArrayAdapter<City> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partCities);
//            spinnerCity.setAdapter(adapter);
//            //adapter.notifyDataSetChanged();
//
//        }