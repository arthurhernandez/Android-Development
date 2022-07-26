package com.example.mybmicalculator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



    }

    public void loginbutton2(View view) {
        SharedPreferences myprefs = getSharedPreferences("SharedPrefApp", MODE_PRIVATE);
        EditText currentuser = findViewById(R.id.userfield);
        EditText currentpass = findViewById(R.id.passwordfield2);
        String curruse = (currentuser.getText().toString());
        String currpass = (currentpass.getText().toString());
        String name = myprefs.getString("NAME",null);
        String password = myprefs.getString("PASS",null);

        if(curruse.equals(name) && currpass.equals(password)) {
            Intent myIntent = new Intent(SecondActivity.this, welcome.class /*from, to*/);
            startActivity(myIntent);
        }else{
            TextView text = findViewById(R.id.textView9);
            text.setText("Your password or username are wrong, please try again" + name + password + currpass + curruse);
        }
    }
}