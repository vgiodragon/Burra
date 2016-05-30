package com.example.giovanny.burra;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.giovanny.burra.Estaciones.MarkersYMas;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private Context c = this;
    private int TiempoCiclo = 2800;
    boolean activado , camaraBurra;
    GoogleMap mMap;
    double l1 = -12.045196, l2 = -77.032163;
    ArrayAdapter<CharSequence> adapter;
    private final int code_request = 1234;
    Marker mN,mS,mE, mBurra;
    ArrayList<Marker> mparadero;
    ConexionServer cs;
    String bus;
    MarkersYMas paraderos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        cs = new ConexionServer();
        mparadero = new ArrayList<>();
        paraderos = new MarkersYMas();

        camaraBurra=false;
        bus = "Sur";
        Spinner spinner = (Spinner) findViewById(R.id.spiBurra);
        spinner.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.burra_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        code_request);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        code_request);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == code_request ) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mMap.setIndoorEnabled(true);
                    mMap.setBuildingsEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                }
            } else {
            }
        }
    }

    public void CamaraBurra(View view){
        double []ll = getLanLon();
        if(ll[0]!=-12.045196 && ll[1]!=-77.032163 && !bus.equals("Las Tres")){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(l1, l2)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activado = true;
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
                    paraderos.espTiempo(TiempoCiclo);
                    if(bus.equals("Las Tres")){
                        new updateMap(c, "Sur",true).execute();
                        new updateMap(c, "Norte",true).execute();
                        new updateMap(c, "Este",true).execute();
                    }
                    else{
                        new updateMap(c, bus,false).execute();
                    }
                }
            }
        }).start();
    }

    private void setUpMap() {
        LocationManager service = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = service.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LatLng userLocation = (location != null) ? new LatLng(location.getLatitude(),location.getLongitude()) :
                new LatLng(-12.0160576,-77.0497945);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

    }

    private void setParaderos(String bus){
        for (int i=0;i<paraderos.getPLength(bus);i++){
            mparadero.add(paraderos.getMarker(mMap,getResources(),bus,i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Credits:
                Toast.makeText(getApplicationContext(),"Burra V1\n" +
                                "Esto fue creado por Estudiantes para Estudiantes.\n" +
                                "Sugerencias, quejas o insultos con Gian Nicola Monero ;)" +
                        "\nAtte. GioDev", Toast.LENGTH_LONG).show();
                return true;
        }
        return true;
    }

    public void RemoveMarkers(){
        while(mparadero.size()>0){
            mparadero.get(0).remove();
            mparadero.remove(0);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!bus.equals(adapter.getItem(position))){
            bus= (String) adapter.getItem(position);
            if(bus.equals("Las Tres")){
                setParaderos("Norte");
                setParaderos("Sur");
                setParaderos("Este");
            }
            else{
                RemoveMarkers();
                setParaderos(bus);
                camaraBurra=true;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
                mMap.setMyLocationEnabled(true);
                //mMap.setIndoorEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                setParaderos(bus);
                setUpMap();
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
            //mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            setParaderos(bus);
            setUpMap();
        }
        HiloLanzador();
    }

    public class updateMap extends AsyncTask<String, String , String> {
        Context context;
        String bus2;
        boolean todas;

        public updateMap(Context context, String bus,boolean todas) {
            this.context = context;
            this.bus2 = bus;
            this.todas=todas;
        }

        @Override
        protected String doInBackground(String... args) {
            String respues = "...";
            try {
                String urlfin = bus2;
                respues = cs.sendToUrl(urlfin);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return respues;
        }

        @Override
        protected void onPostExecute(final String res) {
            final String aca = res;
            if(todas)
                MarkerTodas(aca, bus2);
            else MarkerUna(aca, bus2);
        }
    }

    public void MarkerTodas(final String aca, final String bus){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String []part= aca.split(":");
                if(part[0]!=null) {
                    double l1 = Double.parseDouble(part[0]);
                    double l2 = Double.parseDouble(part[1]);

                    if (bus.equals("Norte")) {
                        if (mN != null) mN.remove();
                        mN = mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title("Burra" + bus));
                        mN.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blue)));
                    } else if (bus.equals("Sur")) {
                        if (mS != null) mS.remove();
                        mS = mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title("Burra" + bus));
                        mS.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red)));
                    } else if (bus.equals("Este")) {
                        if (mE != null) mE.remove();
                        mE = mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title("Burra" + bus));
                        mE.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yellow)));
                    }
                }
            }
        });
    }

    public void MarkerUna(final String aca, final String bus){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String []part= aca.split(":");
                if(part[0]!=null) {
                    setLanLon(Double.parseDouble(part[0]),Double.parseDouble(part[1]));
                    LimpioMarkers();
                    if(camaraBurra){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(l1, l2)));
                        camaraBurra=false;
                    }
                    mBurra = mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title("Burra" + bus));
                    if (bus.equals("Norte")) {
                        mBurra.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blue)));
                    } else if (bus.equals("Sur")) {
                        mBurra.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red)));
                    } else if (bus.equals("Este")) {
                        mBurra.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yellow)));
                    }
                }
            }
        });
    }

    public void LimpioMarkers(){
        if (mBurra != null) mBurra.remove();
        if (mN != null) mN.remove();
        if (mS != null) mS.remove();
        if (mE != null) mE.remove();
    }

    public synchronized void setLanLon (double l1,double l2){
        this.l1=l1;
        this.l2=l2;
    }

    public synchronized double [] getLanLon(){
        return new double[]{l1,l2};
    }
}
