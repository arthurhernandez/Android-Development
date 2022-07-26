package com.example.mybmicalculator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //get content from preferences
        SharedPreferences myprefs = getSharedPreferences("SharedPrefApp", MODE_PRIVATE);
        TextView output = findViewById(R.id.greeting);
        String name = myprefs.getString("NAME",null);
        output.setText("Hello " + name + "!");

        Calendar myCal = Calendar.getInstance();
        Date now = myCal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String dateStr = sdf.format(now);
        int hour = Integer.parseInt(dateStr.substring(0,2));
        TextView greeting = findViewById(R.id.weather);
        String time = "";
        if(hour > 1 && hour < 12){
            time = "Good Morning!";
        }else if(hour > 12 && hour < 2){
            time = "Good Afternoon!";
        }else if(hour > 2){
            time = "Good Evening!";
        }else{
            time = "I hope you're having a good day!";
        }

        greeting.setText(time);


    }


    public void calculatebmi(View view) {
        // go to next activity
        Intent myIntent = new Intent(welcome.this, calculate.class /*from, to*/);
        startActivity(myIntent);
    }
}

