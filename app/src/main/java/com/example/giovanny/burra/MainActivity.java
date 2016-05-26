package com.example.giovanny.burra;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Context c = this;
    private int TiempoCiclo=1000;
    boolean activado;
    GoogleMap mMap;
    CameraUpdate mCamera;
    double l1=-12.045196, l2=-77.032163;
    String url ="http://52.39.235.232:8081/petition/";
    ArrayAdapter<CharSequence> adapter;
    private final int code_request=1234;
    Marker m;
    ConexionServer cs;
    String bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, code_request);
        }
        cs= new ConexionServer();


        setUpMapIfNeeded();
        paraderos();

        bus="Norte";
        Spinner spinner = (Spinner) findViewById(R.id.spiBurra);
        spinner.setOnItemSelectedListener(this);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.burra_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case code_request:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "COARSE LOCATION permitido", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    Toast.makeText(MainActivity.this, "COARSE LOCATION no permitido", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        activado=true;
        HiloLanzador();
        mMap.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activado = false;
    }
    private void HiloLanzador() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (activado) {
                    //double l1=-12.045196, l2=-77.032163;

                    espTiempo(TiempoCiclo);


                    new updateMap(c).execute();
                }
            }
        }).start();
    }
    public void espTiempo(int Time){
        try {
            Thread.sleep(Time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void setUpMapIfNeeded(){
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mMap != null) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                setUpMap();
            }

        }
    }

    private void setUpMap() {
        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0451952, -77.0321625), 11);
        mMap.animateCamera(mCamera);

    }
    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2) {
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .alpha(opacity)
                .anchor(dimension1, dimension2));
    }
    private void paraderos(){
        m  = mMap.addMarker(new MarkerOptions().position(new LatLng(-12.0451952,-77.0321625)).title("Paradero Burra"));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bus= (String) adapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class updateMap extends AsyncTask<String, String , String>{
        Context context;
        public updateMap(Context context){
            this.context=context;

        }

        @Override
        protected String doInBackground(String... args){
            String respues="...";

            try {

                    String urlfin=url + bus;

                    Log.d("localizacion", urlfin);
                    respues = cs.sendToUrl(urlfin);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return respues;
        }
        @Override
        protected void onPostExecute(final String res) {
            final String aca=res;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String []part= aca.split(":");
                    l1=Double.parseDouble(part[1]);
                    l2=Double.parseDouble(part[0]);
                    Log.d("localizacion","_"+l1+"_"+l2);
                    m.remove();
                    m  =   mMap.addMarker(new MarkerOptions().position(new LatLng(l1,l2)).title("Burra"));
                }
            });
        }
    }
}
