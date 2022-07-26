package com.example.comc323proj7aohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class MovieItemActivity extends AppCompatActivity {

    String name = "";
    String language = "";
    String releaseDate = "";
    String description = "";
    String image = "";
    String genre = "";

    TextView names;
    TextView languages;
    TextView releaseDates;
    TextView descriptions;
    TextView genres;
    ImageView images;

    private static String urlJson = "https://api.themoviedb.org/3/genre/movie/popular?api_key=c895b353102014d5ac4331a837b79a9a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);

        names = findViewById(R.id.movieItemTitle);
        languages = findViewById(R.id.movieItemLanguage);
        releaseDates = findViewById(R.id.movieItemReleaseDate);
        descriptions = findViewById(R.id.movieItemDescription);
        images = findViewById(R.id.MovieItemImageView);

        Intent myIntent = getIntent();
        genre = myIntent.getStringExtra("genre");
        name = myIntent.getStringExtra("name");
        language = myIntent.getStringExtra("language");
        releaseDate = myIntent.getStringExtra("releaseDate");
        description = myIntent.getStringExtra("description");
        image = myIntent.getStringExtra("image");

        names.setText("Movie name: " + name);
        languages.setText("Language: " +language);
        releaseDates.setText("Release Data: " +releaseDate);
        descriptions.setText("Description: " +description);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+image)
                .into(images);

        findGenre();

    }

    private void findGenre() {
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
        try {
            JSONObject jsonObject = new JSONObject(current);
            JSONArray jsonArray = jsonObject.getJSONArray("genres");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Log.v("GENRES",jsonObject1.getString("id") + "  " + genre);

                if(genre.trim().contains(jsonObject1.getString("id"))){
                    genres = findViewById(R.id.movieItemGenre);
                    genres.setText(genre);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.weatherActivty:
                Intent myIntent = new Intent(MovieItemActivity.this, MainActivity.class /*from, to*/);
                startActivity(myIntent);
                return true;
            case R.id.movieActivty:
                Intent mIntent = new Intent(MovieItemActivity.this, MovieListActivity.class /*from, to*/);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}