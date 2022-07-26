package com.example.comc323proj10aohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    /**
     * On create creates the toolbar and navigation drawer
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //sets the navigation view
        navigationView.setNavigationItemSelectedListener(this);
        //toggles the navigation drawer open or closed
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
                        new PlayListFragment()).commit();
                break;
                //opens music fragment
            case R.id.music:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new MusicFragment()).commit();
                break;
                 //opens video fragment
            case R.id.videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new VideoFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}