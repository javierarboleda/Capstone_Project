package com.javierarboleda.supercomicreader.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.javierarboleda.supercomicreader.app.data.ComicContract;

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

    public static void archiveHelper(File file, Context context) {

//        File file = new File(filePath);

        Archive archive = null;

        try {
            archive = new Archive(file);
        } catch (RarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (archive != null) {
            int numOfPages = 0;

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

                String fileName = fh.getFileNameString().trim();

                File outFile = new File(absolutePath + "/" + fileName);

                if (!FileUtil.isImage(fileName)) {
                    fh = archive.nextFileHeader();
                    continue;
                }

                try {
                    os = new FileOutputStream(outFile);
                    archive.extractFile(fh, os);
                    os.close();
                    numOfPages++;

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (RarException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("ComicUtil", outFile.getPath());

                fh = archive.nextFileHeader();
            }

            // write comic to database
            // todo place in method
            ContentValues comicValues = new ContentValues();
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_TITLE, folderName);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_FILE, path);
            comicValues.put(ComicContract.ComicEntry.COLUMN_NAME_PAGES, numOfPages);

            context.getContentResolver().insert(ComicContract.ComicEntry.CONTENT_URI, comicValues);

        }
    }

}
