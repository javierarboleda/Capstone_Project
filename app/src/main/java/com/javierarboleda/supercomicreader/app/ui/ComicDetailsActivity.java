package com.javierarboleda.supercomicreader.app.ui;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.javierarboleda.supercomicreader.app.model.Comic;

public class ComicDetailsActivity extends AppCompatActivity {

    Comic mComic;
    SubsamplingScaleImageView mBackgroundImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComic = getIntent().getParcelableExtra("comic");

        String coverFilePath = Environment.getExternalStorageDirectory() +
                mComic.getFile() + mComic.getCover();

        //getLoaderManager().initLoader(0, null, null);

        setContentView(R.layout.activity_comic_details);

        TextView titleTextView = (TextView) findViewById(R.id.title_textView);

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

        setUpRecyclerView();

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
                        Intent intent =
                                new Intent(getApplicationContext(), CreationModeActivity.class);
                        intent.putExtra("comic", mComic);
                        startActivity(intent);
                }
            }).show();
    }

    private void setUpRecyclerView() {
        ComicDetailsAdapter comicDetailsAdapter= new ComicDetailsAdapter();

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

    public static class ComicDetailsAdapter extends RecyclerView.Adapter<ComicDetailsAdapter.ViewHolder> {

        public ComicDetailsAdapter() {

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

//            public final View mView;
//            public final ImageView mImageView;
//            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
//                mView = view;
//                mImageView = (ImageView) view.findViewById(R.id.comic_image_view);
//                mTextView = (TextView) view.findViewById(android.R.id.text1);
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
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

}
