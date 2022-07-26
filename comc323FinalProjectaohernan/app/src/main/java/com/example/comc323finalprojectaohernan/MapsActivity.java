package com.example.comc323finalprojectaohernan;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.comc323finalprojectaohernan.databinding.ActivityMaps2Binding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;

    LocationManager myLocationManager;
    LocationListener myLocationListener;
    float lat, lang;
    String locationForEvent = "";
    boolean locationButtonClicked = false;
    String myCurrentLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //check that provider and location are enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.v("LOCATIONAPP", "Provider enabled: " + myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            Log.v("LOCATIONAPP", "Location enabled: " + myLocationManager.isLocationEnabled());
        } else {
            //do soemthing about it
            Log.v("LOCATIONAPP", "Location/Provider disabled!");
        }
        //listen for location
        myLocationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
            //everytime location changes get the new location
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //geolocator to use lat and long to find an address
                Geocoder geocoder;
                List<Address> addresses;
                geocoder= new Geocoder(MapsActivity.this, Locale.getDefault());

                float latitude = (float)location.getLatitude();
                float longitude = (float) location.getLongitude();
                //not necessary but if buggy I redefine my lat and long
                lat = (float)location.getLatitude();
                lang = (float) location.getLongitude();
                try {
                    //finds the adddress, if it cant find the address it will find a feature name
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if(addresses.get(0).getSubThoroughfare() != null && addresses.get(0).getThoroughfare() != null){
                        myCurrentLocation = addresses.get(0).getSubThoroughfare() + " " + addresses.get(0).getThoroughfare();
                    }else{
                        myCurrentLocation = addresses.get(0).getFeatureName();
                    }

                    Log.v("FEATURENAME",addresses.get(0).getFeatureName());
                    Log.v("Sub+THRU",addresses.get(0).getSubThoroughfare() + addresses.get(0).getThoroughfare());
                } catch (IOException e) {
                    // if we are not allowed to get it return
                    Log.v("OOPS","OPPS");
                    e.printStackTrace();
                }
            }
        };

        //checks for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            }
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
        }

        locationForEvent = myCurrentLocation;
    }

    //makes sure to correct any incorrectly set permissions
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 13:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent myIntent = getIntent();
        String loc = myIntent.getStringExtra("location");
        //we will reverse geocode the location
        //this step may create an incorrect marker depending on the specificity of tbe location
        Geocoder geocoder= new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(loc, 1);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Address addy = addresses.get(0);
        //get the lat and long  and use them to create a new latlang which will be used to create a marker
        float latitude = (float)addy.getLatitude();
        float longitude = (float) addy.getLongitude();

        LatLng marker = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(marker).title(loc));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}