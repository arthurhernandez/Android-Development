package com.example.project3;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //create shared preferences and their string arrays to log
    //create list
    private List<Reminder> myReminders = new ArrayList<Reminder>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populatemyReminders();
    }

    private void populatemyReminders() {
        //create a list view adapter
        ListView listView = findViewById(R.id.listview);
        ArrayAdapter<Reminder> myAdapter = new MyCustomListAdapter();
        listView.setAdapter(myAdapter);
        //create some example reminders
        myReminders.add(new Reminder("example","example",R.mipmap.high));
        myReminders.add(new Reminder("example","example",R.mipmap.low));
        //get the intent extras that we passed from the last activity
        Intent myIntent = getIntent();
        //set them to some strings
        String intentDate = myIntent.getStringExtra("NEWTITLE");
        String intentReminder = myIntent.getStringExtra("NEWDATE");
        String intentPriority = myIntent.getStringExtra("NEWICON");
        //make sure we actually came from the other intent
        if(intentDate != null){
            //if the priority is 1 then use low priority icon
            if(Integer.parseInt(intentPriority) == 1) {
                myReminders.add(new Reminder(intentReminder, intentDate, R.mipmap.low));
                addToSharedPrefs(intentReminder,intentDate,intentPriority);
            }else{
                //else use the high priority icon
                myReminders.add(new Reminder(intentReminder, intentDate, R.mipmap.high));
                addToSharedPrefs(intentReminder,intentDate,intentPriority);
            }
        }
    }
//I used this helper method to keep the code clean and add everything to shared prefrences
    private void addToSharedPrefs(String intentReminder, String intentDate, String intentPriority) {
        //get calendar and the current date and time
        Calendar myCal = Calendar.getInstance();
        Date now = myCal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String dateStr = sdf.format(now);
        //put our values into the shared preferences
        SharedPreferences myprefs = getSharedPreferences("SharedPrefApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = myprefs.edit();
        editor.putString("TITLE", intentReminder);
        editor.putString("DATE", intentDate);
        editor.putString("PRIORITY", intentPriority);
        editor.putString("TIME", dateStr);
        editor.commit();
    }


    //custom list adapter
    private class MyCustomListAdapter extends ArrayAdapter<Reminder> {
        public MyCustomListAdapter() {
            super(MainActivity.this, R.layout.item_layout , myReminders);
        }
        @NonNull
        @Override
        //get our custom list view and inflate it
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_layout, parent, false);
            //reference our views in the custom layout to the list view
            Reminder currentContact = myReminders.get(position);
            TextView textViewTitle = itemView.findViewById(R.id.textviewTitle);
            TextView textViewDate = itemView.findViewById(R.id.textViewDate);
            ImageView imageView = itemView.findViewById(R.id.priorityIcon);
            textViewTitle.setText(currentContact.getTitle());
            textViewDate.setText(currentContact.getDate());
            imageView.setImageResource(currentContact.getPriority());
            return itemView;
        }
    }
    //if clicked explicit intent to the add reminder activity
    public void addonclick(View view) {
        Intent myIntent = new Intent(MainActivity.this, addReminder.class /*from, to*/);
        startActivity(myIntent);
    }
    //method onclick for deleting a list view
    public void deleteonclick(View view) {
        ListView listView = findViewById(R.id.listview);
        ArrayAdapter<Reminder> myAdapter = new MyCustomListAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //if the view is clicked
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //remove that reminder
                myReminders.remove(i);
                //update the adapter
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}

