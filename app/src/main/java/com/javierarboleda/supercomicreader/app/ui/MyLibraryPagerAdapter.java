package com.javierarboleda.supercomicreader.app.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by javierarboleda on 5/25/16.
 */
public class MyLibraryPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    private String mTabTitles[] = new String[]{"ALL COMICS", "MY CREATIONS"};
    private Context mContext;

    public MyLibraryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllComicsFragment allComicsFragment = new AllComicsFragment();
                return allComicsFragment;
            case 1:
                MyCreationsFragment myCreationsFragment = new MyCreationsFragment();
                return myCreationsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
