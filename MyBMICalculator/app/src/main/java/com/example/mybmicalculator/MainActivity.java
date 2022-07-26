package com.example.mybmicalculator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void convertButtonCallBack(View view) {
        //after clicking signup button converts, checks, and saves info.
        EditText name = findViewById(R.id.namefield);
        String names = (name.getText().toString());
        EditText username = findViewById(R.id.usernamefield);
        String usernames = (username.getText().toString());
        EditText password= findViewById(R.id.passwordfield);
        String passwords = (password.getText().toString());
        EditText confirmpassword = findViewById(R.id.confimpasswordfield);
        String confimpasswords = (confirmpassword.getText().toString());
        //confirm password
            if (passwords.equals(confimpasswords)) {
                //save to preferences
                SharedPreferences myprefs = getSharedPreferences("SharedPrefApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putString("USER", usernames);
                editor.putString("NAME", names);
                editor.putString("PASS", passwords);
                editor.apply();
                editor.commit();
                //go to 2nd activity
                Intent myIntent = new Intent(MainActivity.this, welcome.class /*from, to*/);
                startActivity(myIntent);
            } else {
                TextView output = findViewById(R.id.changingfield);
                output.setText("Your passwords do not match, try again");

            }



    }

    public void loginbutton(View view) {
        Intent myIntent = new Intent(MainActivity.this, SecondActivity.class /*from, to*/);
        startActivity(myIntent);
    }
}