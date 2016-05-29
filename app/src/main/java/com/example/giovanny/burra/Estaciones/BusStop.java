package com.example.giovanny.burra.Estaciones;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by giovanny on 28/05/16.
 */
public class BusStop {
    double lati,longi;
    String name;

    public BusStop(double latitud, double longitud, String name) {
        this.lati = latitud;
        this.longi = longitud;
        this.name = name;
    }

    public LatLng getLL(){
        return new LatLng(lati,longi);
    }

    public String getName(){
        return name;
    }
}
