package com.example.settingsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


//dont forget to implement
public class MainActivity extends AppCompatActivity implements settingsFagment.myFragmentInterface, headerFragment.headerFragmentInterface{
    int counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create fragment transaction for the settings
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransactions = getSupportFragmentManager().beginTransaction();
        //create settings fragment
        settingsFagment settingsFragment = new settingsFagment();
        fragmentTransaction.replace(R.id.settingsList, settingsFragment);
        fragmentTransaction.commit();
        //create header fragment
        headerFragment headerFragment = new headerFragment();
        fragmentTransactions.replace(R.id.headerlay, headerFragment);
        fragmentTransactions.commit();
    }

    @Override
    public void headerChanged() {
    }

    @Override
    public void settingsChanged(int count) {
        TextView settingText = findViewById(R.id.settingText);
        TextView one = findViewById(R.id.one);
        TextView two = findViewById(R.id.two);
        TextView three = findViewById(R.id.three);
        TextView four = findViewById(R.id.four);
        TextView five = findViewById(R.id.five);
        //sound output
        if(count == 1){
            settingText.setText("Sound");
            one.setText(">");
            two.setText("");
            three.setText("");
            four.setText("");
            five.setText("");
            counter = 1; }
        //battery output
        if(count == 2){
            settingText.setText("Battery");
            one.setText("");
            two.setText(">");
            three.setText("");
            four.setText("");
            five.setText("");
            counter = 2; }
        //storage output
        if(count == 3){
            settingText.setText("Storage");
            one.setText("");
            two.setText("");
            three.setText(">");
            four.setText("");
            five.setText("");
            counter = 3; }
        //display output
        if(count == 4){
            settingText.setText("Display");
            one.setText("");
            two.setText("");
            three.setText("");
            four.setText(">");
            five.setText("");
            counter = 4; }
        //system output
        if(count == 5){
            settingText.setText("System");
            one.setText("");
            two.setText("");
            three.setText("");
            four.setText("");
            five.setText(">");
            counter = 5; }
    }

    public void nextButtonOnclick(View view) {
        if(counter == 5){
            counter = 1;
            settingsChanged(counter);
        }else{
            counter ++;
            settingsChanged(counter);
        }
    }

}