package com.example.comc323proj9aohernan;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ProductsDB.db";
    private static final String TABLE_PRODUCTS = "myProducts";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_PRICE = "_price";
    private static final String COLUMN_CATEGORY = "_Category";
    private static final String COLUMN_DATE = "_date";

    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE =
                "CREATE TABLE " + TABLE_PRODUCTS +
                "(" + COLUMN_NAME + " STRING PRIMARY KEY," +
                COLUMN_PRICE + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_DATE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }

    public void addProductDB(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.get_productName());
        values.put(COLUMN_PRICE, product.get_productPrice());
        values.put(COLUMN_CATEGORY, product.get_productCategory());
        values.put(COLUMN_DATE, product.get_productDate());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();

    }

    public Product findProductDB(String productName) {

        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_NAME + " =  \"" + productName + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            product.set_productName(cursor.getString(0));
            product.set_productPrice(cursor.getString(1));
            product.set_productCategory(cursor.getString(2));
            product.set_productDate(cursor.getString(3));
            cursor.close();

        } else {

            product= null;

        }
        db.close();
        return product;
    }

    public Boolean deleteProductDB(String productName) {

        boolean result = false;
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_NAME + " =  \"" + productName + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if(cursor.moveToFirst()){

            product.set_productName(cursor.getString(0));
            db.delete(TABLE_PRODUCTS, COLUMN_NAME + " = ?",new String[] { String.valueOf (product.get_productName())});
            cursor.close();
            result = true;

        }
        db.close();
        return result;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS, null);
        return res;
    }
}
