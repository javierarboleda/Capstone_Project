package com.javierarboleda.supercomicreader.app.model;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by javierarboleda on 6/30/16.
 */
public class SavedPanel implements Parcelable{

    private int mId;
    private int mCreationId;
    private int mNumber;
    private int mPage;
    private float mTopLeftX;
    private float mTopLeftY;
    private float mTopRightX;
    private float mTopRightY;
    private float mBottomLeftX;
    private float mBottomLeftY;
    private float mBottomRightX;
    private float mBottomRightY;
    private float mLeftPane;
    private float mRightPane;
    private float mTopPane;
    private float mBottomPane;
    private float mScale;

    public SavedPanel(int id, int creationId, int number, int page, float topLeftX, float topLeftY,
                      float topRightX, float topRightY, float bottomLeftX, float bottomLeftY,
                      float bottomRightX, float bottomRightY, float leftPane, float rightPane,
                      float topPane, float bottomPane, float scale) {
        mId = id;
        mCreationId = creationId;
        mNumber = number;
        mPage = page;
        mTopLeftX = topLeftX;
        mTopLeftY = topLeftY;
        mTopRightX = topRightX;
        mTopRightY = topRightY;
        mBottomLeftX = bottomLeftX;
        mBottomLeftY = bottomLeftY;
        mBottomRightX = bottomRightX;
        mBottomRightY= bottomRightY;
        mLeftPane = leftPane;
        mRightPane = rightPane;
        mTopPane = topPane;
        mBottomPane = bottomPane;
        mScale = scale;
    }

    public SavedPanel(Parcel in) {
        mId = in.readInt();
        mCreationId = in.readInt();
        mNumber = in.readInt();
        mPage = in.readInt();
        mTopLeftX = in.readFloat();
        mTopLeftY = in.readFloat();
        mTopRightX = in.readFloat();
        mTopRightY = in.readFloat();
        mBottomLeftX = in.readFloat();
        mBottomLeftY = in.readFloat();
        mBottomRightX = in.readFloat();
        mBottomRightY = in.readFloat();
        mLeftPane = in.readFloat();
        mRightPane = in.readFloat();
        mTopPane = in.readFloat();
        mBottomPane = in.readFloat();
        mScale = in.readFloat();
    }
    
    public static final Parcelable.Creator<SavedPanel> CREATOR = new Parcelable.Creator<SavedPanel>() {
        @Override
        public SavedPanel createFromParcel(Parcel in) {
            return new SavedPanel(in);
        }

        @Override
        public SavedPanel[] newArray(int size) {
            return new SavedPanel[size];
        }
    };
    
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mCreationId);
        dest.writeInt(mNumber);
        dest.writeInt(mPage);
        dest.writeFloat(mTopLeftX);
        dest.writeFloat(mTopLeftY);
        dest.writeFloat(mTopRightX);
        dest.writeFloat(mTopRightY);
        dest.writeFloat(mBottomLeftX);
        dest.writeFloat(mBottomLeftY);
        dest.writeFloat(mBottomRightX);
        dest.writeFloat(mBottomRightY);
        dest.writeFloat(mLeftPane);
        dest.writeFloat(mRightPane);
        dest.writeFloat(mTopPane);
        dest.writeFloat(mBottomPane);
        dest.writeFloat(mScale);
    }

    public PointF getMidpoint() {
        float x = (mTopLeftX + mBottomRightX) / 2;
        float y = (mTopLeftY + mBottomRightY) / 2;

        return new PointF(x, y);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCreationId() {
        return mCreationId;
    }

    public void setCreationId(int creationId) {
        mCreationId = creationId;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public float getTopLeftX() {
        return mTopLeftX;
    }

    public void setTopLeftX(float topLeftX) {
        mTopLeftX = topLeftX;
    }

    public float getTopLeftY() {
        return mTopLeftY;
    }

    public void setTopLeftY(float topLeftY) {
        mTopLeftY = topLeftY;
    }

    public float getTopRightX() {
        return mTopRightX;
    }

    public void setTopRightX(float topRightX) {
        mTopRightX = topRightX;
    }

    public float getTopRightY() {
        return mTopRightY;
    }

    public void setTopRightY(float topRightY) {
        mTopRightY = topRightY;
    }

    public float getBottomLeftX() {
        return mBottomLeftX;
    }

    public void setBottomLeftX(float bottomLeftX) {
        mBottomLeftX = bottomLeftX;
    }

    public float getBottomLeftY() {
        return mBottomLeftY;
    }

    public void setBottomLeftY(float bottomLeftY) {
        mBottomLeftY = bottomLeftY;
    }

    public float getBottomRightX() {
        return mBottomRightX;
    }

    public void setBottomRightX(float bottomRightX) {
        mBottomRightX = bottomRightX;
    }

    public float getBottomRightY() {
        return mBottomRightY;
    }

    public void setBottomRightY(float bottomRightY) {
        mBottomRightY = bottomRightY;
    }

    public float getLeftPane() {
        return mLeftPane;
    }

    public void setLeftPane(int leftPane) {
        mLeftPane = leftPane;
    }

    public float getRightPane() {
        return mRightPane;
    }

    public void setRightPane(int rightPane) {
        mRightPane = rightPane;
    }

    public float getTopPane() {
        return mTopPane;
    }

    public void setTopPane(int topPane) {
        mTopPane = topPane;
    }

    public float getBottomPane() {
        return mBottomPane;
    }

    public void setBottomPane(int bottomPane) {
        mBottomPane = bottomPane;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale = scale;
    }
}
