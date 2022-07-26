package com.example.c323proj11aohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

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
 * This class is for our food items under categories activity.
 * Here we create a custom recycler view that gets the categories and lists them with a picture in the recycler view.
 */
public class FoodItemListActivity extends AppCompatActivity implements CategoryAdapter.onMovieClickListener{
    private static String urlJson = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";
    List<CategoryClass> categoryList;
    RecyclerView recyclerView;
    String name = "";
    boolean count = false;
    String id = "";
    private RewardedAd mRewardedAd;

    /**
     * Here we will instantiate and create a rewards ad that can be used after we click on an item in the recy view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_list);
        Intent myIntent = getIntent();
        urlJson = urlJson + myIntent.getStringExtra("name");

        categoryList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);
        FoodItemListActivity.GetData getData = new FoodItemListActivity.GetData();
        getData.execute();

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    /**
                     * @param loadAdError will return null if reward is not loaded
                     */
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });

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
                JSONArray jsonArray = jsonObject.getJSONArray("meals");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(count){
                        if(name.equals(jsonObject1.getString("strMeal"))){
                            name = jsonObject1.getString("strMeal");
                            count = false;
                        }
                    }else {
                        CategoryClass category = new CategoryClass();
                        category.setName(jsonObject1.getString("strMeal"));
                        category.setImage(jsonObject1.getString("strMealThumb"));
                        category.setId(jsonObject1.getString("idMeal"));
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
        CategoryAdapter adapter = new CategoryAdapter(this, movieList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    /**
     * If a recycle view item is clicked we will play our reward ad
     * and once it is over we can go to the next activity
     * @param position
     */
    @Override
    public void onMovieClick(int position) {

        CategoryClass move = categoryList.get(position);
        id = move.getId();

        if (mRewardedAd != null) {
            Activity activityContext = FoodItemListActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Intent myIntent = new Intent(FoodItemListActivity.this, RecipeActivity.class /*from, to*/);
                    myIntent.putExtra("id",id);
                    startActivity(myIntent);
                }

            });
        }

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {


            @Override
            public void onAdShowedFullScreenContent() {

            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
            }


            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                //do nothing
                mRewardedAd = null;
            }
        });

    }

}