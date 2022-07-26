package com.example.c323proj11aohernan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Please look at FoodItemListActivity for comments as they are all the same other than for some method
 * For example here will save information about the recipie if add to favorites button is clicked
 */
public class RecipeActivity extends AppCompatActivity {

    private static String urlJson = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    TextView name;
    TextView area;
    TextView category;
    TextView instructions;
    ImageView image;
    String nameText;
    String imageName;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent myIntent = getIntent();
        id = myIntent.getStringExtra("id");
        urlJson = urlJson + id;


        name = findViewById(R.id.name);
        area = findViewById(R.id.area);
        category = findViewById(R.id.category);
        instructions = findViewById(R.id.instructions);
        image = findViewById(R.id.imageView);

        RecipeActivity.GetData getData = new RecipeActivity.GetData();
        getData.execute();

    }

    public class GetData extends AsyncTask<String, String, String> {
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


        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("meals");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (id.equals(jsonObject1.getString("idMeal"))) {
                            nameText = jsonObject1.getString("strMeal");
                            name.setText(jsonObject1.getString("strMeal"));
                            category.setText(jsonObject1.getString("strCategory"));
                            area.setText(jsonObject1.getString("strArea"));
                            instructions.setText(jsonObject1.getString("strInstructions"));
                            imageName = (jsonObject1.getString("strMealThumb"));
                            Glide.with(RecipeActivity.this).load(imageName).into(image);
                        }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * If add to favorites is clicked we will open our db handler and pass our recipy name and image url to the database.
     * @param view
     */
    public void addToFavorites(View view) {
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Product product = new Product(nameText,nameText,imageName,imageName);
            dbHandler.addProductDB(product);

        }
}