package com.example.dogproject.data;

import android.provider.BaseColumns;

public final class FavouriteDogs {
    private FavouriteDogs(){}

    public static final class FavouriteImages implements BaseColumns{
        public final static String TABLE_NAME = "favourite";

        public final static String COLUMN_NAME = "breed";
        public final static String COLUMN_IMAGES = "images";
    }
}
