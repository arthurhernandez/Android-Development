package com.example.comc323finalprojectaohernan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ReviewList {

    private static final float PER_WIDTH = 250;
    private static final float PER_HEIGHT = 250;
    private String name;
    private String image;
    private String reviewID;

    public String getReviewListName() {
        return name;
    }

    public void setReviewListName(String _name) { this.name = _name; }

    public Bitmap getReviewListImage() { return stringToBitmap(this.image);}

    public String getReviewImageAsString(){ return this.image; }

    public void setReviewListImage(String _image) {
        this.image = _image;
    }

    public ReviewList(String _name, Bitmap _image, String reviewID) {
        this.name = _name;
        this.image = bitmapToString(resizeBitmap(_image));
        this.reviewID = reviewID;
    }

    public ReviewList() {

    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, boas);
        byte[] b = boas.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = PER_WIDTH / width;
        float scaleHeight = PER_HEIGHT / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap resizedBit = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,false);
        return resizedBit;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
}
