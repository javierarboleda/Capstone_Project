package com.javierarboleda.supercomicreader.app.ui;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.data.ComicContract;
import com.javierarboleda.supercomicreader.app.model.Comic;
import com.javierarboleda.supercomicreader.app.model.Creation;
import com.javierarboleda.supercomicreader.app.model.Mode;
import com.javierarboleda.supercomicreader.app.model.SavedPanel;
import com.javierarboleda.supercomicreader.app.util.AnimationUtil;

import java.util.ArrayList;

import static com.javierarboleda.supercomicreader.app.data.ComicContract.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CreationModeActivity extends AppCompatActivity implements ComicPagerAdapter.Callback
{

    final String TAG = CreationModeActivity.class.getName();
    ViewPager mViewPager;
    private SubsamplingScaleImageView mImageView;
    private SubsamplingScaleImageView mHiddenImageView;
    private Comic mComic;
    private Creation mCreation;
    private ArrayList<SavedPanel> mSavedPanels;
    private int mSavedPanelNumber;
    private View mTopPanel;
    private View mBottomPanel;
    private View mLeftPanel;
    private View mRightPanel;
    private Menu mMenu;
    private int mUnitSize;
    private String mActivePanels;
    private float mOriginalScale;
    private int mPanelPosition;
    private Mode mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComic = getIntent().getParcelableExtra("comic");
        mCreation = getIntent().getParcelableExtra("creation");
        mSavedPanels = getIntent().getParcelableArrayListExtra("saved_panels");
        mMode = (Mode) getIntent().getSerializableExtra("mode");

        Log.d(TAG, "onCreate: "
                + "\n comic=" + mComic.getTitle()
                + "\n creation=" + mCreation.getTitle()
                + "\n savedPanels size=" + mSavedPanels.size()
                + "\n mode=" + mMode
        );

        mSavedPanelNumber = mSavedPanels.size();

        setContentView(R.layout.activity_creation_mode);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mHiddenImageView = (SubsamplingScaleImageView) findViewById(R.id.comicHiddenImageView);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        init();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            switch (mMode) {
                case CREATE:
                    actionBar.setDisplayShowTitleEnabled(false);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    break;
                case READ:
                    actionBar.hide();
                    break;
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ComicPagerAdapter(this, mComic));
        //mViewPager.setPageTransformer(true, new CustomPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onCreate.onPageSelected: page change listener, position=" + position);
                loadImageViews();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_creation_mode_activity, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent homeIntent = new Intent(this, ComicDetailsActivity.class);
//                homeIntent.putExtra("comic", mComic);
//                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
                break;
            case R.id.top_bottom_side_toggle:
                toggleActivePanel();
                break;
            case R.id.unitaction_bar_button:
                toggleUnitSize();
                break;
            case R.id.plus_action_bar_button:
                updatePanelSize(1);
                break;
            case R.id.minus_action_bar_button:
                updatePanelSize(-1);
                break;
            case R.id.add_panel_action_bar_button:
                savePanel();
                break;
            case R.id.play_action_bar_button:
                playNextPanel();
                break;
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    private void playNextPanel() {
        Log.d(TAG, "playNextPanel: mPanelPosition=" + mPanelPosition);
        if (!mSavedPanels.isEmpty()) {
            animateToPanel(mSavedPanels.get(mPanelPosition));
            mPanelPosition++;
            if (mPanelPosition == mSavedPanels.size()) {
                mPanelPosition = 0;
            }
        }
        Log.d(TAG, "playNextPanel: new mPanelPosition=" + mPanelPosition);
    }

    private void toggleActivePanel() {
        switch (mActivePanels) {
            case "B/T":
                mActivePanels = "L/R";
                break;
            default:
                mActivePanels = "B/T";
                break;
        }
        mMenu.findItem(R.id.top_bottom_side_toggle).setTitle(mActivePanels);
    }

    private void toggleUnitSize() {
        switch (mUnitSize) {
            case 50:
                mUnitSize = 10;
                break;
            case 10:
                mUnitSize = 5;
                break;

            case 5:
                mUnitSize = 1;
                break;
            default:
                mUnitSize = 50;
                break;
        }
        mMenu.findItem(R.id.unitaction_bar_button).setTitle(String.valueOf(mUnitSize));
    }

    private void updatePanelSize(int posOrNeg) {
        switch (mActivePanels) {
            case "B/T":
                mTopPanel.getLayoutParams().height += mUnitSize * posOrNeg;
                mBottomPanel.getLayoutParams().height += mUnitSize * posOrNeg;
                mTopPanel.requestLayout();
                mBottomPanel.requestLayout();
                break;
            case "L/R":
                mLeftPanel.getLayoutParams().width += mUnitSize * posOrNeg;
                mRightPanel.getLayoutParams().width += mUnitSize * posOrNeg;
                mLeftPanel.requestLayout();
                mRightPanel.requestLayout();
                break;
        }
    }

    private void savePanel() {

        float x, y, scale;

        x = 0;
        y = 0;
        PointF topLeftSourcePointF = mImageView.viewToSourceCoord(x, y);
        topLeftSourcePointF.x =
                topLeftSourcePointF.x / (float)mImageView.getSWidth();
        topLeftSourcePointF.y =
                topLeftSourcePointF.y / (float)mImageView.getSHeight();

        x = mImageView.getWidth();
        PointF topRightSourcePointF = mImageView.viewToSourceCoord(x, y);
        topRightSourcePointF.x =
                topRightSourcePointF.x / (float)mImageView.getSWidth();
        topRightSourcePointF.y =
                topRightSourcePointF.y / (float)mImageView.getSHeight();

        x = 0;
        y = mImageView.getHeight();
        PointF bottomLeftSourcePointF = mImageView.viewToSourceCoord(x, y);
        bottomLeftSourcePointF.x =
                bottomLeftSourcePointF.x / (float)mImageView.getSWidth();
        bottomLeftSourcePointF.y =
                bottomLeftSourcePointF.y / (float)mImageView.getSHeight();

        x = mImageView.getWidth();
        PointF bottomRightSourcePointF = mImageView.viewToSourceCoord(x, y);
        bottomRightSourcePointF.x =
                bottomRightSourcePointF.x / (float)mImageView.getSWidth();
        bottomRightSourcePointF.y =
                bottomRightSourcePointF.y / (float)mImageView.getSHeight();

        scale = mImageView.getScale() / mOriginalScale;

        String debugText = "TL: " + topLeftSourcePointF.x + ", " + topLeftSourcePointF.y +
                " TR: " + topRightSourcePointF.x + ", " + topRightSourcePointF.y +
                " BL: " + bottomLeftSourcePointF.x + ", " + bottomLeftSourcePointF.y +
                " BR: " + bottomRightSourcePointF.x + ", " + bottomRightSourcePointF.y;

        //mCoordTextView.setText(debugText);

        // get left panel x, and the percentage x point on image
        x = mLeftPanel.getWidth();
        PointF leftPanePointF = mImageView.viewToSourceCoord(x, 0);
        leftPanePointF.x =
                leftPanePointF.x / (float) mImageView.getSWidth();

        // get right panel x, and the percentage x point on image
        x = mImageView.getWidth() - mRightPanel.getWidth();
        PointF rightPanePointF = mImageView.viewToSourceCoord(x, 0);
        rightPanePointF.x =
                rightPanePointF.x / (float) mImageView.getSWidth();

        // get right panel x, and the percentage x point on image
        y = mTopPanel.getHeight();
        PointF topPanePointF = mImageView.viewToSourceCoord(0, y);
        topPanePointF.y =
                topPanePointF.y / (float) mImageView.getSHeight();

        // get right panel x, and the percentage x point on image
        y = mImageView.getHeight() - mBottomPanel.getHeight();
        PointF bottomPanePointF = mImageView.viewToSourceCoord(0, y);
        bottomPanePointF.y =
                bottomPanePointF.y / (float) mImageView.getSHeight();

        mSavedPanelNumber++;

        SavedPanel savedPanel = new SavedPanel(
                                        0,
                                        mCreation.getId(),
                                        mSavedPanelNumber,
                                        mViewPager.getCurrentItem(),
                                        topLeftSourcePointF.x,
                                        topLeftSourcePointF.y,
                                        topRightSourcePointF.x,
                                        topRightSourcePointF.y,
                                        bottomLeftSourcePointF.x,
                                        bottomLeftSourcePointF.y,
                                        bottomRightSourcePointF.x,
                                        bottomRightSourcePointF.y,
                                        leftPanePointF.x,
                                        rightPanePointF.x,
                                        topPanePointF.y,
                                        bottomPanePointF.y,
                                        scale
                                    );

        insertPanelIntoDb(savedPanel);

//        Panel panel = new Panel(topLeftSourcePointF, topRightSourcePointF,
//                bottomLeftSourcePointF, bottomRightSourcePointF, leftPanePointF, rightPanePointF,
//                topPanePointF, bottomPanePointF, scale, mViewPager.getCurrentItem());
//
//        mSavedPanelsOld.add(panel);

//        Log.d("SavedPanelImage",
//                panel.getTopLeft() + " : " +
//                        panel.getTopRight() + " : " +
//                        panel.getBottomLeft() + " : " +
//                        panel.getBottomRight() + " : " +
//                        panel.getScale()
//        );
//        Log.d("SavedPanelBorders",
//                panel.getLeftPane().x + " : " +
//                        panel.getRightPane().x + " : " +
//                        panel.getTopPane().y + " : " +
//                        panel.getBottomPane().y
//        );
    }

    private void insertPanelIntoDb(SavedPanel savedPanel) {

        ContentValues savedPanelValues = new ContentValues();

        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_CREATION_ID, savedPanel.getCreationId());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_NUMBER, savedPanel.getNumber());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_PAGE, savedPanel.getPage());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_TOP_LEFT_X, savedPanel.getTopLeftX());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_TOP_LEFT_Y, savedPanel.getTopLeftY());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_TOP_RIGHT_X, savedPanel.getTopRightX());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_TOP_RIGHT_Y, savedPanel.getTopRightY());
        savedPanelValues.put(
                SavedPanelEntry.COLUMN_NAME_BOTTOM_LEFT_X,savedPanel.getBottomLeftX());
        savedPanelValues.put(
                SavedPanelEntry.COLUMN_NAME_BOTTOM_LEFT_Y, savedPanel.getBottomLeftY());
        savedPanelValues.put(
                SavedPanelEntry.COLUMN_NAME_BOTTOM_RIGHT_X, savedPanel.getBottomRightX());
        savedPanelValues.put(
                SavedPanelEntry.COLUMN_NAME_BOTTOM_RIGHT_Y, savedPanel.getBottomRightY());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_LEFT_PANE, savedPanel.getLeftPane());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_RIGHT_PANE, savedPanel.getRightPane());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_TOP_PANE, savedPanel.getTopPane());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_BOTTOM_PANE, savedPanel.getBottomPane());
        savedPanelValues.put(SavedPanelEntry.COLUMN_NAME_SCALE, savedPanel.getScale());

        Uri uri = this.getContentResolver().insert(SavedPanelEntry.CONTENT_URI, savedPanelValues);

        savedPanel.setId((int) ContentUris.parseId(uri));

        mSavedPanels.add(savedPanel);

        Log.d(TAG, "insertPanelIntoDb: current savedPanels size=" + mSavedPanels.size());

        Log.d(TAG, "insertPanelIntoDb: inserting panel:"
                + "\n" + "id=" + savedPanel.getId()
                + "\n" + "creationId=" + savedPanel.getCreationId()
                + "\n" + "panelNumber=" + savedPanel.getNumber()
                + "\n" + "page=" + savedPanel.getPage()
                + "\n" + "scale=" + savedPanel.getScale()
        );
    }

    private void animateToPanel(final SavedPanel panel) {

        Log.d(TAG, "animateToPanel: Entered"
                + "\n panel.getPage()=" + panel.getPage()
                + "\n mViewPager.getCurrentItem()=" + mViewPager.getCurrentItem()
        );

        if (panel.getPage() != mViewPager.getCurrentItem()) {

            mViewPager.setCurrentItem(panel.getPage(), false);

            Log.d(TAG, "animateToPanel: getPage != getCurrentItem");
        }

        final PointF midPointPercentage = panel.getMidpoint();
        final PointF midPointCoord = new PointF(midPointPercentage.x * mImageView.getSWidth(),
                midPointPercentage.y * mImageView.getSHeight());
        final float scale = panel.getScale() * mOriginalScale;

        PointF midPointView = mImageView.sourceToViewCoord(midPointCoord);

        final float screenRatio = (float)mImageView.getWidth() / (float)mImageView.getHeight();

        mHiddenImageView.animateScaleAndCenter(scale, midPointCoord)
                .withDuration(1)
                .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                .withInterruptible(false).withOnAnimationEventListener(
                new SubsamplingScaleImageView.OnAnimationEventListener() {
                    @Override
                    public void onComplete() {

//                        float leftPerc = panel.getLeftPane().x - panel.getTopLeft().x;
//                        float rightPerc = panel.getTopRight().x - panel.getRightPane().x;
//                        float topPerc = panel.getTopPane().y - panel.getTopRight().y;
//                        float bottomPerc = panel.getBottomRight().y - panel.getBottomPane().y;

                        int left = (int) mHiddenImageView.sourceToViewCoord(
                                panel.getLeftPane() * mHiddenImageView.getSWidth(), 0).x;
                        int right = mHiddenImageView.getWidth()
                                - (int) mHiddenImageView.sourceToViewCoord(
                                panel.getRightPane() * mHiddenImageView.getSWidth(), 0).x;
                        int top = (int) mHiddenImageView.sourceToViewCoord(
                                0, panel.getTopPane() * mHiddenImageView.getSHeight()).y;
                        int bottom = mHiddenImageView.getHeight()
                                - (int) mHiddenImageView.sourceToViewCoord(
                                0, panel.getBottomPane() * mHiddenImageView.getSHeight()).y;

                        animateBorderPanels(left, right, top, bottom);

                        mImageView.animateScaleAndCenter(scale, midPointCoord)
                                .withDuration(500)
                                .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                                .withInterruptible(false).start();
                    }

                    @Override
                    public void onInterruptedByUser() {

                    }

                    @Override
                    public void onInterruptedByNewAnim() {

                    }
                }).start();



//        Log.d("AnimateToPanel", "final mpv:" + midPointView);



//        Log.d("AnimateToPanel", "mpp:" + midPointPercentage + " mpc:" + midPointCoord +
//                " scale:" + scale + " mpv:" + midPointView + " ratio:" + screenRatio);
//
//        Log.d("AnimateToPanel", "LP:" + leftPerc + " RP:" + rightPerc + " TP:" + topPerc +
//                " BP:" + bottomPerc);
//
//        Log.d("AnimateToPanel", "L:" + left + " R:" + right + " T:" + top + " B:" + bottom);


    }

    private void animateBorderPanels(int left, int right, int top, int bottom) {
        ValueAnimator va1 =
                AnimationUtil.getTopBottomPanelValueAnimator(mTopPanel, top);

        ValueAnimator va2 =
                AnimationUtil.getTopBottomPanelValueAnimator(mBottomPanel, bottom);

        ValueAnimator va3 =
                AnimationUtil.getLeftRightPanelValueAnimator(mLeftPanel, left);

        ValueAnimator va4 =
                AnimationUtil.getLeftRightPanelValueAnimator(mRightPanel, right);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(va1, va2, va3, va4);
        set.start();
    }

    private void init() {

        Log.d(TAG, "init: Entered");

        mTopPanel = findViewById(R.id.topPanel);
        mBottomPanel = findViewById(R.id.bottomPanel);
        mLeftPanel = findViewById(R.id.leftPanel);
        mRightPanel = findViewById(R.id.rightPanel);
        mUnitSize = 50;
        mActivePanels = "B/T";
        mPanelPosition = 0;
    }

    private void resetEditControlsAndPanels() {
        mTopPanel.getLayoutParams().height = 0;
        mBottomPanel.getLayoutParams().height = 0;
        mLeftPanel.getLayoutParams().width = 0;
        mRightPanel.getLayoutParams().width = 0;
        mTopPanel.requestLayout();
        mBottomPanel.requestLayout();
        mLeftPanel.requestLayout();
        mRightPanel.requestLayout();
        mUnitSize = 50;
        mMenu.findItem(R.id.unitaction_bar_button).setTitle(String.valueOf(mUnitSize));
        View view = findViewById(R.id.main_layout);
        Log.d(TAG, "resetEditControlsAndPanels: invalidating view ( view.invalidate() )");
        view.invalidate();
    }

    @Override
    public void onViewLoaded() {
        Log.d(TAG, "onViewLoaded: Entered");
        if (mImageView == null) {
            Log.d(TAG, "onViewLoaded: mImageView is null, calling loadImageViews()");
            loadImageViews();
        }

        Log.d(TAG, mImageView == null ? "onViewLoaded: mImageView = null"
                : "onViewLoaded: mImageView = not null");
        Log.d(TAG, mHiddenImageView == null ? "onViewLoaded: mHiddenImageView = null"
                : "mHiddenImageView: mImageView = not null");
    }

    private void loadImageViews() {

        Log.d(TAG, "loadImageViews: Entered"
            + "\n Getting tagged images at page/position = " + mViewPager.getCurrentItem()
        );

        mImageView = (SubsamplingScaleImageView)
                mViewPager.findViewWithTag("v" + mViewPager.getCurrentItem());

        mHiddenImageView = (SubsamplingScaleImageView)
                mViewPager.findViewWithTag("h" + mViewPager.getCurrentItem());

        if (mImageView == null) {
            Log.d(TAG, "loadImageViews: mImageView = null!");
        }

        Log.d(TAG, "loadImageViews: setting click and gesture listeners on mImageView");

        assert mImageView != null;
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMode) {
                    case CREATE:
                        resetEditControlsAndPanels();
                        break;
                    case READ:
                        playNextPanel();
                        break;
                }
            }
        });

        mImageView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onImageLoaded() {
                Log.d(TAG, "loadImageViews.onImageLoaded: mImageView loaded");
                mOriginalScale = mImageView.getScale();
            }
            @Override
            public void onReady() {

            }
            @Override
            public void onPreviewLoadError(Exception e) {

            }
            @Override
            public void onImageLoadError(Exception e) {

            }
            @Override
            public void onTileLoadError(Exception e) {

            }
        });

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        switch (mMode) {
                            case CREATE:
                                resetEditControlsAndPanels();
                                break;
                            case READ:
                                break;
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        switch (mMode) {
                            case CREATE:
                                break;
                            case READ:
                                break;
                        }
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        toggle();
                        return true;
                    }
                });

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);

            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.show();
//            }

            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);

            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


}
