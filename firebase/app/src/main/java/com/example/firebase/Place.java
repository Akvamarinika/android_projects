package com.example.firebase;

public class Place {
    public String name;
    public double lat, lon;

    public Place() {
        // для Firebase требуется конструктор по умолчанию
    }
    public Place(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
    public String toString () {
        return "name: " + name + ", latitude: " + lat + ", longtitude: " + lon;
    }
}
