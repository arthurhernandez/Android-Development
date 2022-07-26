package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

//my location manager assumes that you do not have enough time to input and event and date before a location is processed
public class MainActivity extends AppCompatActivity {
    //instantiate and set views
    //everything for location
    LocationManager myLocationManager;
    LocationListener myLocationListener;
    float lat, lang;
    TextView currentLocationTextView;
    boolean locationButtonClicked = false;
    String myCurrentLocation = "";
    String locationForEvent = "";
    //everything for the other 2 parameters
    EditText eventInformation;
    EditText dateInformation;
    //output to header if there is an error in inputs
    TextView header;
    //create a new json Array
    JSONArray data = new JSONArray();
    //create an object for the event list
    JSONObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                geocoder= new Geocoder(MainActivity.this, Locale.getDefault());

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

    //this is the onlcick method for the current location button and will set the current location for the event
    public void CurrentlocationOnclick(View view) throws JSONException, IOException {
        currentLocationTextView.setText(myCurrentLocation);
        locationForEvent = myCurrentLocation;
        locationButtonClicked = true;

    }

    //this onlcick method sends the user to the second activity (events list)
    public void viewEventsOnclick(View view) {
        //create intent to event list activity
        Intent myIntent = new Intent(MainActivity.this, listActivity.class /*from, to*/);
        startActivity(myIntent);
    }

    //onclick method allows the user to add an event to the list
    public void addEventOnclick(View view) throws IOException, JSONException {
        //find views needed to create an event
        header = findViewById(R.id.header);
        eventInformation = findViewById(R.id.eventInformationTextBox);
        dateInformation = findViewById(R.id.enterDataTextBox);
        //check to see if the date is valid in a separate method returning true if it is
        Boolean valid = validateDate(dateInformation.getText().toString());
        //check to see that there is a valid current location, event information, and date information
        if(eventInformation.getText().toString().matches("") || !valid || !locationButtonClicked){
            if(eventInformation.getText().toString().matches("") && !valid && !locationButtonClicked) {
                header.setText("Please enter an event, date and current location");
            }
            else if(eventInformation.getText().toString().matches("") && !valid){
                header.setText("Please enter an event and date");
            }
            else if(eventInformation.getText().toString().matches("")){
                header.setText("Please enter an event");
            }
            else if(!valid){
                header.setText("Please enter a date");
            }
            else if(!locationButtonClicked){
                header.setText("Please enter a location");
            }
        }else {
            //if everything checks out save the location, date, and event info to json file
            header.setText("Everything Checks out");
            addEvent();

        }
    }
    //this method will add an event to the json array everytime the button is clicked and returns a full event
    private void addEvent() throws JSONException, IOException {
        if(!isExternalStorageAvailable())
            return;
        //new event with location, date, and event info (add the lat and long for the map)
        event = new JSONObject();
        event.put("location", locationForEvent);
        event.put("date", dateInformation.getText().toString());
        event.put("eventInfo", eventInformation.getText().toString());
        data.put(event);
        String dataInText = data.toString();
        FileOutputStream fos = openFileOutput("events", MODE_PRIVATE);
        fos.write(dataInText.getBytes());
        fos.close();
    }
    //check to see that we have external storage available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    //method to validate that we have a date in date format mm/dd/yyyy
    private boolean validateDate(String date) {
        if(date.matches("") || date.length() != 10){
            return false;
        }
        int month = Integer.parseInt(date.substring(0,2));
        int day = Integer.parseInt(date.substring(3,5));
        Log.v("month",month + "");
        Log.v("day",day + "");
        if(date.charAt(2) == '/' && date.charAt(5) == '/' && month >= 1 && month <= 12 && day >= 1 && day <= 31 && date.length() == 10){
            return true;
        }
        return false;
    }

}