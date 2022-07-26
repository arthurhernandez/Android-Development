package com.example.comc323finalprojectaohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {
    private List<ReviewList> myReviews = new ArrayList<>();
    ReviewListActivity.MyCustomListAdapter reviewsAdapter;
    public static final int GET_FROM_GALLERY = 3;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int swipe = 200;
    private static final int swipeVelocity = 100;
    ListView listView;
    String category;
    String categoryID;
    ImageView imageView;
    Bitmap newImage;
    GestureDetector gestureDetector;
    String globalReviewID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Intent myIntent = getIntent();
        category = myIntent.getStringExtra("category");
        TextView title = findViewById(R.id.reviewListTitle);
        title.setText(category);

        FloatingActionButton addButton = findViewById(R.id.addReview);
        addButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //do something
                        alertDialogueClicked();
                    }
                });

        populateMyReviews();

        listView = findViewById(R.id.reviewListListView);
        reviewsAdapter = new MyCustomListAdapter(this, myReviews);
        listView.setAdapter(reviewsAdapter);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            //PLEASE NOTE I FOUND MOST OF THIS IMPLEMENTATION ONLINE
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                try {
                    //subtract x and y start and end points
                    float diffY = motionEvent1.getY() - motionEvent.getY();
                    float diffX = motionEvent1.getX() - motionEvent.getX();
                    //if the absolute value of x is more than y we are moving horizontally
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        //if the value is going upward we are moving to the right
                        if (Math.abs(diffX) > swipe && Math.abs(v) > swipeVelocity) {
                            if (diffX > 0) {
                                return true;
                            }
                        }
                    }
                    //catch anything that doesnt fit our bounds
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return false;
            }
        });

    }

    private void populateMyReviews() {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Cursor res = dbHandler.getAllCategoryData();
        while(res.moveToNext()){
            if((res.getString(1).replaceAll("\\s", "")).equals(category.replaceAll("\\s", ""))){
                categoryID = res.getString(0).replaceAll("\\s", "");
            }
        }

        myReviews = new ArrayList<ReviewList>();

        //TODO change this to review not review list
        // add a button for adding image
        //we have our category id to check with the review category id
        // if they check out add it to the list
        Cursor ress = dbHandler.getAllReviewData();
        while(ress.moveToNext()){
            if(ress.getString(2).equals(categoryID)){

                ReviewList review = new ReviewList();
                review.setReviewListName(ress.getString(3));
                review.setReviewListImage(ress.getString(7));
                review.setReviewID(ress.getString(0));
                myReviews.add(review);
            }
        }
    }

    private class MyCustomListAdapter extends ArrayAdapter<ReviewList> {
        private Context context;
        private List<ReviewList> products = null;
        /**
         *
         * @param context of the item frame
         * @param products that will be populated into the frames
         */
        public MyCustomListAdapter(Context context,  List<ReviewList> products) {
            super(context, 0, products);
            this.products = products;
            this.context = context;
        }
        /**
         * @param position current position in the list
         * @param convertView converts the current view into whatever we set from our list
         * @param parent holds the parent ViewGroup (ListView )
         * @return
         */
        @NonNull
        @Override
        //get our custom list view and inflate it
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.reviewlistcustomlayout, parent, false);
            //reference our views in the custom layout to the list view
            ReviewList currentContact = myReviews.get(position);

            TextView textName = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.reviewListImage);
            textName.setText(currentContact.getReviewListName());
            Bitmap imageName = currentContact.getReviewListImage();
            Glide.with(context).load(imageName).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(ReviewListActivity.this, ReviewActivity.class /*from, to*/);
                    myIntent.putExtra("reviewID",currentContact.getReviewID());
                    startActivity(myIntent);
                    Log.v("reviewId" ,currentContact.getReviewID() );

                }
            });

            ImageButton editPicture = itemView.findViewById(R.id.reviewListImageButton);
            editPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraAlert(currentContact.getReviewID());
                    //TODO: update review image STILL
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(gestureDetector.onTouchEvent(event)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Are you sure you want to move this review to trash?");
                        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);
                                String IDofFavorite = currentContact.getReviewID();

                                Review  rev = new Review();
                                Cursor res = dbHandler.getAllReviewData();
                                while(res.moveToNext()){
                                    if((res.getString(0)).equals(IDofFavorite)){
                                        rev.setReviewName(res.getString(3));
                                        rev.setReviewDescription(res.getString(4));
                                        rev.setReviewDetail(res.getString(5));
                                        rev.setReviewLocation(res.getString(6));
                                        rev.setReviewImage(res.getString(7));
                                        break;
                                    }
                                }
                                dbHandler.addTrashToDB(categoryID,IDofFavorite,rev);
                                dbHandler.deleteReviewItemDB(IDofFavorite);
                                myReviews.remove(position);
                                notifyDataSetChanged();
                            }
                        });

                        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }});
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    return true;
                }
            });

            return itemView;
        }
    }

    private void alertDialogueClicked() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Review");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.reviewlistcustomalertdialog, null);
        builder.setView(customLayout);

        EditText title = customLayout.findViewById(R.id.reviewTitleEditText);
        EditText description = customLayout.findViewById(R.id.reviewDiscriptionEditText);
        EditText detail = customLayout.findViewById(R.id.reviewDetailsEditText);
        EditText location = customLayout.findViewById(R.id.reviewLocationEditText);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity

                if(title.getText().toString().length() != 0 && description.getText().toString().length() != 0 && detail.getText().toString().length() != 0 && location.getText().toString().length() != 0 ){

                    MyDBHandler dbHandler = new MyDBHandler(ReviewListActivity.this, null, null, 1);
                    Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ball);
                    Review newReview = new Review(title.getText().toString(),description.getText().toString(),detail.getText().toString(),location.getText().toString(),icon);
                    dbHandler.addReviewToDB(categoryID,newReview);
                    ReviewList review = new ReviewList(title.getText().toString(), icon,null);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_SHORT).show();
                }else{
                }
            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cameraAlert(String reviewID) {
        globalReviewID = reviewID;
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Image");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.customimagetakingdialog, null);
        builder.setView(customLayout);

        Button gallery = customLayout.findViewById(R.id.galleryButton);
        Button camera = customLayout.findViewById(R.id.cameraButton);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GET_FROM_GALLERY);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                Review review = new Review("1","1","1","1",newImage);
                dbHandler.updateImage(review.getReviewImageAsString(),reviewID);
                finish();
                startActivity(getIntent());
            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) { }});
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                newImage = BitmapFactory.decodeStream(imageStream);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            newImage = imageBitmap;
        }
    }
}