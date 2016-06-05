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
    public static final String PATH_SAVED_PANEL  = "saved_panel";
    public static final String PATH_CREATION  = "creation";
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
        public static final String COLUMN_NAME_PAGES  = "pages";
        public static final String COLUMN_NAME_LAST_PAGE_READ  = "last_page_read";
        public static final String COLUMN_NAME_LAST_CREATION_READ  = "last_creation_read";

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
        public static final String COLUMN_NAME_  = "";
    }

    public static final class SavedPanelEntry implements BaseColumns {

        public static final String TABLE_NAME = "saved_panel";
        public static final String COLUMN_NAME_  = "";
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
