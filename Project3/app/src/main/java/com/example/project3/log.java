package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

public class log extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        //Get shared prefs, get text views that will change, get strings from shared prefs
        SharedPreferences myprefs = getSharedPreferences("SharedPrefApp", MODE_PRIVATE);
        TextView t = findViewById(R.id.tittless);
        TextView d = findViewById(R.id.datess);
        TextView p = findViewById(R.id.pri);
        TextView time = findViewById(R.id.timess);
        //set the prefrences to the text views
        t.setText(myprefs.getString("DATE", null));
        d.setText( myprefs.getString("DATE",null));
        p.setText( myprefs.getString("PRIORITY",null));
        time.setText( myprefs.getString("TIME",null));

    }
//intent to go back to the main activity
    public void backHomeButton(View view) {
        Intent myIntent = new Intent(log.this, MainActivity.class /*from, to*/);
        startActivity(myIntent);
    }
}