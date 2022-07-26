package com.example.comc323finalprojectaohernan;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class User {
    private static final float PER_WIDTH = 250;
    private static final float PER_HEIGHT = 250;
    private String userName;
    private String userEmail;
    private String userImage;

    public User() {

    }

    public User(String userName, String userEmail, Bitmap userImage) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = bitmapToString(resizeBitmap(userImage));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Bitmap getUserImage() {
        return stringToBitmap(this.userImage);
    }

    public String getImagesAsString(){
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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
