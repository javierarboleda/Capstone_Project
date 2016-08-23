package com.javierarboleda.supercomicreader.app.ui;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.data.ComicContract;
import com.javierarboleda.supercomicreader.app.model.Comic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllComicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int URL_LOADER = 0;
    private static final int REQUEST_FILE_ACCESS = 200;
    private SimpleStringRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    final String[] PERMS = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (hasFileAccessPermissions()) {
            getLoaderManager().initLoader(URL_LOADER, null, this);
        } else {
            if (isMPlus()) {
                requestReadExternalStoragePermission();
            }
        }



        mRecyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_all_comics, container, false);
        //setupRecyclerView(mRecyclerView);

        return mRecyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView, Cursor cursor) {

        File files =
                new File(Environment.getExternalStorageDirectory().getPath() + "/comics/covers");

        ArrayList<String> comicCovers = new ArrayList<>();

        if (files.exists()) {
            for (File f : files.listFiles()) {
                comicCovers.add(f.toString());
            }
        }

        mAdapter = new SimpleStringRecyclerViewAdapter(getActivity(), cursor);

        recyclerView.addItemDecoration(new MarginDecoration(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        ComicContract.ComicEntry.buildComicDirUri(),
                        null,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        setupRecyclerView(mRecyclerView, data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean hasFileAccessPermissions() {

        int readExternalStorage = ContextCompat.checkSelfPermission(getContext(),
                PERMS[0]);
        int writeExternalStorage = ContextCompat.checkSelfPermission(getContext(),
                PERMS[1]);

        if (readExternalStorage == PackageManager.PERMISSION_GRANTED
                && writeExternalStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void requestReadExternalStoragePermission() {


        requestPermissions(PERMS, REQUEST_FILE_ACCESS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FILE_ACCESS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLoaderManager().initLoader(URL_LOADER, null, this);

                    mRecyclerView.invalidate();
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private List<String> getRandomSublist(ArrayList<String> array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array.get(random.nextInt(array.size())));
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private Context mContext;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        //private List<String> mValues;
        private Cursor mCursor;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.comic_image_view);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }

//        public String getValueAt(int position) {
//            return mValues.get(position);
//        }

        public SimpleStringRecyclerViewAdapter(Context context, Cursor cursor) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            //mValues = items;
            mCursor = cursor;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_all_comics, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.mBoundString = mValues.get(position);
//            holder.mTextView.setText(mValues.get(position));

            mCursor.moveToPosition(position);

            final Comic comic = new Comic(
                    mCursor.getInt(ComicContract.ComicEntry.INDEX_ID),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_TITLE),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_FILE),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_COVER),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_PAGES),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_LAST_PAGE_READ),
                    mCursor.getString(ComicContract.ComicEntry.INDEX_LAST_CREATION_READ)
            );

            //String imagePath = mValues.get(position);
            String imagePath = Environment.getExternalStorageDirectory().getPath() +
                    comic.getFile() + comic.getCover();

            File imageFile = new File(imagePath);

            Glide.with(holder.mImageView.getContext())
                    .load(imageFile)
                    .centerCrop()
                    .into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = createDetailComicDetailsActivityIntent(comic);
                    mContext.startActivity(intent);
                }
            });
        }

        /**
         * Helper method for creating a ComicDetailsActivity intent
         */
        private Intent createDetailComicDetailsActivityIntent(Comic comic) {
            Intent intent = new Intent(mContext, ComicDetailsActivity.class);
            intent.putExtra("comic", comic);
            return intent;
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }


    }
}