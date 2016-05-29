package com.example.giovanny.burra.Estaciones;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.example.giovanny.burra.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by giovanny on 28/05/16.
 */
public class MarkersYMas {
    /*
    -12.166885, -76.949351 paradero electra
    -12.159503, -76.956007 paradero san gabriel
    -12.158397, -76.960507 paradero hospital
     */
    BusStop[] pS = new BusStop[]{new BusStop(-12.153654, -76.9725183,"Paradero Ciudad"),
            new BusStop(-12.150713, -76.979516,"Paradero Atocongo"),
            new BusStop(-12.170875, -76.945882,"Paradero Pesquero"),
            new BusStop(-12.166885, -76.949351,"Paradero Electra"),
            new BusStop(-12.159503, -76.956007,"Paradero San Gabriel"),
            new BusStop(-12.158397, -76.960507,"Paradero Hospital")
    };

    BusStop[] pN = new BusStop[]{new BusStop(-11.9273604,-77.0556201,"Paradero Trapiche"),
            new BusStop(-11.9405726,-77.0743999,"Paradero Pro")
    };
    BusStop[] pE = new BusStop[]{new BusStop(-12.0182501,-76.9422157,"Paradero Huachipa"),
            new BusStop(-12.0160936,-76.8889148,"Paradero Santa Clara")
    };

    public MarkersYMas(){

    }

    public BusStop getPS(String dir ,int i){
        if (dir.equals("Sur"))
            return pS[i];
        else if(dir.equals("Norte"))
            return pN[i];
        else
            return pE[i];
    }

    public int getPLength(String dir){
        if (dir.equals("Sur"))
            return pS.length;
        else if(dir.equals("Norte"))
            return pN.length;
        else
            return pE.length;
    }

    public Marker getMarker(GoogleMap mMap,Resources res,String dir, int pos){
        BusStop bs= getPS(dir,pos);
        Marker marker = mMap.addMarker(new MarkerOptions().position(bs.getLL()).title(bs.getName()));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(res, R.drawable.green)));
        return marker;
    }


    public void espTiempo(int Time) {
        try {
            Thread.sleep(Time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
