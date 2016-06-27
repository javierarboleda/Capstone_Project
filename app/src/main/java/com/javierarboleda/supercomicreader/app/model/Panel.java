package com.javierarboleda.supercomicreader.app.model;

import android.graphics.PointF;

/**
 * Created by javierarboleda on 5/8/16.
 */
public class Panel {

    private PointF mTopLeft;
    private PointF mTopRight;
    private PointF mBottomLeft;
    private PointF mBottomRight;
    private PointF mLeftPane;
    private PointF mRightPane;
    private PointF mTopPane;
    private PointF mBottomPane;
    private float mScale;

    public Panel(PointF topLeft, PointF topRight, PointF bottomLeft, PointF bottomRight,
                 PointF leftPane, PointF rightPane, PointF topPane, PointF bottomPane,
                 float scale) {

        this.mTopLeft = topLeft;
        this.mTopRight = topRight;
        this.mBottomLeft = bottomLeft;
        this.mBottomRight = bottomRight;
        this.mLeftPane = leftPane;
        this.mRightPane = rightPane;
        this.mTopPane = topPane;
        this.mBottomPane = bottomPane;
        this.mScale = scale;

    }

    public PointF getTopLeft() {
        return mTopLeft;
    }

    public void setTopLeft(PointF mTopLeft) {
        this.mTopLeft = mTopLeft;
    }

    public PointF getTopRight() {
        return mTopRight;
    }

    public void setTopRight(PointF mTopRight) {
        this.mTopRight = mTopRight;
    }

    public PointF getBottomLeft() {
        return mBottomLeft;
    }

    public void setBottomLeft(PointF mBottomLeft) {
        this.mBottomLeft = mBottomLeft;
    }

    public PointF getBottomRight() {
        return mBottomRight;
    }

    public void setBottomRight(PointF mBottomRight) {
        this.mBottomRight = mBottomRight;
    }

    public PointF getMidpoint() {
        float x = (mTopLeft.x + mBottomRight.x) / 2;
        float y = (mTopLeft.y + mBottomRight.y) / 2;

        return new PointF(x, y);
    }

    public PointF getLeftPane() {
        return mLeftPane;
    }

    public void setLeftPane(PointF mLeftPane) {
        this.mLeftPane = mLeftPane;
    }

    public PointF getRightPane() {
        return mRightPane;
    }

    public void setRightPane(PointF mRightPane) {
        this.mRightPane = mRightPane;
    }

    public PointF getTopPane() {
        return mTopPane;
    }

    public void setTopPane(PointF mTopPane) {
        this.mTopPane = mTopPane;
    }

    public PointF getBottomPane() {
        return mBottomPane;
    }

    public void setBottomPane(PointF mBottomPane) {
        this.mBottomPane = mBottomPane;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float mScale) {
        this.mScale = mScale;
    }

}
