package com.javierarboleda.supercomicreader.app.ui;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.model.Comic;

/**
 * Created by javierarboleda on 6/19/16.
 */
public class ComicPagerAdapter extends PagerAdapter {

    private final String TAG = ComicPagerAdapter.class.getName();
    private Context mContext;
    private Comic mComic;

    public ComicPagerAdapter(Context context, Comic comic) {
        mContext = context;
        mComic = comic;

        Log.d(TAG, "ComicPagerAdapter: comic = " + comic.getTitle());
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.d(TAG, "instantiateItem: Entered"
                + "\nposition=" + position
        );

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout =
                (ViewGroup) inflater.inflate(R.layout.pager_item_comic, container, false);
        container.addView(layout);

        String imageFilePath = Environment.getExternalStorageDirectory() +
                mComic.getFile() + mComic.getmImagePaths().get(position);

        SubsamplingScaleImageView mComicImageView =
                (SubsamplingScaleImageView) layout.findViewById(R.id.comic_image_view);

        SubsamplingScaleImageView mHiddenComicImageView =
                (SubsamplingScaleImageView) layout.findViewById(R.id.comicHiddenImageView);

        Log.d(TAG, "instantiateItem: Setting image source and params. Set tag 'h' & 'v'");
        mHiddenComicImageView.setImage(ImageSource.uri(imageFilePath));
        mHiddenComicImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);
        mHiddenComicImageView.setTag("h" + position);

        mComicImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);
        mComicImageView.setImage(ImageSource.uri(imageFilePath));
        mComicImageView.setTag("v" + position);

        Log.d(TAG, "instantiateItem: calling onViewLoaded callback");
        ((Callback) mContext).onViewLoaded();

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem: position=" + position);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mComic.getmImagePaths().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface Callback {
        void onViewLoaded();
    }

}
