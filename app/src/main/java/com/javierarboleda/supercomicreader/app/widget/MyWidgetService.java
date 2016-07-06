package com.javierarboleda.supercomicreader.app.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.data.ComicContract;

/**
 * Created by javierarboleda on 7/5/16.
 */
public class MyWidgetService extends RemoteViewsService {

    public static final String LOG_TAG = MyWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {
            private Cursor mDataCursor = null;

            @Override
            public void onCreate() {
                Log.d(LOG_TAG, "onCreate()");
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                Log.d(LOG_TAG, "onDataSetChanged()");

                if (mDataCursor != null) {
                    mDataCursor.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
//                Uri scoresUri = DatabaseContract.scores_table.buildScoreWithDate();
                Uri creationDirUri = ComicContract.CreationEntry.buildCreationDirUri();
                mDataCursor = getContentResolver().query(creationDirUri,
                        null,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public RemoteViews getViewAt(int position) {

                Log.d(LOG_TAG, "getViewAt(int position): position=" + position);

                if (position == AdapterView.INVALID_POSITION ||
                        mDataCursor == null || !mDataCursor.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_creation_list_item);

                String title = mDataCursor.getString(ComicContract.CreationEntry.INDEX_TITLE);

                views.setTextViewText(R.id.title_text_view, title);

                return views;
            }

            @Override
            public void onDestroy() {
                Log.d(LOG_TAG, "onDestroy()");
                if (mDataCursor != null) {
                    mDataCursor.close();
                    mDataCursor = null;
                }
            }

            @Override
            public int getCount() {
                Log.d(LOG_TAG, "getCount(): count = " + mDataCursor.getCount());
                // totally have to update this method correctly
                return mDataCursor == null ? 0 : mDataCursor.getCount();
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.d(LOG_TAG, "getLoadingView()");
                return new RemoteViews(getPackageName(), R.layout.widget_creation_list_item);
            }

            @Override
            public int getViewTypeCount() {
                Log.d(LOG_TAG, "getViewTypeCount()");
                return 1;
            }

            @Override
            public long getItemId(int position) {
                Log.d(LOG_TAG, "getItemId(int position)");
                return position;
            }

            @Override
            public boolean hasStableIds() {
                Log.d(LOG_TAG, "hasStableIds()");
                return true;
            }
        };
    }
}
