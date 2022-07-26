package com.example.comc323finalprojectaohernan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Review {

    private static final float PER_WIDTH = 250;
    private static final float PER_HEIGHT = 250;
    private String name;
    private String description;
    private String detail;
    private String location;
    private String image;

    public Review(String _name, String description,String detail, String location, Bitmap _image) {
        this.name = _name;
        this.description = description;
        this.detail = detail;
        this.location = location;
        this.image = bitmapToString(resizeBitmap(_image));

    }

    public String getReviewLocation() { return location; }
    public void setReviewLocation(String location) { this.location = location; }
    public String getReviewDescription() { return description; }
    public void setReviewDescription(String description) { this.description = description; }
    public String getReviewDetail() { return detail; }
    public void setReviewDetail(String detail) { this.detail = detail; }
    public String getReviewName() { return name; }
    public void setReviewName(String _name) { this.name = _name; }
    public Bitmap getReviewImage() { return stringToBitmap(this.image);}
    public String getReviewImageAsString(){ return this.image; }
    public void setReviewImage(String _image) { this.image = _image; }

    public Review() {

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
}
