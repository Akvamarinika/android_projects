package com.example.geolocationbasis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button showMapButton;
    TextView latText, lonText, timeText; /*будет result*/

    LocationManager locationManager;
    Location location;

    private boolean granted = false; //
    private final int LOCATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapButton   =   findViewById(R.id.toMapButton);
        lonText         =   findViewById(R.id.lon);
        latText         =   findViewById(R.id.lat);
        timeText        =   findViewById(R.id.timeText);

        //3) подключение менеджера местоположения (сервис)
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //7) передача координат в GoogleMaps активность
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                if (location != null){
                    intent.putExtra("latitude", location.getLatitude());
                    intent.putExtra("longitude", location.getLongitude());
                    startActivity(intent);
                }
            }
        });
    }

    //4) LocationListener
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null){
                return;
            } else {
                showLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //5)получить местоположение
    private void showLocation(Location location){
        String coord = String.valueOf(location.getLatitude()); //широта
        latText.setText(coord);
        coord = String.valueOf(location.getLongitude()); //долгота
        lonText.setText(coord);
        coord = new Date(location.getTime()).toString();
        timeText.setText(coord);
    }

    //6)получение координат с запросом разрешения
    @Override
    protected void onResume() {
        super.onResume();

        if (granted || checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 30, listener);

            if(locationManager != null){ // если никуда не передвигаемся
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null){
                    showLocation(location);
                }
            }
        }

    }

    //2)функция обратного вызова для обработки ответа пользователя
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String[] permissions, //разрешения
                                          @NonNull int[] grantResult) { // решения пользователя
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        if (grantResult.length > 0){
            for (int j : grantResult) {
                if (j != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                }
            }
        } else {
            granted = false;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }

    //1) функция проверки, есть ли у приложения разрешение на определение местоположения
    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
    }
}
