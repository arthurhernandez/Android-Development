package com.example.comc323finalprojectaohernan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Category {

    private static final float PER_WIDTH = 250;
    private static final float PER_HEIGHT = 250;
    private String _name;
    private String _image;
    private int categoryID;

    public int getCategoryid(){
        return categoryID;
    }

    public String get_category_name() {
        return _name;
    }

    public void set_category_name(String _name) { this._name = _name; }

    public Bitmap get_category_image() { return stringToBitmap(this._image);}

    public String getCategoryImageAsString(){ return this._image; }

    public void setCategoryImage(String _image) {
        this._image = _image;
    }

    public void set_category_image(String _image) {
        this._image = _image;
    }

    public Category(String _name, Bitmap _image) {
        this._name = _name;
        this._image = bitmapToString(resizeBitmap(_image));
    }

    public Category() {

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
