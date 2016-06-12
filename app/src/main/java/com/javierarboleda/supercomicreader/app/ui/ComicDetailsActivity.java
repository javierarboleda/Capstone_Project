package com.javierarboleda.supercomicreader.app.ui;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

        setContentView(R.layout.activity_comic_details);



        TextView titleTextView = (TextView) findViewById(R.id.title_textView);

        titleTextView.setText(mComic.getTitle());



        mBackgroundImageView = (SubsamplingScaleImageView) findViewById(R.id.background);

        mBackgroundImageView.setImage(ImageSource.uri(coverFilePath));

        mBackgroundImageView.setPanEnabled(false);

        mBackgroundImageView.setZoomEnabled(false);

        mBackgroundImageView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {}

            @Override
            public void onImageLoaded() {
                int sWidth = mBackgroundImageView.getSWidth();
                int width = mBackgroundImageView.getWidth();

                Log.d("image", sWidth + " " + width);

                mBackgroundImageView.setScaleAndCenter(
                        ((float) mBackgroundImageView.getWidth()) / ((float) mBackgroundImageView.getSWidth()),
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



}
