package com.javierarboleda.supercomicreader.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.javierarboleda.supercomicreader.app.data.ComicContract.ComicEntry;

/**
 * Created by javierarboleda on 5/23/16.
 *
 *
 */
public class ComicDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "super_comic_reader.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    public ComicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_COMIC_TABLE =
                "CREATE TABLE " + ComicEntry.TABLE_NAME + " (" +
                        ComicEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        ComicEntry.COLUMN_NAME_TITLE + TEXT_TYPE + "NOT NULL UNIQUE" + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_FILE + TEXT_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_COVER + TEXT_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_PAGES + INTEGER_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_LAST_PAGE_READ + INTEGER_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_LAST_CREATION_READ + INTEGER_TYPE +
                " )";

        db.execSQL(SQL_CREATE_COMIC_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
