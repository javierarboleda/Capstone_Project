package com.javierarboleda.supercomicreader.app.data;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by javierarboleda on 7/2/16.
 *
 */
public class CursorWithData<D> extends CursorWrapper {
    private final D mData;

    public CursorWithData(Cursor cursor, D data) {
        super(cursor);
        mData = data;
    }

    public D getData() {
        return mData;
    }
}
