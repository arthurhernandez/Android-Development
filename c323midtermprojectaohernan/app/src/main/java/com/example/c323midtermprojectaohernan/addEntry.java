package com.example.c323midtermprojectaohernan;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This Activity is used to save information of Entries based on their type.
 * saves name, location, best time to visit, price, history and image to json file
 */
public class addEntry extends AppCompatActivity implements addMonumentFragment.myMonumentFragmentInterface, addCityFragment.myCityFragmentInterface, addCampFragment.myCampingFragmentInterface{
    String intentExtra;
    //instantiations for camera and image
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    //instantiations for location
    LocationManager myLocationManager;
    LocationListener myLocationListener;
    float lat, lang;
    TextView currentLocationTextView;
    String myCurrentLocation = "";
    String locationForEvent = "";
    //getting values from fragment
    String name;
    String bestTimeToVisit;
    String priceOrMetropolitanOrTourist;
    //only for monument
    String historyOfMonument = "";
    //json object for adding data
    JSONArray data = new JSONArray();
    JSONObject entry;

    /**
     * Chooses what type of fragment to open
     * city, monument, or camping
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        //get the type of entry that will be entered from previous activity
        Intent myIntent = getIntent();
        intentExtra = myIntent.getStringExtra("NEWENTRY");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(intentExtra.equals("CITY")){
            addCityFragment cityFrag = new addCityFragment();
            fragmentTransaction.replace(R.id.entryLayout, cityFrag);
            fragmentTransaction.commit();
        }else if(intentExtra.equals("MONUMENT")){
            addMonumentFragment monumentFrag = new addMonumentFragment();
            fragmentTransaction.replace(R.id.entryLayout, monumentFrag);
            fragmentTransaction.commit();
        }else if(intentExtra.equals("CAMPING")){
            addCampFragment campFrag = new addCampFragment();
            fragmentTransaction.replace(R.id.entryLayout, campFrag);
            fragmentTransaction.commit();
        }

        currentLocationTextView = findViewById(R.id.locationTextView);
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
                geocoder= new Geocoder(addEntry.this, Locale.getDefault());

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

    /**
     *
     * @param requestCode to check what permission is being requested
     * @param resultCode to check to see if it is being allowed or denied
     * @param data what data is being passed back aka the image in camera request
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
    //passes an inent to the camera app to capture an image is permission is granted
    @SuppressLint("NewApi")
    public void goTocameraAppOnclick(View view) {
        this.imageView = (ImageView) this.findViewById(R.id.capturedImage);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    //save the current location from location listener to @locationForEvent
    public void addcurrentLocationOnclick(View view) {
        currentLocationTextView.setText(myCurrentLocation);
        locationForEvent = myCurrentLocation;
    }

    /**
     * @param requestCode to check what permission is being requested
     * @param permissions what type of permission is to be checked
     * @param grantResults allows or does not allow them to be activated
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == 13){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
                Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show();

            }
        }
    }

    /**
     *
     * @param monumentName name of monument to name
     * @param monumentHistory is saved to historyOfMonument
     *                        This is the only entry type to have history
     * @param monumentBestTimeToVisit saved to bestTimeToVisit
     * @param monumentPrice saved to priceOrMetropolitanOrTourist changes meaning from entry to entry
     */
    @Override
    public void MonumentChange(String monumentName, String monumentHistory, String monumentBestTimeToVisit, String monumentPrice) {
        name = monumentName;
        bestTimeToVisit = monumentBestTimeToVisit;
        priceOrMetropolitanOrTourist = monumentPrice;
        //only for monument
        historyOfMonument = monumentHistory;
    }

    @Override
    public void CityChange(String CityName, String CitySpots, String cityBestTimeToVisit) {
        name = CityName;
        bestTimeToVisit = cityBestTimeToVisit;
        priceOrMetropolitanOrTourist = CitySpots;
    }
    @Override
    public void CampingChange(String campName, String campingBestTimeToVisit, String campingArea) {
        name = campName;
        bestTimeToVisit = campingBestTimeToVisit;
        priceOrMetropolitanOrTourist = campingArea;
    }

    public void addEntryOnClick(View view) throws JSONException, IOException {
        if(!isExternalStorageAvailable())
            return;
        entry = new JSONObject();
        entry.put("type", intentExtra);
        entry.put("location", "Location: " + locationForEvent);
        entry.put("name", "Name: " + name);
        entry.put("bestTimeToVisit", "Best time to visit: " + bestTimeToVisit);

        if(intentExtra.equals("MONUMENT")){
            entry.put("priceOrMetropolitanOrTourist", "Price of monument: " + priceOrMetropolitanOrTourist);
        }else if(intentExtra.equals("CITY")){
            entry.put("priceOrMetropolitanOrTourist", "Tourist Spots: " + priceOrMetropolitanOrTourist);
        }else{
            entry.put("priceOrMetropolitanOrTourist", "Closest metropolitan area: " + priceOrMetropolitanOrTourist);
        }

        if(intentExtra.equals("MONUMENT")){
            entry.put("historyOfMonument", "History of monument: " + historyOfMonument);
        }else{
            entry.put("historyOfMonument", " ");
        }
        entry.put("image", 1);
        data.put(entry);

        String dataInText = data.toString();
        FileOutputStream fos = openFileOutput("entries", MODE_PRIVATE);
        fos.write(dataInText.getBytes());
        fos.close();

    }

    /**
     *
     * @returns true if we have external storage available, false if not
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}