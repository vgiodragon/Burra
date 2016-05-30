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
    BusStop[] pS = new BusStop[]{new BusStop(-12.153654, -76.9725183,"Paradero Ciudad"),
            new BusStop(-12.150713, -76.979516,"Paradero Atocongo"),
            new BusStop(-12.170875, -76.945882,"Paradero Pesquero"),
            new BusStop(-12.166885, -76.949351,"Paradero Electra"),
            new BusStop(-12.159503, -76.956007,"Paradero San Gabriel"),
            new BusStop(-12.158397, -76.960507,"Paradero Hospital"),
            new BusStop(-12.181885, -76.942734,"Paradero La Curva"),
            new BusStop(-12.1408939,-76.9809145,"Paradero Flores"),
            new BusStop(-12.122619,-76.9769939,"Paradero Pedagógico")

    };

    BusStop[] pN = new BusStop[]{new BusStop(-11.9273604,-77.0556201,"Paradero Trapiche"),
            new BusStop(-11.9405726,-77.0743999,"Paradero Pro")
    };
    BusStop[] pE = new BusStop[]{
            new BusStop(-11.948481, -76.983460 ,"Paradero 5"),
            new BusStop(-11.950391, -76.987078 ,"Paradero Electra"),
            new BusStop(-11.951677, -76.987730,"Paradero Plaza Vea"),
            new BusStop(-11.955944, -76.985794,"Paradero Seoane"),
            new BusStop(-11.959027, -76.987206,"Paradero Complejo"),
            new BusStop(-11.960672, -76.988341,"Paradero Grifo 7"),
            new BusStop(-11.962802, -76.989799,"X"),
            new BusStop(-11.964056, -76.990641,"Y"),
            new BusStop(-11.966571, -76.992533,"Z"),
            new BusStop(-11.968403, -76.993858,"Paradero Banchero"),
            new BusStop(-11.971910, -76.996442,"Paradero San Martin"),
            new BusStop(-11.974451, -77.000026,"Paradero Canto Rey"),
            new BusStop(-11.975759, -77.001659,"Paradero Metro"),
            new BusStop(-11.977774, -77.003901,"Paradero El bosque"),
            new BusStop(-11.979234, -77.004813,"Paradero Sedapal"),
            new BusStop(-11.981104, -77.005603,"Paradero Duraznos"),
            new BusStop(-11.982783, -77.006172,"Paradero Ciruelos"),
            new BusStop(-11.984631, -77.006890,"Paradero San Carlos"),
            new BusStop(-11.986478, -77.007908,"Paradero 27"),
            new BusStop(-11.989219, -77.010609,"Paradero 25"),
            new BusStop(-11.993359, -77.010997,"Paradero 23"),
            new BusStop(-11.996467, -77.010046,"Paradero Los Postes"),
            new BusStop(-11.999779, -77.009132,"Paradero 19"),
            new BusStop(-12.002943, -77.008140,"Paradero Los Ebanos"),
            new BusStop(-12.005825, -77.006307,"Paradero Los Jardines"),
            new BusStop(-12.006775, -77.005749,"Paradero Metro"),
            new BusStop(-12.009156, -77.004175,"Paradero Britanico"),
            new BusStop(-12.010805, -77.003141,"Paradero Huiracocha"),
            new BusStop(-12.012846, -77.002259,"Paradero Celima"),
            new BusStop(-12.016472, -77.001998,"Paradero Ascarrus"),
            new BusStop(-12.017799, -77.002750,"Paradero Malvinas"),
            new BusStop(-12.020144, -77.005015,"Paradero Teatro"),
            new BusStop(-12.023001, -77.007781,"A"),
            new BusStop(-12.025368, -77.009949,"B"),
            new BusStop(-12.027905, -77.011377,"Paradero Caja de Agua"),
            new BusStop(-12.029649, -77.011887,"Paradero Consejo")


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
