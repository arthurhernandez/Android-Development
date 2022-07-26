package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {
    private List<Event> myEvents;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //try to populate the events list through the event object
        try {
            populateMyEvents();
            Log.v("LISTACTIVITY", "we Populated");
        } catch (IOException e) {
            Log.v("LISTACTIVITY", "we got stuck @ IOException" );
            e.printStackTrace();
        } catch (JSONException e) {
            Log.v("LISTACTIVITY", "we got stuck @ JSONException ");
            e.printStackTrace();
        }
        populateList();
    }

    private void populateMyEvents() throws IOException, JSONException {
        //opens json file
        myEvents = new ArrayList<Event>();
        FileInputStream fis = openFileInput("events");
        BufferedInputStream bis = new BufferedInputStream(fis);
        //reads it char by char
        StringBuffer b = new StringBuffer();
        while(bis.available() != 0) {
            b.append((char)bis.read());
        }
        bis.close();
        fis.close();
        //get the json data array
        JSONArray data = new JSONArray(b.toString());
        //for the length of the amount of objects create a new event
        for (int i=0; i<data.length(); i++){
            myEvents.add(new Event(data.getJSONObject(i).getString("location"),
                    data.getJSONObject(i).getString("date"), data.getJSONObject(i).getString("eventInfo"), i));
        }
    }

    private void populateList() {
        //create a listview adapter
        ArrayAdapter<Event> myAdapter = new MyCustomListAdapter();
        listView = findViewById(R.id.listView);
        listView.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Event> {
        //create a customlist adapter that finds each value in our custom layout
        public MyCustomListAdapter() {
            super(listActivity.this, R.layout.listlayout , myEvents);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.listlayout, parent, false);
            //find all views in custom layout and do something with them
            Event currentEvent = myEvents.get(position);
            TextView textViewEvent = itemView.findViewById(R.id.eventTextView);
            TextView textViewDate = itemView.findViewById(R.id.dateTextView);
            TextView textViewLocation = itemView.findViewById(R.id.locationTextView);
            Button deleteButton = itemView.findViewById(R.id.deleteButton);
            textViewLocation.setText(currentEvent.getLocation());
            textViewDate.setText(currentEvent.getDate());
            textViewEvent.setText(currentEvent.getEvent());
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    currentEvent.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });
            return itemView;

        }
    }
    
    //check to see if search button was clicked
    public void searchButtonOnClick(View view) throws IOException, JSONException {
        EditText searchDateText = findViewById(R.id.searchTextBox);
        String searchDate = searchDateText.getText().toString();
        TextView title = findViewById(R.id.textView);
        //if it was validate the date inputted
        if(validateDate(searchDate)){
            //everything about the date input checks out use a different method to only get a filtered list
            filteredEventList(searchDate);
            //populate that list
            populateList();

        }else if(searchDate.length() == 0){
            //if the user deletes the search and is wondering why nothing is popping up populate regular list
            populateMyEvents();
            populateList();
        }
        else{
            //tell the user to enter a valid date
            title.setText("Please enter a valid date mm/dd/yyyy");
        }
    }
    //used to filter through what json objects should be used
    private void filteredEventList(String searchDate) throws IOException, JSONException {
        //substring the date given to us to month date and year
        int monthSearchDate = Integer.parseInt(searchDate.substring(0,2));
        int daySeachDate = Integer.parseInt(searchDate.substring(3,5));
        int yearSearchDate = Integer.parseInt(searchDate.substring(6,10));
        //same read from json file as beforehand
        myEvents = new ArrayList<Event>();
        FileInputStream fis = openFileInput("events");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while(bis.available() != 0) {
            b.append((char)bis.read());
        }
        bis.close();
        fis.close();
        JSONArray data = new JSONArray(b.toString());
        //for each object we will extract its date and then get the substring of the date as we did for the search date
        for (int i=0; i<data.length(); i++){
            String dates = data.getJSONObject(i).getString("date");
            int month = Integer.parseInt(dates.substring(0,2));
            int day = Integer.parseInt(dates.substring(3,5));
            int year = Integer.parseInt(dates.substring(6,10));
            //check to see if the date given is less than the date we have in json object
            if(yearSearchDate <= year){
                if(monthSearchDate <= month){
                    if (daySeachDate <= day) {
                        //if it is then we can add the json object to our events using our event object constructor
                        myEvents.add(new Event(data.getJSONObject(i).getString("location"),
                                data.getJSONObject(i).getString("date"), data.getJSONObject(i).getString("eventInfo"), i));
                    }
                }
            }
        }

    }

    //intent to the google maps fragment
    public void locationDisplayOnclick(View view) {
        Intent myIntent = new Intent(listActivity.this, MapsActivity.class /*from, to*/);
        startActivity(myIntent);
    }

    //method to check to see if a date is valid
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