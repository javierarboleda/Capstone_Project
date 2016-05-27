package com.javierarboleda.supercomicreader.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by javierarboleda on 5/24/16.
 */
public class TestUtilities extends AndroidTestCase{

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createComicValues() {

        ContentValues comicValues = new ContentValues();

        String comicTitle = "Star Gauntlet Pool 06";
        String comicFile = "/comics/Star Gauntlet Pool 06 (2009)";
        String comicPages = "32";
        String comicLastPageRead = "5";
        String comicLastCreationRead = "";

        comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_TITLE, comicTitle);
        comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_FILE, comicFile);
        comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_PAGES, comicPages);
        comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_LAST_PAGE_READ, comicLastPageRead);
        comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_LAST_CREATION_READ,
                comicLastCreationRead);

        return comicValues;
    }


    static long insertComicValues(Context context) {
        // insert our test records into the database
        ComicDbHelper dbHelper = new ComicDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createComicValues();

        long locationRowId;
        locationRowId = db.insert(ComicContract.ComicEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert text comic values", locationRowId != -1);

        return locationRowId;
    }



//    /*
//        Students: The functions we provide inside of TestProvider use this utility class to test
//        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
//        CTS tests.
//
//        Note that this only tests that the onChange function is called; it does not test that the
//        correct Uri is returned.
//     */
//    static class TestContentObserver extends ContentObserver {
//        final HandlerThread mHT;
//        boolean mContentChanged;
//
//        static TestContentObserver getTestContentObserver() {
//            HandlerThread ht = new HandlerThread("ContentObserverThread");
//            ht.start();
//            return new TestContentObserver(ht);
//        }
//
//        private TestContentObserver(HandlerThread ht) {
//            super(new Handler(ht.getLooper()));
//            mHT = ht;
//        }
//
//        // On earlier versions of Android, this onChange method is called
//        @Override
//        public void onChange(boolean selfChange) {
//            onChange(selfChange, null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange, Uri uri) {
//            mContentChanged = true;
//        }
//
//        public void waitForNotificationOrFail() {
//            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
//            // It's useful to look at the Android CTS source for ideas on how to test your Android
//            // applications.  The reason that PollingCheck works is that, by default, the JUnit
//            // testing framework is not running on the main Android application thread.
//            new PollingCheck(5000) {
//                @Override
//                protected boolean check() {
//                    return mContentChanged;
//                }
//            }.run();
//            mHT.quit();
//        }
//    }
//
//    static TestContentObserver getTestContentObserver() {
//        return TestContentObserver.getTestContentObserver();
//    }


}
