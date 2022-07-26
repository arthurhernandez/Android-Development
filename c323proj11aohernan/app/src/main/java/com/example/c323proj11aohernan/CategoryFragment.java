package com.example.c323proj11aohernan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This java class is part of our category fragment held inside of the navigation drawer.
 * Here we create a custom recycler view that gets the categories and lists them with a picture in the recycler view.
 */
public class CategoryFragment extends Fragment implements CategoryAdapter.onMovieClickListener {
    private static String urlJson = "https://www.themealdb.com/api/json/v1/1/categories.php";
    List<CategoryClass> categoryList;
    RecyclerView recyclerView;
    String name = "";
    AdView adView;
    boolean count = false;
    InterstitialAd interstitialAd;

    /**
     *
     * @param inflater inflates the fragment into the container
     * @param container the container that is going to contain the fragment
     * @param savedInstanceState the saved state of the fragment
     * @return the completed view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //populates our music objects into mysongs list
        //inflates the view
        View view = inflater.inflate(R.layout.categoryfragment, container, false);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        // creating the banner add at the bottom of the fragment
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        categoryList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleView);
        GetData getData = new GetData();
        getData.execute();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            /**
             * must check when the ad is ready to be opened and it will open automatically for us
             * @param initializationStatus
             */
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        AdRequest addRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", addRequest, new InterstitialAdLoadCallback() {
            /**
             * Here were opening our ad
             * @param InterstitialAd
             */
            @Override
            public void onAdLoaded(@NonNull InterstitialAd InterstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                interstitialAd = InterstitialAd;
            }

            /**
             * Here we are saying that our add is null if it failed to load
             * @param loadAdError
             */
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                interstitialAd = null;
            }

        });
        return view;
    }

    /**
     * Get Data gets our JSON formated data from the url we requested to create a JSON string object from it
     */
    public class GetData extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(urlJson);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream input = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(input);
                    int data = inputStreamReader.read();
                    while (data != -1) {
                        current += (char) data;
                        data = inputStreamReader.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        /**
         * once we have our string we create the object s
         * We also create and store category objects from it into an array list for the recycle view
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(count){
                        if(name.equals(jsonObject1.getString("strCategory"))){
                            name = jsonObject1.getString("strCategory");
                            count = false;
                        }
                    }else {
                        CategoryClass category = new CategoryClass();
                        category.setName(jsonObject1.getString("strCategory"));
                        category.setImage("https://www.themealdb.com//images//category//"+ jsonObject1.getString("strCategory") + ".png");
                        categoryList.add(category);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            putData(categoryList);
        }
    }

    /**
     * Putting all our objects into the recycle view using our adaper
     * @param movieList
     */
    private void putData(List<CategoryClass> movieList){
        CategoryAdapter adapter = new CategoryAdapter(getContext(), movieList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);
    }

    /**
     * If a recycle view item is clicked we will play our instantial ad
     * and once it is dimissed we can go to the next activity
     * @param position
     */
    @Override
    public void onMovieClick(int position) {

        CategoryClass move = categoryList.get(position);
        name = move.getName();
        if (interstitialAd != null) {
            interstitialAd.show(getActivity());
        }

        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Intent myIntent = new Intent(getActivity(), FoodItemListActivity.class /*from, to*/);
                myIntent.putExtra("name",name);
                startActivity(myIntent);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.

            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                interstitialAd = null;

            }
        });
    }

}
