package com.example.dogproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "favourite.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + FavouriteDogs.FavouriteImages.TABLE_NAME + " ("
                + FavouriteDogs.FavouriteImages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavouriteDogs.FavouriteImages.COLUMN_NAME + " TEXT NOT NULL, "
                + FavouriteDogs.FavouriteImages.COLUMN_IMAGES + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
