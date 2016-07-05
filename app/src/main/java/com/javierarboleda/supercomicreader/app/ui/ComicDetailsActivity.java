package com.javierarboleda.supercomicreader.app.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.data.CursorWithData;
import com.javierarboleda.supercomicreader.app.model.Comic;
import com.javierarboleda.supercomicreader.app.model.Creation;
import com.javierarboleda.supercomicreader.app.model.Mode;
import com.javierarboleda.supercomicreader.app.model.SavedPanel;

import java.util.ArrayList;

import static com.javierarboleda.supercomicreader.app.data.ComicContract.CreationEntry;
import static com.javierarboleda.supercomicreader.app.data.ComicContract.SavedPanelEntry;

public class ComicDetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = ComicDetailsActivity.class.getName();
    private Comic mComic;
    private SubsamplingScaleImageView mBackgroundImageView;
    private static final int CREATION_LOADER = 0;
    private static final int SAVED_PANEL_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comic_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mComic = savedInstanceState.getParcelable("comic");
        } else {
            mComic = getIntent().getParcelableExtra("comic");
        }

        String coverFilePath = Environment.getExternalStorageDirectory() +
                mComic.getFile() + mComic.getCover();

        TextView titleTextView = (TextView) findViewById(R.id.title_textView);

        assert titleTextView != null;
        titleTextView.setText(mComic.getTitle());

        setBackgroundCoverImage(coverFilePath);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_creation_fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        getSupportLoaderManager().initLoader(CREATION_LOADER, null, ComicDetailsActivity.this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("comic", mComic);
    }

    private void showDialog() {

        new MaterialDialog.Builder(this)
            .title(R.string.input)
            .content(R.string.input_content)
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input(R.string.input_hint, R.string.input_prefill,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        Creation creation = insertCreation(input.toString());

                        ArrayList<SavedPanel> savedPanels = new ArrayList<>();

                        startCreationModeActivity(creation, savedPanels, Mode.CREATE);

                }
            }).show();
    }

    private Creation insertCreation(String input) {

        int comicId = mComic.getId();
        String title = input;
        String author = "";
        int date = 0;
        int lastPanelRead = 0;

        ContentValues creationValues = new ContentValues();

        creationValues.put(CreationEntry.COLUMN_NAME_COMIC_ID, comicId);
        creationValues.put(CreationEntry.COLUMN_NAME_TITLE, title);
        creationValues.put(CreationEntry.COLUMN_NAME_AUTHOR, author);
        creationValues.put(CreationEntry.COLUMN_NAME_CREATION_DATE, date);
        creationValues.put(CreationEntry.COLUMN_NAME_LAST_PANEL_READ, lastPanelRead);

        Uri uri = this.getContentResolver().insert(CreationEntry.CONTENT_URI, creationValues);

        return new Creation((int) ContentUris.parseId(uri), comicId, input, author,
                date, lastPanelRead);

    }

    private void setUpRecyclerView(Cursor cursor) {
        ComicDetailsAdapter comicDetailsAdapter= new ComicDetailsAdapter(this, cursor, mComic);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(comicDetailsAdapter);
    }

    private void setBackgroundCoverImage(String coverFilePath) {
        mBackgroundImageView = (SubsamplingScaleImageView) findViewById(R.id.background);

        mBackgroundImageView.setImage(ImageSource.uri(coverFilePath));

        mBackgroundImageView.setPanEnabled(false);

        mBackgroundImageView.setZoomEnabled(false);

        mBackgroundImageView.setOnImageEventListener(
                new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {}

            @Override
            public void onImageLoaded() {
                int sWidth = mBackgroundImageView.getSWidth();
                int width = mBackgroundImageView.getWidth();

                Log.d("image", sWidth + " " + width);

                mBackgroundImageView.setScaleAndCenter(
                        ((float) mBackgroundImageView.getWidth()) /
                                ((float) mBackgroundImageView.getSWidth()),
                        new PointF(mBackgroundImageView.getWidth()/2, 0)
                );
            }

            @Override
            public void onPreviewLoadError(Exception e) {}

            @Override
            public void onImageLoadError(Exception e) {}

            @Override
            public void onTileLoadError(Exception e) {}
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        switch (id) {
            case CREATION_LOADER: {
                return new CursorLoader(
                        this,
                        CreationEntry.buildCreationDirUri(),
                        null,
                        CreationEntry.COLUMN_NAME_COMIC_ID + " = ?",
                        new String[]{String.valueOf(mComic.getId())},
                        null
                );
            }
            case SAVED_PANEL_LOADER: {
                Creation creation = (Creation) args.get("creation");
                return new CursorLoader(
                        this,
                        SavedPanelEntry.buildSavedPanelDirUri(),
                        null,
                        SavedPanelEntry.COLUMN_NAME_CREATION_ID + " = ?",
                        new String[]{String.valueOf(creation.getId())},
                        SavedPanelEntry.COLUMN_NAME_NUMBER + " ASC"
                ){
                    @Override
                    public Cursor loadInBackground() {
                        return new CursorWithData<Bundle>(super.loadInBackground(), args);
                    }
                };
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int id = loader.getId();

        switch (id) {
            case CREATION_LOADER: {
                setUpRecyclerView(data);
                break;
            }
            case SAVED_PANEL_LOADER: {
                CursorWithData<Bundle> cursorWithData = (CursorWithData<Bundle>) data;
                Bundle args = cursorWithData.getData();
                data = cursorWithData.getWrappedCursor();
                Creation creation = (Creation) args.get("creation");
                Mode mode = (Mode) args.getSerializable("mode");
                ArrayList<SavedPanel> savedPanels = getSavedPanelsFromCursor(data);

                Log.d(TAG, "onLoadFinished: passing saved panels:");
                for (SavedPanel s : savedPanels) {
                    Log.d(TAG,""
                            + "\n" + "id=" + s.getId()
                            + "\n" + "creationId=" + s.getCreationId()
                            + "\n" + "panelNumber=" + s.getNumber()
                            + "\n" + "page=" + s.getPage()
                            + "\n" + "scale=" + s.getScale()
                    );
                }

                startCreationModeActivity(creation, savedPanels, mode);
                break;
            }
        }
    }

    private void startCreationModeActivity(Creation creation, ArrayList<SavedPanel> savedPanels,
                                           Mode mode) {
        Intent intent = new Intent(this, CreationModeActivity.class);
        intent.putExtra("comic", mComic);
        intent.putExtra("creation", creation);
        intent.putExtra("mode", mode);
        intent.putParcelableArrayListExtra("saved_panels", savedPanels);

        startActivity(intent);
    }

    private ArrayList<SavedPanel> getSavedPanelsFromCursor(Cursor data) {

        ArrayList<SavedPanel> savedPanels = new ArrayList<>();

        data.moveToFirst();

         do {
            savedPanels.add(new SavedPanel(
                    data.getInt(SavedPanelEntry.INDEX_ID),
                    data.getInt(SavedPanelEntry.INDEX_CREATION_ID),
                    data.getInt(SavedPanelEntry.INDEX_NUMBER),
                    data.getInt(SavedPanelEntry.INDEX_PAGE),
                    data.getFloat(SavedPanelEntry.INDEX_TOP_LEFT_X),
                    data.getFloat(SavedPanelEntry.INDEX_TOP_LEFT_Y),
                    data.getFloat(SavedPanelEntry.INDEX_TOP_RIGHT_X),
                    data.getFloat(SavedPanelEntry.INDEX_TOP_RIGHT_Y),
                    data.getFloat(SavedPanelEntry.INDEX_BOTTOM_LEFT_X),
                    data.getFloat(SavedPanelEntry.INDEX_BOTTOM_LEFT_Y),
                    data.getFloat(SavedPanelEntry.INDEX_BOTTOM_RIGHT_X),
                    data.getFloat(SavedPanelEntry.INDEX_BOTTOM_RIGHT_Y),
                    data.getFloat(SavedPanelEntry.INDEX_LEFT_PANE),
                    data.getFloat(SavedPanelEntry.INDEX_RIGHT_PANE),
                    data.getFloat(SavedPanelEntry.INDEX_TOP_PANE),
                    data.getFloat(SavedPanelEntry.INDEX_BOTTOM_PANE),
                    data.getFloat(SavedPanelEntry.INDEX_SCALE)
            ));
        } while (data.moveToNext());

        return savedPanels;
    }

    public void loadSavedPanelsAndStartActivity(Creation creation, Mode mode) {
        Bundle args = new Bundle();
        args.putParcelable("creation", creation);
        args.putSerializable("mode", mode);
        getSupportLoaderManager().restartLoader(SAVED_PANEL_LOADER, args, ComicDetailsActivity.this);
        //getSupportLoaderManager().initLoader(SAVED_PANEL_LOADER, args, ComicDetailsActivity.this);
    }

    public class ComicDetailsAdapter extends
            RecyclerView.Adapter<ComicDetailsAdapter.ViewHolder> {

        private Cursor mCursor;
        private Context mContext;

        public ComicDetailsAdapter(Context context, Cursor cursor, Comic comic) {
            mContext = context;
            mCursor = cursor;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final TextView mTextView;
            public final View mPlayIconView;
            public final View mEditIconView;

            public ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view.findViewById(R.id.text_view);
                mPlayIconView = view.findViewById(R.id.play_icon);
                mEditIconView = view.findViewById(R.id.edit_creation_image_view);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_comic_details_creation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d("bindview", "position " + position);

            mCursor.moveToPosition(position);

            int id = mCursor.getInt(CreationEntry.INDEX_ID);
            String title = mCursor.getString(CreationEntry.INDEX_TITLE);
            String author = mCursor.getString(CreationEntry.INDEX_AUTHOR);
            int creationDate = mCursor.getInt(CreationEntry.INDEX_CREATION_DATE);
            int lastPanelRead = mCursor.getInt(CreationEntry.INDEX_LAST_PANEL_READ);

            final Creation creation = new Creation(id, mComic.getId(), title, author, creationDate,
                    lastPanelRead);

            TextView textView = holder.mTextView;
            textView.setText(title);

            holder.mPlayIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComicDetailsActivity.this
                            .loadSavedPanelsAndStartActivity(creation, Mode.READ);
                }
            });

            holder.mEditIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComicDetailsActivity.this
                            .loadSavedPanelsAndStartActivity(creation, Mode.CREATE);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("onLoaderReset", "onLoaderReset");
    }
}
