package com.example.comc323finalprojectaohernan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;
    ImageView accountIcon;
    EditText editName;
    EditText editEmail;
    TextView textView;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        textView = findViewById(R.id.titleTextView);
        accountIcon = findViewById(R.id.accountIcon);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        if (dbHandler.isUserEmpty()) {
            //oncreate if we have no user in the database...
            textView.setText("Welcome, please create an account!");
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ball);
            accountIcon.setImageBitmap(icon);

            accountIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), GET_FROM_GALLERY);
                }
            });
        } else {
            textView.setText("Welcome back, please enter your credentials!");
            User user = dbHandler.findUserDB();
            accountIcon.setImageBitmap(user.getUserImage());
        }
    }

    public void signInsignUpOnclick(View view) {
        //start db handler
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //if our user  table is empty is empty we will add sum to em
        if (dbHandler.isUserEmpty()) {
            //if we're empty
            if(editName.getText().toString().length() != 0 && editEmail.getText().toString().length() != 0) {
                //get the image
                Bitmap image = ((BitmapDrawable)accountIcon.getDrawable()).getBitmap();
                User newUser = new User(editName.getText().toString(), editEmail.getText().toString(), image);
                dbHandler.addUserToDB(newUser);
                finish();
                Intent myIntent = new Intent(MainActivity.this, NavDrawer.class /*from, to*/);
                startActivity(myIntent);

            }else{
                //if empty but no input
                textView.setText("Please enter a name and an email");
            }

        } else {
            //if full compare with the first user we find (should only have one in the first place)
            User user = dbHandler.findUserDB();
            if (editName.getText().toString().replaceAll("\\s", "").equals(user.getUserName().replaceAll("\\s", "")) && editEmail.getText().toString().equals(user.getUserEmail().replaceAll("\\s", ""))) {
                Intent myIntent = new Intent(MainActivity.this, NavDrawer.class /*from, to*/);
                startActivity(myIntent);
            } else {
                //didnt work to check everthing is the same
                textView.setText("Your credentials are incorrect, please try again");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            try {
                selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                accountIcon.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }

    }
}