package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;

public class second_activity extends AppCompatActivity implements gestures_playgroundFragment.gesturesFragmentInterface, logLayoutFragment.logFragmentInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //create fragment transactions
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransactions = getSupportFragmentManager().beginTransaction();
        //create a gesture playground fragment connection
        gestures_playgroundFragment playgroundFragment = new gestures_playgroundFragment();
        logLayoutFragment logFragment = new logLayoutFragment();
        //place the gesture playground in the second activity

        fragmentTransaction.replace(R.id.playgroundholder, playgroundFragment);
        fragmentTransaction.commit();
        //create a log list fragment connection

        //place the log fragment in the second activity
        fragmentTransactions.replace(R.id.logholder, logFragment);
        fragmentTransactions.commit();

    }
    //check to see if the orientation changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}