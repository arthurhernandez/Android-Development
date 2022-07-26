package com.example.comc323finalprojectaohernan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ReviewActivity extends AppCompatActivity {
    Review review = new Review();
    boolean inReview = false;
    ImageView reviewItemImage;
    TextView reviewItemTitle;
    TextView reviewItemDescription;
    TextView reviewItemDetail;
    TextView reviewItemLocation;
    ImageButton favoritesButton;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent myIntent = getIntent();
        title = myIntent.getStringExtra("reviewID");
        reviewItemTitle = findViewById(R.id.reviewItemTitle);
        reviewItemDescription = findViewById(R.id.reviewItemDescription);
        reviewItemDetail = findViewById(R.id.reviewItemDetail);
        reviewItemLocation = findViewById(R.id.reviewItemLocation);
        favoritesButton = findViewById(R.id.favoritesButton);
        reviewItemImage = findViewById(R.id.reviewItemImage);

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Cursor res = dbHandler.getAllReviewData();
        while(res.moveToNext()){
            if((res.getString(0)).equals(title)){
                inReview = true;

                review.setReviewName(res.getString(3));
                review.setReviewDescription(res.getString(4));
                review.setReviewDetail(res.getString(5));
                review.setReviewLocation(res.getString(6));
                review.setReviewImage(res.getString(7));
                break;
            }
        }

        if(!inReview){
            Cursor ress = dbHandler.getAllTrashData();
            while(ress.moveToNext()){
                if((ress.getString(0)).equals(title)){
                    review.setReviewName(res.getString(4));
                    review.setReviewDescription(res.getString(5));
                    review.setReviewDetail(res.getString(6));
                    review.setReviewLocation(res.getString(7));
                    review.setReviewImage(res.getString(8));
                    break;
                }
            }
        }

        reviewItemTitle.setText(review.getReviewName());
        reviewItemDescription.setText(review.getReviewDescription());
        reviewItemDetail.setText(review.getReviewDetail());
        reviewItemLocation.setText(review.getReviewLocation());
        Bitmap image = review.getReviewImage();
        Glide.with(this).load(image).into(reviewItemImage);
        //TODO image set
        if(!inReview){
            favoritesButton.setClickable(false);
        }
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDBHandler dbHandler = new MyDBHandler(ReviewActivity.this, null, null, 1);
                dbHandler.addFavoritesToDB(review,title);
            }
        });
    }

    public void viewLocationOnClick(View view) {
        Intent myIntent = new Intent(ReviewActivity.this, MapsActivity.class /*from, to*/);
        myIntent.putExtra("location", reviewItemLocation.getText().toString());
        startActivity(myIntent);
    }
}