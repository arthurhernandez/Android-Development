package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class addReminder extends AppCompatActivity {
    int Priority = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
    }

    //this will add a reminder to the list
    public void addButton(View view) {
        Intent myIntent = new Intent(addReminder.this, MainActivity.class /*from, to*/);
        //get the values for title and date
        EditText title = findViewById(R.id.titlebox);
        EditText date = findViewById(R.id.datebox);
        String titles = title.getText().toString();
        String dates = date.getText().toString();

        //check to make sure all values are accounted for
        if(titles.length() != 0 && dates.length() != 0 && Priority != 0){
            //change activities
            //pass through our values using intent extra
            myIntent.putExtra("NEWTITLE",titles);
            myIntent.putExtra("NEWDATE",dates);
            myIntent.putExtra("NEWICON",Priority + "");
            //start next activity
            startActivity(myIntent);

        }else{
            // tell the user that their values are not valid.
            TextView change = findViewById(R.id.textView3);
            change.setText("Not enough information to create a reminder, try again");
        }
    }
//check to make sure one of the boxes is checked
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked if both are click the last one will have preference
        switch(view.getId()) {
            case R.id.lowPrioirityCheckbox:
                if (checked){
                    Priority = 1;
                    break;

                }
            case R.id.highPriorityCheckBox:
                if (checked) {
                    Priority = 2;
                    break;
                }
        }
    }
//start a new intent to the data activity
    public void dataLogButton(View view) {
        Intent myIntent = new Intent(addReminder.this, log.class /*from, to*/);
        startActivity(myIntent);
    }


}