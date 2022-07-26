package com.example.comc323finalprojectaohernan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CAMERA_REQUEST = 1888;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView name = header.findViewById(R.id.navName);
        TextView email = header.findViewById(R.id.navEmail);
        ImageView imageView = header.findViewById(R.id.accountIcon);
        User user = dbHandler.findUserDB();
        name.setText(user.getUserName());
        email.setText(user.getUserEmail());
        imageView.setImageBitmap(user.getUserImage());

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        //sets the listener and syncs the state to the icon
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new DashBoardFragment()).commit();
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
                        new DashBoardFragment()).commit();
                break;
            //opens music fragment
            case R.id.videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new FavoritesFragment()).commit();
                break;
            case R.id.trash:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new TrashFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}