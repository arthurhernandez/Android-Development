package com.example.c323proj11aohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

/**
 * Arthur Hernandez
 * aohernan
 * Tues Dec 7th
 * Project 11
 * This main activity mainly holds our Toolbar and its navigation drawer.
 * Please keep in mind that when running, wait a couple seconds before ads so that they can load
 * HttpRequest request = HttpRequest.newBuilder()
 * 		.uri(URI.create("https://themealdb.p.rapidapi.com/filter.php?a=Canadian"))
 * 		.header("x-rapidapi-host", "themealdb.p.rapidapi.com")
 * 		.header("x-rapidapi-key", "c1f9fb92d9mshc4ec8089437d432p1de0bbjsn75cf6fc0f686")
 * 		.method("GET", HttpRequest.BodyPublishers.noBody())
 * 		.build();
 * HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
 * System.out.println(response.body());
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    /**
     *
     * @param savedInstanceState
     * instantiations fo toolbar and navigation views
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        //sets the listener and syncs the state to the icon
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * checks whether or not the drawer is open and helps sync when to open/ close
     */
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    /**
     * Checks what item is selected and opens its fragment
     * @param item view for each item
     * @return true whenever an item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //opens playlist fragment
            case R.id.playlist:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new CategoryFragment()).commit();
                break;
            //opens music fragment
            case R.id.videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new FavortiesFagment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}