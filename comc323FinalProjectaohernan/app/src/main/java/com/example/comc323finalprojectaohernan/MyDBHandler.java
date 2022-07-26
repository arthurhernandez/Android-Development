package com.example.comc323finalprojectaohernan;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;
/**
 • User Table:o UserIdo UserEmailo UserNameo Image
 • Category/ Dashboard Table:o CategoryIdo CategoryNameo Image
 • ReviewList Table:o ReviewListIdo CategoryIdo ReviewName (eg. If Category is Restaurant then, you will store Restaurant’s name here).o Image
 • Review Table:o ReviewIdo ReviewListIdo CategoryIdo ReviewDetailso Image
 • Favorites Table:o FavoriteIdo ReviewIdo ReviewListIdo ReviewNameo ReviewDetails o Image
 • Trash Table:o TrashIdo FavoriteId o ReviewId o ReviewListId o ReviewName o ReviewDetails o Image
 **/
public class MyDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "TABLE_USER";
    private static final String COLUMN_USER_ID  = "userId";
    private static final String COLUMN_USER_EMAIL  = "userEmail";
    private static final String COLUMN_USER_NAME  = "userName";
    private static final String COLUMN_IMAGE  = "userImage";

    private static final String TABLE_CATEGORY = "TABLE_CATEGORY";
    private static final String COLUMN_CATEGORY_ID  = "categoryId";
    private static final String COLUMN_CATEGORY_NAME  = "categoryName";

    private static final String TABLE_REVIEW_LIST = "TABLE_REVIEW_LIST";
    private static final String COLUMN_REVIEW_LIST_ID  = "reviewListId";
    private static final String COLUMN_REVIEW_NAME  = "reviewName";

    private static final String TABLE_REVIEW = "TABLE_REVIEW";
    private static final String COLUMN_REVIEW_ID  = "reviewId";
    private static final String COLUMN_REVIEW_LOCATION  = "reviewLocation";
    private static final String COLUMN_REVIEW_DESCRIPTION = "reviewDescription";
    private static final String COLUMN_REVIEW_DETAILS  = "reviewDetails";

    private static final String TABLE_FAVORITES = "TABLE_FAVORITES";
    private static final String COLUMN_FAVORITE_ID  = "favoriteId";

    private static final String TABLE_TRASH = "TABLE_TRASH";
    private static final String COLUMN_TRASH_ID  = "trashId";

    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String USER_TABLE =
                "CREATE TABLE " + TABLE_USER +
                " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT, " +
                        COLUMN_USER_EMAIL+ " TEXT, " +
                COLUMN_IMAGE + " TEXT " + ")";
        sqLiteDatabase.execSQL(USER_TABLE);

        String CATEGORY_TABLE =
                "CREATE TABLE " + TABLE_CATEGORY +
                        " (" + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_CATEGORY_NAME + " TEXT, " +
                        COLUMN_IMAGE + " TEXT " + ")";
        sqLiteDatabase.execSQL(CATEGORY_TABLE);

        String REVIEW_LIST_TABLE =
                "CREATE TABLE " + TABLE_REVIEW_LIST +
                        " (" + COLUMN_REVIEW_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_CATEGORY_ID + " TEXT, " +
                        COLUMN_REVIEW_NAME + " TEXT, " +
                        COLUMN_IMAGE + " TEXT " + ")";
        sqLiteDatabase.execSQL(REVIEW_LIST_TABLE);

        String REVIEW_TABLE =
                "CREATE TABLE " + TABLE_REVIEW+
                        " (" + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + //0
                        COLUMN_REVIEW_LIST_ID+ " TEXT, " + //1
                        COLUMN_CATEGORY_ID + " TEXT, " +  //2
                        COLUMN_REVIEW_NAME+ " TEXT, " +  //3
                        COLUMN_REVIEW_DESCRIPTION+ " TEXT, " + //4
                        COLUMN_REVIEW_DETAILS + " TEXT, " +  //5
                        COLUMN_REVIEW_LOCATION+ " TEXT, " + //6
                        COLUMN_IMAGE + " TEXT " + ")"; //7
        sqLiteDatabase.execSQL(REVIEW_TABLE);

        String FAVORITES_TABLE =
                "CREATE TABLE " + TABLE_FAVORITES+
                        " (" + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_REVIEW_ID+ " TEXT, " +
                        COLUMN_REVIEW_LIST_ID+ " TEXT, " +
                        COLUMN_REVIEW_NAME+ " TEXT, " +
                        COLUMN_REVIEW_DETAILS + " TEXT, " +
                        COLUMN_IMAGE + " TEXT " + ")";
        sqLiteDatabase.execSQL(FAVORITES_TABLE);

        String TRASH_TABLE =
                "CREATE TABLE " + TABLE_TRASH+
                        " (" + COLUMN_TRASH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + //0
                        COLUMN_REVIEW_LIST_ID+ " TEXT, " + //1
                        COLUMN_REVIEW_ID+ " TEXT, " + //2
                        COLUMN_CATEGORY_ID + " TEXT, " +  //3
                        COLUMN_REVIEW_NAME+ " TEXT, " +  //4
                        COLUMN_REVIEW_DESCRIPTION+ " TEXT, " + //5
                        COLUMN_REVIEW_DETAILS + " TEXT, " +  //6
                        COLUMN_REVIEW_LOCATION+ " TEXT, " + //7
                        COLUMN_IMAGE + " TEXT " + ")"; //8
        sqLiteDatabase.execSQL(TRASH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_REVIEW_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_REVIEW);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_FAVORITES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRASH);
        onCreate(sqLiteDatabase);
    }

    public void addUserToDB(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_IMAGE, user.getImagesAsString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close(); }

    public void addCategoryToDB(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.get_category_name());
        values.put(COLUMN_IMAGE, category.getCategoryImageAsString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORY, null, values);
        db.close(); }

    public void addReviewToDB(String categoryID, Review review) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_LIST_ID, 1);
        values.put(COLUMN_CATEGORY_ID, categoryID);
        values.put(COLUMN_REVIEW_NAME, review.getReviewName());
        values.put(COLUMN_REVIEW_DESCRIPTION, review.getReviewDescription());
        values.put(COLUMN_REVIEW_DETAILS, review.getReviewDetail());
        values.put(COLUMN_REVIEW_LOCATION, review.getReviewLocation());
        values.put(COLUMN_IMAGE, review.getReviewImageAsString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_REVIEW, null, values);
        db.close(); }

    public void addFavoritesToDB(Review review, String reviewID) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_ID, reviewID);
        values.put(COLUMN_REVIEW_LIST_ID, 1);
        values.put(COLUMN_REVIEW_NAME, review.getReviewName());
        values.put(COLUMN_REVIEW_DETAILS, review.getReviewLocation());
        values.put(COLUMN_IMAGE, review.getReviewImageAsString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_FAVORITES, null, values);
        db.close(); }

    public void addTrashToDB(String categoryID,String reviewID, Review review) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_LIST_ID, 1);
        values.put(COLUMN_REVIEW_ID, reviewID);
        values.put(COLUMN_CATEGORY_ID, categoryID);
        values.put(COLUMN_REVIEW_NAME, review.getReviewName());
        values.put(COLUMN_REVIEW_DESCRIPTION, review.getReviewDescription());
        values.put(COLUMN_REVIEW_DETAILS, review.getReviewDetail());
        values.put(COLUMN_REVIEW_LOCATION, review.getReviewLocation());
        values.put(COLUMN_IMAGE, review.getReviewImageAsString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TRASH, null, values);
        db.close(); }

    public boolean isUserEmpty(){
        Boolean res = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM TABLE_USER", null);
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getInt (0) == 0) {
                res = true;
            }else{
                res = false;
            }
        }
        return res;
    }

    public boolean isCategoriesEmpty(){
        Boolean res = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM TABLE_CATEGORY", null);
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getInt (0) == 0) {
                res = true;
            }else{
                res = false;
            }
        }
        return res;
    }

    public boolean isReviewListEmpty(){
        Boolean res = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM TABLE_REVIEW_LIST", null);
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getInt (0) == 0) {
                res = true;
            }else{
                res = false;
            }
        }
        return res;
    }

    public Cursor getAllReviewData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_REVIEW, null);
        return res;
    }
    public Cursor getAllFavoritesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_FAVORITES, null);
        return res;
    }

    public Cursor getAllTrashData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_TRASH, null);
        return res;
    }

    public Boolean deleteFavoritesItemDB(String favoriteId) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_FAVORITE_ID + " =  \"" + favoriteId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String favoriteCursor;
        if(cursor.moveToFirst()){
            favoriteCursor = cursor.getString(0);
            db.delete(TABLE_FAVORITES, COLUMN_FAVORITE_ID + " = ?",new String[] { favoriteCursor});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public Boolean deleteFromTrashDB(String favoriteId) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_TRASH + " WHERE " + COLUMN_TRASH_ID + " =  \"" + favoriteId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String favoriteCursor;
        if(cursor.moveToFirst()){
            favoriteCursor = cursor.getString(0);
            db.delete(TABLE_TRASH, COLUMN_TRASH_ID + " = ?",new String[] { favoriteCursor});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public Boolean deleteReviewItemDB(String reviewId) {
        boolean indata = false;
        Cursor res = getAllFavoritesData();
        while(res.moveToNext()){
            if((res.getString(1)).equals(reviewId)){
                indata = true;
            }
        }
        if(indata){
            boolean result = false;
            String query = "Select * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_REVIEW_ID + " =  \"" + reviewId + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            String favoriteCursor;
            if(cursor.moveToFirst()){
                favoriteCursor = cursor.getString(1);
                db.delete(TABLE_FAVORITES, COLUMN_REVIEW_ID + " = ?",new String[] { favoriteCursor});
                cursor.close();
                result = true;
            }
            db.close();
            return result;
        }

        boolean result = false;
        String query = "Select * FROM " + TABLE_REVIEW + " WHERE " + COLUMN_REVIEW_ID + " =  \"" + reviewId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String reviewCursor;
        if(cursor.moveToFirst()){
            reviewCursor = cursor.getString(0);
            db.delete(TABLE_REVIEW, COLUMN_REVIEW_ID + " = ?",new String[] {reviewCursor});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public User findUserDB() {
        String query = "Select * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User product = new User();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            product.setUserName(cursor.getString(1));
            product.setUserEmail(cursor.getString(2));
            product.setUserImage(cursor.getString(3));
            cursor.close();
        } else {
            product= null;
        }
        db.close();
        return product;
    }

    public Cursor getAllCategoryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_CATEGORY, null);
        return res;
    }

    public void updateImage(String newImage, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_REVIEW + " SET " + COLUMN_IMAGE +
                " = '" + newImage + "' WHERE " + COLUMN_REVIEW_ID + " = '"
                + id + "'";

        db.execSQL(query);
    }
}