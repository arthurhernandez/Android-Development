package com.example.c323midtermprojectaohernan;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.example.c323midtermprojectaohernan.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String intentData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binds the map to the layout inflator aka activity
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        intentData = myIntent.getStringExtra("LOCATION");

        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(intentData, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address addy = addresses.get(0);
        //get the lat and long  and use them to create a new latlang which will be used to create a marker
        float latitude = (float) addy.getLatitude();
        float longitude = (float) addy.getLongitude();

        LatLng markerLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(markerLocation).title(intentData));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));

        LatLng currentLocation = new LatLng(41.67786610573844, -87.72919611561927);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));

        PolylineOptions line=
                new PolylineOptions().add(new LatLng(currentLocation.latitude,
                                currentLocation.longitude),
                        new LatLng(markerLocation.latitude,
                                markerLocation.longitude))
                        .width(5).color(Color.RED);

        mMap.addPolyline(line);
    }
}