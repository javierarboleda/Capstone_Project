package com.javierarboleda.supercomicreader.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.javierarboleda.supercomicreader.app.data.ComicContract.ComicEntry;
import com.javierarboleda.supercomicreader.app.data.ComicContract.CreationEntry;
import com.javierarboleda.supercomicreader.app.data.ComicContract.SavedPanelEntry;

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
    private static final String REAL_TYPE = " REAL";
    public static final String NOT_NULL = " NOT NULL";
    public static final String NOT_NULL_UNIQUE = " NOT NULL UNIQUE";
    private static final String COMMA_SEP = ",";

    public ComicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_COMIC_TABLE =
                "CREATE TABLE " + ComicEntry.TABLE_NAME + " (" +
                        ComicEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        ComicEntry.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL_UNIQUE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_FILE + TEXT_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_COVER + TEXT_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_PAGES + INTEGER_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_LAST_PAGE_READ + INTEGER_TYPE + COMMA_SEP +
                        ComicEntry.COLUMN_NAME_LAST_CREATION_READ + INTEGER_TYPE +
                " )";

        final String SQL_CREATE_CREATION_TABLE =
                "CREATE TABLE " + CreationEntry.TABLE_NAME + " (" +
                        CreationEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        CreationEntry.COLUMN_NAME_COMIC_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        CreationEntry.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL_UNIQUE + COMMA_SEP +
                        CreationEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                        CreationEntry.COLUMN_NAME_CREATION_DATE + INTEGER_TYPE + COMMA_SEP +
                        CreationEntry.COLUMN_NAME_LAST_PANEL_READ + INTEGER_TYPE + COMMA_SEP +

                        // Set up foreign key
                        " FOREIGN KEY (" + CreationEntry.COLUMN_NAME_COMIC_ID + ") REFERENCES " +
                        ComicEntry.TABLE_NAME + " (" + ComicEntry._ID + ")" +
                " )";

        final String SQL_CREATE_SAVED_PANEL_TABLE =
                "CREATE TABLE " + SavedPanelEntry.TABLE_NAME + " (" +
                        SavedPanelEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                        SavedPanelEntry.COLUMN_NAME_CREATION_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + NOT_NULL_UNIQUE + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_PAGE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_TOP_LEFT + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_TOP_RIGHT + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_BOTTOM_LEFT + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_BOTTOM_RIGHT + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_LEFT_PANE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_RIGHT_PANE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_TOP_PANE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        SavedPanelEntry.COLUMN_NAME_BOTTOM_PANE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +

                        // Set up foreign key
                        " FOREIGN KEY (" + SavedPanelEntry.COLUMN_NAME_CREATION_ID + ") REFERENCES " +
                        CreationEntry.TABLE_NAME + " (" + CreationEntry._ID + ")" +
                " )";

        db.execSQL(SQL_CREATE_COMIC_TABLE);
        db.execSQL(SQL_CREATE_CREATION_TABLE);
        db.execSQL(SQL_CREATE_SAVED_PANEL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
