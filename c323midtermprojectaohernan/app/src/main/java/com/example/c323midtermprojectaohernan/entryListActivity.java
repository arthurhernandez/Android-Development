package com.example.c323midtermprojectaohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 *takes in @intentExtra from Main Activity to filter through JSON list
 * HOME for both custom and card lists
 * fragment @headerFrag used for the top, two button fragment
 */
public class entryListActivity extends AppCompatActivity implements headerFragment.myHeaderFragmentInterface {
    //instantiate lists and JSON specific Arrays
    private List<Entry> myEntries;
    ListView listView;
    String listDefine;
    String intentExtra;
    JSONArray favoritesArray = new JSONArray();
    JSONObject favorites;

    /**
     * Creates the header fragment and saves the intentExtra entry type for later
     * @param savedInstanceState is set when creating the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        Intent myIntent = getIntent();
        intentExtra = myIntent.getStringExtra("ACTIVITY");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        headerFragment headerFrag = new headerFragment();
        fragmentTransaction.replace(R.id.headerPlaceHolder, headerFrag);
        fragmentTransaction.commit();
    }

    /**
     * From the header interface
     * @param buttonEvent is returned from fragment that defines whether the list will be card or custom list
     */
    @Override
    public void HeaderChange(String buttonEvent) {
        listDefine = buttonEvent;
        if(listDefine.equals("CARD")) {
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
        }else if (listDefine.equals("LIST")){
            try {
                populateMyList();
                Log.v("LISTACTIVITY", "we Populated");
            } catch (IOException e) {
                Log.v("LISTACTIVITY", "we got stuck @ IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.v("LISTACTIVITY", "we got stuck @ JSONException ");
                e.printStackTrace();
            }
            populateLists();
        }
    }

    /**
     * External method for adding to favorites list. takes in:
     * @param name of the entry
     * @param location of the entry
     * @param bestTimeToVisit of the entry
     * @param getpriceOrMetropolitanOrTourist of the entry depending on its type
     * @param gethistoryOfMonument of the entry depending on if it is a monument (empty if not)
     * @throws JSONException for accessing JSON files incorrectly
     * @throws IOException for reading files incorrectly
     */
    private void addToFavoritesList(String name, String location, String bestTimeToVisit, String getpriceOrMetropolitanOrTourist, String gethistoryOfMonument) throws JSONException, IOException  {
        if(!isExternalStorageAvailable())
            return;
        //new event with location, date, and event info (add the lat and long for the map)
        favorites = new JSONObject();

        favorites.put("name", name);
        favorites.put( "location",location);
        favorites.put( "bestTimeToVisit", bestTimeToVisit);
        favorites.put( "priceOrMetropolitanOrTourist",  getpriceOrMetropolitanOrTourist);
        favorites.put( "historyOfMonument", gethistoryOfMonument);
        Log.v("FAVORITESLIST",name + location + bestTimeToVisit + getpriceOrMetropolitanOrTourist + gethistoryOfMonument);
        favoritesArray.put(favorites);
        String dataInText = favoritesArray.toString();
        FileOutputStream fos = openFileOutput("favorites", MODE_PRIVATE);
        fos.write(dataInText.getBytes());
        fos.close();
    }

    /**
     * Used for the custom card view to fill @myEntries, the custom entry object
     * @throws IOException when reading from files incorrectly
     * @throws JSONException when reading the JSON files incorrectly
     */
    private void populateMyEntries() throws IOException, JSONException {
        //opens json file
        myEntries = new ArrayList<Entry>();
        FileInputStream fis = openFileInput("entries");
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
            if(data.getJSONObject(i).getString("type").equals(intentExtra)) {
                myEntries.add(new Entry(
                        data.getJSONObject(i).getString("location")
                        , data.getJSONObject(i).getString("name")
                        , data.getJSONObject(i).getString("bestTimeToVisit")
                        , data.getJSONObject(i).getString("priceOrMetropolitanOrTourist")
                        , data.getJSONObject(i).getString("historyOfMonument")
                        , 1));
            }
        }
    }

    /**
     * Used by custom cardView list to create a new adapter @myAdapter
     * gets the list place-holder inside listView and sets up the adapter to it
     */
    private void populateList() {
        //create a listview adapter
        ArrayAdapter<Entry> myAdapter = new MyCustomListAdapter();
        listView = findViewById(R.id.listPlaceHolder);
        listView.setAdapter(myAdapter);
    }

    /**
     * Custom List adapter links all components in card view to their corresponding views
     * sets views to corresponding myEntries object elements
     * sets onlclick listeners for buttons and listview items
     */
    private class MyCustomListAdapter extends ArrayAdapter<Entry> {
        //create a customlist adapter that finds each value in our custom layout
        public MyCustomListAdapter() {
            super(entryListActivity.this, R.layout.cardlayout, myEntries);
        }
        /**
         *
         * @param position is the position in the element list
         * @param convertView the view being converted
         * @param parent the parent view
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.cardlayout, parent, false);
            //find all views in custom layout and do something with them
            Entry currentEntry = myEntries.get(position);
            TextView textViewName = itemView.findViewById(R.id.nameCardTextView);
            TextView textViewLocation = itemView.findViewById(R.id.LocationCardTextView);
            TextView textViewBestTimeToVisit = itemView.findViewById(R.id.bestTimeToVisitCardTextView);
            TextView textViewpriceOrMetropolitanOrTourist = itemView.findViewById(R.id.otherCardTextView);
            TextView textViewHistoryOfMonument = itemView.findViewById(R.id.monumentHistoryCardTextView);

            Button favoritesButton = itemView.findViewById(R.id.addToFavoritesCardButton);
            Button mapDisplayButton = itemView.findViewById(R.id.mapDisplayCardLayout);

            textViewName.setText(currentEntry.getName());
            textViewLocation.setText(currentEntry.getLocation());
            textViewBestTimeToVisit.setText(currentEntry.getBestTimeToVisit());
            textViewpriceOrMetropolitanOrTourist.setText(currentEntry.getpriceOrMetropolitanOrTourist());
            textViewHistoryOfMonument.setText(currentEntry.gethistoryOfMonument());
            //onlcick listener to add items to favorites list
            favoritesButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    try {
                        addToFavoritesList(currentEntry.getName(),currentEntry.getLocation(), currentEntry.getBestTimeToVisit(),currentEntry.getpriceOrMetropolitanOrTourist(), currentEntry.gethistoryOfMonument());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mapDisplayButton.setOnClickListener(new View.OnClickListener(){
                /**
                 * goes to map activity through intent if button is pressed
                 * @param v view
                 * passes @currentEntry.getLocation() to activity
                 */
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(entryListActivity.this, MapsActivity.class /*from, to*/);
                    myIntent.putExtra("LOCATION",currentEntry.getLocation());
                    startActivity(myIntent);
                }
            });
            //deletes item in listview if pressed
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                        myEntries.remove(position); //or some other task
                        notifyDataSetChanged();
                }
            });

            return itemView;

        }
    }

    /**
     * Used by custom list view instead of card view
     * @throws IOException
     * @throws JSONException
     */
    private void populateMyList() throws IOException, JSONException {
        //opens json file
        myEntries = new ArrayList<Entry>();
        FileInputStream fis = openFileInput("entries");
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
            if(data.getJSONObject(i).getString("type").equals(intentExtra)) {
                myEntries.add(new Entry(
                        data.getJSONObject(i).getString("location")
                        , data.getJSONObject(i).getString("name")
                        , data.getJSONObject(i).getString("bestTimeToVisit")
                        , data.getJSONObject(i).getString("priceOrMetropolitanOrTourist")
                        , data.getJSONObject(i).getString("historyOfMonument")
                        , 1));
            }
        }
    }

    /**
     * Used by custom list view instead of card view
     */
    private void populateLists() {
        //create a listview adapter
        ArrayAdapter<Entry> myAdapter = new MyCustomListAdapters();
        listView = findViewById(R.id.listPlaceHolder);
        listView.setAdapter(myAdapter);
    }

    private class MyCustomListAdapters extends ArrayAdapter<Entry> {
        //create a customlist adapter that finds each value in our custom layout
        public MyCustomListAdapters() {
            super(entryListActivity.this, R.layout.listlayoutforlist, myEntries);
        }

        /**
         *
         * @param position is the position in the element list
         * @param convertView the view being converted
         * @param parent the parent view
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.listlayoutforlist, parent, false);
            //find all views in custom layout and do something with them
            Entry currentEntry = myEntries.get(position);
            TextView textViewName = itemView.findViewById(R.id.textView7);
            TextView textViewLocation = itemView.findViewById(R.id.textView8);
            TextView textViewBestTimeToVisit = itemView.findViewById(R.id.textView9);
            TextView textViewpriceOrMetropolitanOrTourist = itemView.findViewById(R.id.textView10);
            TextView textViewHistoryOfMonument = itemView.findViewById(R.id.textView11);

            Button favoritesButton = itemView.findViewById(R.id.button2);
            Button mapDisplayButton = itemView.findViewById(R.id.button3);

            textViewName.setText(currentEntry.getName());
            textViewLocation.setText(currentEntry.getLocation());
            textViewBestTimeToVisit.setText(currentEntry.getBestTimeToVisit());
            textViewpriceOrMetropolitanOrTourist.setText(currentEntry.getpriceOrMetropolitanOrTourist());
            textViewHistoryOfMonument.setText(currentEntry.gethistoryOfMonument());

            favoritesButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    try {
                        addToFavoritesList(currentEntry.getName(),currentEntry.getLocation(), currentEntry.getBestTimeToVisit(),currentEntry.getpriceOrMetropolitanOrTourist(), currentEntry.gethistoryOfMonument());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            mapDisplayButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(entryListActivity.this, MapsActivity.class /*from, to*/);
                    myIntent.putExtra("LOCATION",currentEntry.getLocation());
                    startActivity(myIntent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    myEntries.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });

            return itemView;

        }
    }

    /**
     * Checks to see if there is external storage available
     * @return true if it is available
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


}