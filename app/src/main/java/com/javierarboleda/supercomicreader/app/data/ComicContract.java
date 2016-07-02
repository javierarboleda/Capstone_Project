package com.javierarboleda.supercomicreader.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by javierarboleda on 5/23/16.
 *
 * Defines table and column names for the comic database.
 *
 */
public class ComicContract {

    public static final String CONTENT_AUTHORITY = "com.javierarboleda.supercomicreader";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    public static final String F_SLASH = "/";

    public static final String PATH_COMIC  = "comic_entry";
    public static final String PATH_CREATION  = "creation";
    public static final String PATH_SAVED_PANEL  = "saved_panel";
    public static final String PATH_MY_CREATION  = "my_creation";
    public static final String PATH_IMAGE  = "image";
    public static final String PATH_FAVORITE  = "favorite";

    /**
     *
     *  Entry for the comic table
     *
     */
    public static final class ComicEntry implements BaseColumns {

        public static final String TABLE_NAME = "comic";

        public static final String COLUMN_NAME_TITLE  = "title";
        public static final String COLUMN_NAME_FILE = "file";
        public static final String COLUMN_NAME_COVER = "cover";
        public static final String COLUMN_NAME_PAGES  = "pages";
        public static final String COLUMN_NAME_LAST_PAGE_READ  = "last_page_read";
        public static final String COLUMN_NAME_LAST_CREATION_READ  = "last_creation_read";

        public static final int INDEX_ID = 0;
        public static final int INDEX_TITLE = 1;
        public static final int INDEX_FILE = 2;
        public static final int INDEX_COVER = 3;
        public static final int INDEX_PAGES = 4;
        public static final int INDEX_LAST_PAGE_READ = 5;
        public static final int INDEX_LAST_CREATION_READ = 6;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_COMIC).build();

        public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                F_SLASH + CONTENT_AUTHORITY + F_SLASH + PATH_COMIC;

        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                F_SLASH + CONTENT_AUTHORITY + F_SLASH + PATH_COMIC;

        // this is for building uri on insertion
        public static Uri buildComicDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMIC).build();
        }

        // this is for building uri on insertion
        public static Uri buildComicUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CreationEntry implements BaseColumns {

        public static final String TABLE_NAME = "creation";
        public static final String COLUMN_NAME_COMIC_ID  = "comic_id";
        public static final String COLUMN_NAME_TITLE  = "title";
        public static final String COLUMN_NAME_AUTHOR  = "author";
        public static final String COLUMN_NAME_CREATION_DATE  = "creation_date";
        public static final String COLUMN_NAME_LAST_PANEL_READ  = "last_panel_read";

        public static final int INDEX_ID = 0;
        public static final int INDEX_COMIC_ID = 1;
        public static final int INDEX_TITLE = 2;
        public static final int INDEX_AUTHOR = 3;
        public static final int INDEX_CREATION_DATE = 4;
        public static final int INDEX_LAST_PANEL_READ = 5;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CREATION).build();

        public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                F_SLASH + CONTENT_AUTHORITY + F_SLASH + PATH_CREATION;

        // this is for building uri on insertion
        public static Uri buildCreationDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CREATION).build();
        }

        // this is for building uri on insertion
        public static Uri buildCreationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SavedPanelEntry implements BaseColumns {

        public static final String TABLE_NAME = "saved_panel";
        public static final String COLUMN_NAME_CREATION_ID  = "creation_id";
        public static final String COLUMN_NAME_NUMBER  = "number";
        public static final String COLUMN_NAME_PAGE  = "page";
        public static final String COLUMN_NAME_TOP_LEFT_X  = "top_left_x";
        public static final String COLUMN_NAME_TOP_LEFT_Y  = "top_left_y";
        public static final String COLUMN_NAME_TOP_RIGHT_X  = "top_right_x";
        public static final String COLUMN_NAME_TOP_RIGHT_Y  = "top_right_y";
        public static final String COLUMN_NAME_BOTTOM_LEFT_X  = "bottom_left_x";
        public static final String COLUMN_NAME_BOTTOM_LEFT_Y  = "bottom_left_y";
        public static final String COLUMN_NAME_BOTTOM_RIGHT_X  = "bottom_right_x";
        public static final String COLUMN_NAME_BOTTOM_RIGHT_Y  = "bottom_right_y";
        public static final String COLUMN_NAME_LEFT_PANE  = "left_pane";
        public static final String COLUMN_NAME_RIGHT_PANE  = "right_pane";
        public static final String COLUMN_NAME_TOP_PANE  = "top_pane";
        public static final String COLUMN_NAME_BOTTOM_PANE  = "bottom_pane";
        public static final String COLUMN_NAME_SCALE = "scale";

        public static final int INDEX_ID = 0;
        public static final int INDEX_CREATION_ID  = 1;
        public static final int INDEX_NUMBER  = 2;
        public static final int INDEX_PAGE  = 3;
        public static final int INDEX_TOP_LEFT_X  = 4;
        public static final int INDEX_TOP_LEFT_Y  = 5;
        public static final int INDEX_TOP_RIGHT_X  = 6;
        public static final int INDEX_TOP_RIGHT_Y = 7;
        public static final int INDEX_BOTTOM_LEFT_X  = 8;
        public static final int INDEX_BOTTOM_LEFT_Y  = 9;
        public static final int INDEX_BOTTOM_RIGHT_X  = 10;
        public static final int INDEX_BOTTOM_RIGHT_Y  = 11;
        public static final int INDEX_LEFT_PANE  = 12;
        public static final int INDEX_RIGHT_PANE  = 13;
        public static final int INDEX_TOP_PANE  = 14;
        public static final int INDEX_BOTTOM_PANE  = 15;
        public static final int INDEX_SCALE  = 16;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_SAVED_PANEL).build();

        public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                F_SLASH + CONTENT_AUTHORITY + F_SLASH + PATH_SAVED_PANEL;

        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                F_SLASH + CONTENT_AUTHORITY + F_SLASH + PATH_SAVED_PANEL;

        // this is for building uri on insertion
        public static Uri buildSavedPanelDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_SAVED_PANEL).build();
        }

        // this is for building uri on insertion
        public static Uri buildSavedPanelUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MyCreationEntry implements BaseColumns {

        public static final String TABLE_NAME = "my_creation";
        public static final String COLUMN_NAME_  = "";
    }

    public static final class ImageEntry implements BaseColumns {

        public static final String TABLE_NAME = "image";
        public static final String COLUMN_NAME_  = "";
    }

    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_  = "";
    }








}
