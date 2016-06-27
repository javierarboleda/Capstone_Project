package com.javierarboleda.supercomicreader.app.model;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.javierarboleda.supercomicreader.app.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javierarboleda on 6/9/16.
 *
 */
public class Comic implements Parcelable {

    private String mTitle;
    private String mFile;
    private String mCover;
    private String mPages;
    private String mLastPageRead;
    private String mLastCreationRead;
    private List<String> mImagePaths;

    public Comic(String mTitle, String mFile, String mCover, String mPages, String mLastPageRead,
                 String mLastCreationRead) {
        this.mTitle = mTitle;
        this.mFile = mFile;
        this.mCover = mCover;
        this.mPages = mPages;
        this.mLastPageRead = mLastPageRead;
        this.mLastCreationRead = mLastCreationRead;

        populateImagePaths();
    }

    private void populateImagePaths() {
        mImagePaths = FileUtil.getListOfImageFileNamesInDir(
                Environment.getExternalStorageDirectory().getPath() + mFile);
    }

    public Comic(Parcel in) {
        mTitle = in.readString();
        mFile = in.readString();
        mCover = in.readString();
        mPages = in.readString();
        mLastPageRead = in.readString();
        mLastCreationRead = in.readString();
        mImagePaths = new ArrayList<>();
        in.readList(mImagePaths, null);
    }

    public static final Parcelable.Creator<Comic> CREATOR = new Parcelable.Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mFile);
        dest.writeString(mCover);
        dest.writeString(mPages);
        dest.writeString(mLastPageRead);
        dest.writeString(mLastCreationRead);
        dest.writeList(mImagePaths);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getFile() {
        return mFile;
    }

    public void setFile(String file) {
        this.mFile = file;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String cover) {
        this.mCover = cover;
    }

    public String getPages() {
        return mPages;
    }

    public void setPages(String pages) {
        this.mPages = pages;
    }

    public String getLastPageRead() {
        return mLastPageRead;
    }

    public void setLastPageRead(String lastPageRead) {
        this.mLastPageRead = lastPageRead;
    }

    public String getLastCreationRead() {
        return mLastCreationRead;
    }

    public void setLastCreationRead(String lastCreationRead) {
        mLastCreationRead = lastCreationRead;
    }

    public List<String> getmImagePaths() {
        return mImagePaths;
    }

    public void setmImagePaths(List<String> mImagePaths) {
        this.mImagePaths = mImagePaths;
    }
}
