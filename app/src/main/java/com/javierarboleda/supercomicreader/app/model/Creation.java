package com.javierarboleda.supercomicreader.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by javierarboleda on 6/30/16.
 */
public class Creation implements Parcelable{

    public int mId;
    public int mComicId;
    public String mTitle;
    public String mAuthor;
    public int mCreationDate;
    public int mLastPanelRead;

    public Creation(int id, int comicId, String title, String author, int creationDate, int lastPanelRead) {
        mId = id;
        mComicId = comicId;
        mTitle = title;
        mAuthor = author;
        mCreationDate = creationDate;
        mLastPanelRead = lastPanelRead;
    }

    public Creation(Parcel in) {
        mId = in.readInt();
        mComicId = in.readInt();
        mTitle = in.readString();
        mAuthor = in.readString();
        mCreationDate = in.readInt();
        mLastPanelRead = in.readInt();
    }

    public static final Parcelable.Creator<Creation> CREATOR = new Parcelable.Creator<Creation>() {
        @Override
        public Creation createFromParcel(Parcel in) {
            return new Creation(in);
        }

        @Override
        public Creation[] newArray(int size) {
            return new Creation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mComicId);
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeInt(mCreationDate);
        dest.writeInt(mLastPanelRead);
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getComicId() {
        return mComicId;
    }

    public void setComicId(int comicId) {
        mComicId = comicId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public int getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(int creationDate) {
        mCreationDate = creationDate;
    }

    public int getLastPanelRead() {
        return mLastPanelRead;
    }

    public void setLastPanelRead(int lastPanelRead) {
        mLastPanelRead = lastPanelRead;
    }
}
