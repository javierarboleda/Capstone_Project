package com.javierarboleda.supercomicreader.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by javierarboleda on 5/24/16.
 *
 */
public class TestComicDb extends AndroidTestCase {


    public static final String LOG_TAG = TestComicDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(ComicDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*

     */
    public void testCreateDb() throws Throwable {

        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();

        tableNameHashSet.add(ComicContract.ComicEntry.TABLE_NAME);

        mContext.deleteDatabase(ComicDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new ComicDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that the database doesn't contain all of the tables
        assertTrue("Error: Your database was created without all of the tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + ComicContract.ComicEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table " +
                "information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> comicColumnHashSet = new HashSet<String>();
        comicColumnHashSet.add(ComicContract.ComicEntry._ID);
        comicColumnHashSet.add(ComicContract.ComicEntry.COLUMN_NAME_TITLE);
        comicColumnHashSet.add(ComicContract.ComicEntry.COLUMN_NAME_FILE);
        comicColumnHashSet.add(ComicContract.ComicEntry.COLUMN_NAME_PAGES);
        comicColumnHashSet.add(ComicContract.ComicEntry.COLUMN_NAME_LAST_PAGE_READ);
        comicColumnHashSet.add(ComicContract.ComicEntry.COLUMN_NAME_LAST_CREATION_READ);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            comicColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required comic
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required comic entry columns.",
                comicColumnHashSet.isEmpty());
        db.close();
    }

    /*

    */
    public void testComicTable() {
        insertComic();
    }

    public long insertComic() {

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        ComicDbHelper dbHelper = new ComicDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = TestUtilities.createComicValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(ComicContract.ComicEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                ComicContract.ComicEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from comic query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return locationRowId;
    }









}
