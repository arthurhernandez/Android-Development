package com.example.comc323proj8aohernan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Boolean musicSet;
    TextView optVervificationTextView;
    TextView titleTextView;
    EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        passwordEditText = findViewById(R.id.enterOTPEditText);
        optVervificationTextView = findViewById(R.id.otpVerificationTextView);

        new OTP_Receiver().setPasswordEditText(passwordEditText);
        new OTP_Receiver().setOTPTextView(optVervificationTextView);
    }

    private void requestPermissions() {
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this,smspermission);

        if(grant != PackageManager.PERMISSION_GRANTED){
            String[] permissionList = new String[1];
            permissionList[0] = smspermission;

            ActivityCompat.requestPermissions(this,permissionList,1);
        }
    }

    public void playMusicOnclick(View view) {
        if(musicSet == null || musicSet == false) {
            startService(new Intent(this, MyService.class));
            musicSet = true;
        }else{
            stopService(new Intent(this, MyService.class));
            musicSet = false;
        }
    }

    public void verifyOnclick(View view) {
        if("1234".equals(passwordEditText.getText().toString())){
            Intent myIntent = new Intent(MainActivity.this, SecondActivity.class /*from, to*/);
            startActivity(myIntent);

        }else{
            titleTextView = findViewById(R.id.titileTextView);
            titleTextView.setText("INCORRECT CODE PLEASE TRY AGAIN");
        }
    }
}