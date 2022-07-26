package com.example.project5;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener, GestureDetector.OnGestureListener, View.OnTouchListener {
    //instantiate all sensors and their text views
    private SensorManager mySensorManager;
    Sensor pressures;
    Sensor temperatures;
    Sensor lights;
    TextView pressure;
    TextView temp;
    TextView light;
    //instantiate gesture detection and button view
    private GestureDetector guesterDetector;
    Button button;
    //instantiate necessary location variables
    TextView city;
    TextView state;
    LocationListener myLocationListener;
    LocationManager myLocationManager;
    double lat;
    double lang;

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the gesturebutton as a view
        button = findViewById(R.id.button);

        button.setOnTouchListener(this);
        guesterDetector = new GestureDetector(this,this);
        //set views
        temp = findViewById(R.id.temperatureText);
        pressure = findViewById(R.id.pressureText);
        light = findViewById(R.id.lightText);
        city = findViewById(R.id.citytext);
        state = findViewById(R.id.statetext);
        //create a sensor manager and sensors
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressures = mySensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        lights = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temperatures = mySensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        //create a listener for the location
        myLocationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
            //if location changed (simulators dont have set location beforehand)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //create reverse geocoder
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses;
                lat = location.getLatitude();
                lang = location.getLongitude();
                //try to set the location using gecoder
                try {
                    addresses = geocoder.getFromLocation(lat, lang, 1);
                    city.setText(addresses.get(0).getLocality());
                    state.setText(addresses.get(0).getAdminArea());
                } catch (IOException e) {
                    // if we are not allowed to get it return
                    e.printStackTrace();
                }
            }
        };
        //check for permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, myLocationListener);
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        guesterDetector.onTouchEvent(motionEvent);
        return true;
    }
    //we want to keep listening for any gestures in case onfling isnt the first press
    //minimizing space for all that do not matter
    @Override public boolean onDown(MotionEvent motionEvent) { return false; }
    @Override public void onShowPress(MotionEvent motionEvent) { }
    @Override public boolean onSingleTapUp(MotionEvent motionEvent) { return false; }
    @Override public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }
    @Override public void onLongPress(MotionEvent motionEvent) { }
    //AHHH finally on fling
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.v("DOWN","FLINGGGGG");
        Intent myIntent = new Intent(MainActivity.this, second_activity.class /*from, to*/);
        startActivity(myIntent);
        return false;
    }

    //whenever the sensors changes we want to update it
    @Override
    public final void onSensorChanged(SensorEvent event) {
        //check to see what sensor you want to get values for
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float tem = event.values[0];
            temp.setText(tem + "Degrees Celsius");
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float millibarsOfPressure = event.values[0];
            pressure.setText(millibarsOfPressure + "Millibars");
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float ligh = event.values[0];
            light.setText(ligh+ "Lux");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    //I dont need anything here
    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mySensorManager.registerListener(this, pressures, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, temperatures, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, lights, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mySensorManager.unregisterListener(this);
    }

    public void buttonclick(View view) {
        //Intent myIntent = new Intent(MainActivity.this, second_activity.class /*from, to*/);
        //startActivity(myIntent);
    }
}