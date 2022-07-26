package com.example.c323midtermprojectaohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * used to create and print out the list of saved favorite entries
 * usses JSON file "favorites"
 * uses favorites xml as custom format
 * does not have any buttons or onclick methods
 */
public class favoritesActivity extends AppCompatActivity {

    private List<Entry> myEntries;
    ListView listView;

    /**
     * Tries to populate a custom list view when created using MyCustomListAdapter
     * @param savedInstanceState is the state it will create when opened
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        try {
            populateMyEntries();
            Log.v("LISTACTIVITY", "we Populated");
        } catch (IOException e) {
            Log.v("LISTACTIVITY", "we got stuck @ IOException");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.v("LISTACTIVITY", "we got stuck @ JSONException ");
            e.printStackTrace();
        }
        populateList();
    }

    /**
     *
     * @throws IOException when accessing files incorrectly
     * @throws JSONException when accessing JSON files incorrectly
     */
    private void populateMyEntries() throws IOException, JSONException {
        //opens json file
        myEntries = new ArrayList<Entry>();
        FileInputStream fis = openFileInput("favorites");
        BufferedInputStream bis = new BufferedInputStream(fis);
        //reads it char by char
        StringBuffer b = new StringBuffer();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        fis.close();
        //get the json data array
        JSONArray data = new JSONArray(b.toString());
        //for the length of the amount of objects create a new event
        for (int i = 0; i < data.length(); i++) {
            myEntries.add(new Entry(
                    data.getJSONObject(i).getString("location")
                    ,data.getJSONObject(i).getString("name")
                    ,data.getJSONObject(i).getString("bestTimeToVisit")
                    ,data.getJSONObject(i).getString("priceOrMetropolitanOrTourist")
                    ,data.getJSONObject(i).getString("historyOfMonument")
                    ,1));
        }
    }

    private void populateList() {
        //create a listview adapter
        ArrayAdapter<Entry> myAdapter = new MyCustomListAdapter();
        listView = findViewById(R.id.favoritesListPlaceHolders);
        listView.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Entry> {
        //create a customlist adapter that finds each value in our custom layout
        public MyCustomListAdapter() {
            super(favoritesActivity.this, R.layout.favoriteslayout, myEntries);
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.favoriteslayout, parent, false);
            //find all views in custom layout and do something with them
            Entry currentEntry = myEntries.get(position);

            TextView textViewName = itemView.findViewById(R.id.name);
            TextView textViewLocation = itemView.findViewById(R.id.location);
            TextView textViewBestTimeToVisit = itemView.findViewById(R.id.besttimetovisit);
            TextView textViewpriceOrMetropolitanOrTourist = itemView.findViewById(R.id.history);
            TextView textViewHistoryOfMonument = itemView.findViewById(R.id.other);

            textViewName.setText(currentEntry.getName());
            textViewLocation.setText(currentEntry.getLocation());
            textViewBestTimeToVisit.setText(currentEntry.getBestTimeToVisit());
            textViewpriceOrMetropolitanOrTourist.setText(currentEntry.getpriceOrMetropolitanOrTourist());
            textViewHistoryOfMonument.setText(currentEntry.gethistoryOfMonument());

            return itemView;

        }
    }
}