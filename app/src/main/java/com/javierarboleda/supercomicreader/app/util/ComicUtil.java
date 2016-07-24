package com.javierarboleda.supercomicreader.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.javierarboleda.supercomicreader.app.data.ComicContract;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by javierarboleda on 5/28/16.
 *
 *
 */
public class ComicUtil {

    public static void archiveHelper(File file, Context context) throws RarException, IOException {

//        File file = new File(filePath);

        Archive archive = null;

        archive = new Archive(file);

        if (archive != null) {
            int numOfPages = 0;

            String coverImageName = null;

            archive.getMainHeader().print();

            FileHeader fh = archive.nextFileHeader();

//            String[] filePathDirs = filePath.split("/");
            String[] filePathDirs = file.getPath().split("/");

            String folderName =
                    FileUtil.getFileNameWithoutExtension(filePathDirs[filePathDirs.length - 1]);

            String path = "/Comics/SuperComicReader/" + folderName + "/";
            String absolutePath = Environment.getExternalStorageDirectory().getPath() + path;

            File folder = new File(absolutePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            while (fh != null) {
                FileOutputStream os;

                String fileName =
                        FileUtil.getLastPathComponent(fh.getFileNameString().trim(), true);

                File outFile = new File(absolutePath + "/" + fileName);

                if (!FileUtil.isImage(fileName)) {
                    fh = archive.nextFileHeader();
                    continue;
                }

                if (coverImageName == null) {
                    coverImageName = FileUtil.getLastPathComponent(fileName, true);
                }

                os = new FileOutputStream(outFile);
                archive.extractFile(fh, os);
                os.close();
                numOfPages++;

                Log.d("ComicUtil", outFile.getPath());

                fh = archive.nextFileHeader();
            }

            // write comic to database
            // todo place in method
            ContentValues comicValues = new ContentValues();
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_TITLE, folderName);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_FILE, path);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_COVER, coverImageName);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_PAGES, numOfPages);

            context.getContentResolver().insert(ComicContract.ComicEntry.CONTENT_URI, comicValues);

        }
    }

    public static void unzipHelper(File file, Context context) throws ZipException {

        String[] filePathDirs = file.getPath().split("/");
        String folderName =
                FileUtil.getFileNameWithoutExtension(filePathDirs[filePathDirs.length - 1]);
        String path = "/Comics/SuperComicReader/" + folderName + "/";
        String absolutePath = Environment.getExternalStorageDirectory().getPath() + path;

        String source = file.getPath();
        String destination = absolutePath;

        File folder = new File(absolutePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        ZipFile zipFile = new ZipFile(source);
        zipFile.extractAll(destination);

        // get the number of images (comic pages)

        String coverImageName = null;
        int numOfPages = 0;

        File comicFolderFileName = new File(destination);

        File[] files = comicFolderFileName.listFiles();

        for (File f : files) {

            String fileName = FileUtil.getLastPathComponent(f.getPath(), false);

            if (!FileUtil.isImage(fileName)) {
                continue;
            }

            if (coverImageName == null) {
                coverImageName = fileName;
            }

            Log.d("ComicUtil", fileName);

            numOfPages++;
        }

        // if coverImageName == null, then there were no images in the zip file... DANGER!
        if (coverImageName != null) {
            // write comic to database
            // todo place in method
            ContentValues comicValues = new ContentValues();
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_TITLE, folderName);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_FILE, path);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_COVER, coverImageName);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_PAGES, numOfPages);

            context.getContentResolver().insert(ComicContract.ComicEntry.CONTENT_URI, comicValues);
        }

    }

}
