package com.javierarboleda.supercomicreader.app.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.junrar.exception.RarException;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.javierarboleda.supercomicreader.R;
import com.javierarboleda.supercomicreader.app.services.AnalyticsApplication;
import com.javierarboleda.supercomicreader.app.util.ComicUtil;
import com.javierarboleda.supercomicreader.app.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_FILE_ACCESS = 200;
    private DrawerLayout mDrawerLayout;
    private MaterialDialog mProgressBar;
    private Tracker mTracker;

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        AnalyticsApplication application = ((AnalyticsApplication) LibraryActivity.this.getApplication());
        mTracker = application.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final ActionBar ab = getSupportActionBar();
//        if (ab != null) {
//            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//            ab.setDisplayHomeAsUpEnabled(true);
//        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void init() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.library_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
//                mDrawerLayout.openDrawer(GravityCompat.START);
//
//                if (isMPlus()) {
//                    requestReadExternalStoragePermission();
//                }
//
//                return true;
            }
            case R.id.action_add_comic: {
                if (isMPlus()) {
                    requestReadExternalStoragePermission();
                }

                performFileSearch();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    private void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("application/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // todo check if rar file and extract images

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.d("LibraryActivity", "OnActivityResult URI=" + uri.getPath());
            }

            String path = uri.getPath();

            File file = FileUtil.convertDocumentUriPathToFile(path);

            if (FileUtil.hasCbrExtension(path)) {
                // todo: this needs to be called from async task, ALSO add progress spinner

                new ExtractAsyncTask().execute(file);
                mProgressBar =
                        new MaterialDialog.Builder(this)
                                .title("Extracting Comic Imagaes")
                                .content("Please wait...")
                                .progress(true,0)
                                .autoDismiss(false)
                                .cancelable(false)
                                .show();
            }



        }

    }

    private boolean isMPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestReadExternalStoragePermission() {
        String[] perms = {"android.permission.READ_EXTERNAL_STORAGE",
                          "android.permission.WRITE_EXTERNAL_STORAGE"};

        requestPermissions(perms, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FILE_ACCESS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
//        adapter.addFragment(new AllComicsFragment(), "all comics");
        adapter.addFragment(new AllComicsFragment(), "all comics");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public class ExtractAsyncTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {

            try {
                ComicUtil.archiveHelper(params[0], getApplicationContext());
            } catch (RarException e) {

                mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription("RarException in ExtractAsyncTask")
                        .build()
                );

            } catch (IOException e) {

                mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription("IOException in ExtractAsyncTask")
                        .build()
                );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.dismiss();
        }
    }


}
