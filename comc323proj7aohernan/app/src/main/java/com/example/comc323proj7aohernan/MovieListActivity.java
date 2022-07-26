package com.example.comc323proj7aohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.onMovieClickListener {
    private static String urlJson = "https://api.themoviedb.org/3/movie/popular?api_key=c895b353102014d5ac4331a837b79a9a";

    List<MovieClass> movieList;
    RecyclerView recyclerView;

    boolean count = false;
    String name = "";
    String language = "";
    String releaseDate = "";
    String description = "";
    String genreId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);

        GetData getData = new GetData();
        getData.execute();
    }

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

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(count){
                        if(name.equals(jsonObject1.getString("title"))){
                            name = jsonObject1.getString("title");
                            language = jsonObject1.getString("original_language");
                            releaseDate = jsonObject1.getString("release_date");
                            description = jsonObject1.getString("overview");
                            genreId = jsonObject1.getString("genre_ids");
                            count = false;
                        }
                    }else {
                        MovieClass movie = new MovieClass();
                        movie.setName(jsonObject1.getString("title"));
                        movie.setImage(jsonObject1.getString("poster_path"));
                        movieList.add(movie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            putData(movieList);
        }
    }

    private void putData(List<MovieClass> movieList){
        MovieAdapter adapter = new MovieAdapter(this, movieList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
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
                Intent myIntent = new Intent(MovieListActivity.this, MainActivity.class /*from, to*/);
                startActivity(myIntent);
                return true;
            case R.id.movieActivty:
                Intent mIntent = new Intent(MovieListActivity.this, MovieListActivity.class /*from, to*/);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMovieClick(int position) {
        count = true;
        MovieClass move = movieList.get(position);
        name = move.getName();

        GetData getData = new GetData();
        getData.execute();

        Log.v("CLICK", name);
        Log.v("CLICK", language);
        Log.v("CLICK", releaseDate);
        Log.v("CLICK", description);
        Log.v("GENRE", genreId);

        Log.v("CLICK", move.getImage());



        Intent myIntent = new Intent(MovieListActivity.this, MovieItemActivity.class /*from, to*/);
        myIntent.putExtra("name",name);
        myIntent.putExtra("language",language);
        myIntent.putExtra("releaseDate",releaseDate);
        myIntent.putExtra("description",description);
        myIntent.putExtra("genre",genreId);
        myIntent.putExtra("image",move.getImage());
        startActivity(myIntent);
    }
}