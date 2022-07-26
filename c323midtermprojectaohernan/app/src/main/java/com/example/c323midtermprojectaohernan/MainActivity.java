package com.example.c323midtermprojectaohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Arthur Hernandez
 * Midterm Project
 * 10/14/2021
 */
public class MainActivity extends AppCompatActivity {
    /**
     * this activity will detect the 3 layouts and whether
     * we will be adding an item to one, or viewing a list of items of that type.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * When a button is pressed we will decided which intent extra to attach before moving to the add entry activity
     * @param view
     */
    public void addItemButtonOnClick(View view) {
        String entryToAdd = "";
        switch(view.getId())
        {
            case R.id.addCitiesButton:
                entryToAdd = "CITY";
                break;

            case R.id.addMonumentsItem:
                entryToAdd = "MONUMENT";
                break;

            case R.id.addCampingItem:
                entryToAdd = "CAMPING";
                break;
        }
        Intent myIntent = new Intent(MainActivity.this, addEntry.class /*from, to*/);
        myIntent.putExtra("NEWENTRY",entryToAdd);
        startActivity(myIntent);
    }

    /**
     * this button gives an explict intent to the favorite views saved
     * @param view
     */
    public void favoritesButtonOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, favoritesActivity.class /*from, to*/);
        startActivity(myIntent);
    }
    /**
     * The 3 button onlcicks below all have an explicity intent to
     * the same activity but pass different intent extra strings depending on the view pressed
     * @param view
     */
    public void goToEntryListActivityCampingButtonOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, entryListActivity.class /*from, to*/);
        myIntent.putExtra("ACTIVITY","CAMPING");
        startActivity(myIntent);

    }

    public void goToEntryListActivityHistoricalButtonOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, entryListActivity.class /*from, to*/);
        myIntent.putExtra("ACTIVITY","MONUMENT");
        startActivity(myIntent);
    }

    public void goToEntryListActivityCitiesButtonOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, entryListActivity.class /*from, to*/);
        myIntent.putExtra("ACTIVITY","CITY");
        startActivity(myIntent);
    }
}