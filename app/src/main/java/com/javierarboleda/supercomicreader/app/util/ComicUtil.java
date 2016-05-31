package com.javierarboleda.supercomicreader.app.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

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



    public static void archiveHelper(String filePath, Context context) {

        File file = new File(filePath);

        Archive archive = null;

        try {
            archive = new Archive(file);
        } catch (RarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (archive != null) {
            archive.getMainHeader().print();
            FileHeader fh = archive.nextFileHeader();

            String[] filePathDirs = filePath.split("/");

            String folderName = getFileNameWithoutExtension(filePathDirs[filePathDirs.length - 1]);

            String path = Environment.getExternalStorageDirectory().getPath() +
                    "/Android/data/SuperComicReader/" + folderName + "/";

            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            while (fh != null) {
                FileOutputStream os;

                String fileName = fh.getFileNameString().trim();

                File outFile = new File(path + "/" + fileName);

                if (!isImage(fileName)) {
                    fh = archive.nextFileHeader();
                    continue;
                }

                try {
                    os = new FileOutputStream(outFile);
                    archive.extractFile(fh, os);
                    os.close();

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
                fh = archive.nextFileHeader();
            }

            if (folder.exists()) {
                for (File f : folder.listFiles()) {

                    Log.d("ComicUtil", f.toString());
                }
            }

            Log.d("ComicUtil", folder.toString() + " " + folder.length());

        }
    }

    public static String getExtension(String fileName) {
        String[] fileNameSplit = fileName.split("\\.");

        if (fileNameSplit.length > 0) {
            return fileNameSplit[fileNameSplit.length - 1];
        }

        return "";
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (index > 0) {
            return fileName.substring(0, index);
        }

        return fileName;
    }

    public static boolean isImage(String fileName) {
        String ext = getExtension(fileName);
        return ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png");
    }

}
