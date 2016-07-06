package com.javierarboleda.supercomicreader.app.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.javierarboleda.supercomicreader.R;

/**
 * Created by javierarboleda on 7/5/16.
 */
public class WidgetProvider extends AppWidgetProvider {


    public static final String LOG_TAG = WidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate(Context context, AppWidgetManager appWidgetManager," +
                "int[] appWidgetIds)");

        // get a list of AppWidgetIds, unique identifiers for each widget user has created
        //  update all of these widgets

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_all_creations);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }

            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);


        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive(Context context, Intent intent)");
        super.onReceive(context, intent);
        if ("com.javierarboleda.supercomicreader.app.ACTION_DATA_UPDATED"
                .equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }


    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.d(LOG_TAG, "setRemoteAdapter(Context context, @NonNull final RemoteViews views)");
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, MyWidgetService.class));
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        Log.d(LOG_TAG, "setRemoteAdapterV11(Context context, @NonNull final RemoteViews views)");
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, MyWidgetService.class));
    }



}
