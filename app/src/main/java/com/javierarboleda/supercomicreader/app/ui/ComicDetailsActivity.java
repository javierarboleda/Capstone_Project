package com.javierarboleda.supercomicreader.app.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.model.Comic;

public class ComicDetailsActivity extends AppCompatActivity {

    Comic mComic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComic = getIntent().getParcelableExtra("comic");

        setContentView(R.layout.activity_comic_details);
    }
}
