package com.example.comc323proj7aohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Arthur Hernandez
 * Project 7
 * Tuesday Oct 26th
 * This Activity gets the weather from the user specification.
 * if the user does not specify a location, this activity will also find the phone/
 * emulator location on create and use it instead.
 * This activity also implements an overflow menu
 */
public class MainActivity extends AppCompatActivity {
    //instantiations for location
    LocationManager myLocationManager;
    LocationListener myLocationListener;
    float lat, lang;
    TextView currentLocationTextView;
    String city;
    String state;
    String myCurrentLocation = "";
    String lats = "";
    String langs = "";
    //instantiations for getting weather
    EditText latEditText;
    EditText langEditText;
    TextView todaysWeather;
    TextView description;
    TextView temperature;
    TextView feelsLike;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appId = "7d07737fd10de3420fa2c7b05918aa5d";

    /**
     * This oncreate method gets the location of the user and displays it
     * @param savedInstanceState is the saved state that the method will use to create the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find th textview so we can set it to our current location
        currentLocationTextView = findViewById(R.id.currentLocationTextView);
        //create a location manager
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
            /**
             * creates an intent to the location settings
             * @param provider is the provider for the settings action
             */
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            /**
             * checks to see if the location has changed
             * if it has, update it
             * @param location is current location context of the phone / emulator
             */
            //everytime location changes get the new location
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //geolocator to use lat and long to find an address
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                float latitude = (float) location.getLatitude();
                float longitude = (float) location.getLongitude();

                try {
                    //finds the adddress, if it cant find the address it will find a feature name
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.get(0).getSubThoroughfare() != null && addresses.get(0).getThoroughfare() != null) {
                        //not necessary but if buggy I redefine my lat and long
                        //saved in case no lat and lang are produced by the user
                        lat = (float) location.getLatitude();
                        lang = (float) location.getLongitude();
                        //only used to show the user their location
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();

                        myCurrentLocation = city + ", " + state;
                        currentLocationTextView.setText("Current Location: " + myCurrentLocation);
                    } else {
                        myCurrentLocation = addresses.get(0).getSubThoroughfare() + " " + addresses.get(0).getThoroughfare();
                        currentLocationTextView.setText(myCurrentLocation);
                    }
                 } catch (IOException e) {
                    // if we are not allowed to get it return
                    Log.v("OOPS", "OPPS");
                    e.printStackTrace();
                }
            }
        };

        //checks for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            }
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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
     * Used to inflate the options menu when clicked on
     * @param menu gets the empty
     * @return super.onCreateOptionsMenu(menu); the inflated options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Used to
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.weatherActivty:
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class /*from, to*/);
                startActivity(myIntent);
                return true;
            case R.id.movieActivty:
                Intent mIntent = new Intent(MainActivity.this, MovieListActivity.class /*from, to*/);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getWeatherDataOnClick(View view) {
        latEditText = findViewById(R.id.latitudeEditText);
        langEditText = findViewById(R.id.longitudeEditText);
        todaysWeather= findViewById(R.id.todaysWeatherTextView);
        description= findViewById(R.id.descriptionTextView);
        temperature= findViewById(R.id.temperatureTextView);
        feelsLike= findViewById(R.id.feelsLikeTextView);
        String realUrl = "";
        lats = latEditText.getText().toString().trim();
        langs = langEditText.getText().toString().trim();

        if(lats.length() == 0 || langs.length() == 0 ){
            //useCurrentLocation
            Log.v("WEEEE","city " + city);
            realUrl = url + "?lat=" + lat + "&lon=" + lang + "&appid=" + appId;
        }else if(myCurrentLocation.length() == 0){
            TextView textV = findViewById(R.id.textView2);
            textV.setText("Please wait for your current location to load");
        }
        else{
            //use the inputs
            realUrl = url + "?lat=" + lats + "&lon=" + langs + "&appid=" + appId;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, realUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DecimalFormat df = new DecimalFormat("#.##");
                Log.v("WEEEE","we did it!" + response);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String main = jsonObjectWeather.getString("main");
                    String descript = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feels = jsonObjectMain.getDouble("feels_like") - 273.15;

                    todaysWeather.setText("Today's Weather: " + main);
                    description.setText("Description: " + descript );
                    temperature.setText("Temperature: " + df.format(temp)+ " °C");
                    feelsLike.setText("Feels Like: " + df.format(feels)+ " °C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please enter a valid location", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Goes to the movie list if clicked
     * @param view
     */
    public void goToMovieListOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, MovieListActivity.class /*from, to*/);
        startActivity(myIntent);
    }
}