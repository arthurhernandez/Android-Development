package com.example.locationapp;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.locationapp.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

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
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;
        //try to add markers from json file
        try {
            addMarkers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addMarkers() throws IOException, JSONException{
        //open file and read from it char by char
        FileInputStream fis = openFileInput("events");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while(bis.available() != 0) {
            b.append((char)bis.read());
        }
        //close file
        bis.close();
        fis.close();
        JSONArray data = new JSONArray(b.toString());

        Log.v("MAPMARKERS", "wb now");
        //for every object in events we will take its location
        for (int i=0; i<data.length(); i++){
            String location = data.getJSONObject(i).getString("location");
            //we will reverse geocode the location
            //this step may create an incorrect marker depending on the specificity of tbe location
            Geocoder geocoder= new Geocoder(MapsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            Address addy = addresses.get(0);
            //get the lat and long  and use them to create a new latlang which will be used to create a marker
            float latitude = (float)addy.getLatitude();
            float longitude = (float) addy.getLongitude();

            LatLng marker = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(marker).title(data.getJSONObject(i).getString("eventInfo")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            Log.v("MAPMARKERS", "SET MARKER @" + latitude + ", " + longitude);
        }
    }
}