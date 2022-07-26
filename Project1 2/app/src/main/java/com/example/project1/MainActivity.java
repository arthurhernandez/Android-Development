package com.example.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void convertButtonCallBack(View view) {
        //find and store inputs and output views by id
        EditText mins = findViewById(R.id.minInput);
        TextView output = findViewById(R.id.secondsOut);
        //change input to a double
        // must be some type of double no matter what since capturing input from user through keypad
        double minDoubleType = Double.parseDouble(mins.getText().toString());
        //convert
        double seconds = minDoubleType * 60;
        //print output as a string
        output.setText(String.valueOf(seconds));
    }
}