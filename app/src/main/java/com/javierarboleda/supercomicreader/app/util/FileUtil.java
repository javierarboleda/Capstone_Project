package com.javierarboleda.supercomicreader.app.util;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javierarboleda on 6/3/16.
 */
public class FileUtil {

    public static String CBR_EXTENSION = "cbr";

    public static boolean hasCbrExtension(String path) {
        return getExtension(path).equalsIgnoreCase(CBR_EXTENSION);
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

    public static String getLastPathComponent(String path, boolean backSlash) {

        String[] pathSplit = backSlash ? path.split("\\\\") : path.split("/");

        return pathSplit[pathSplit.length - 1];
    }

    public static boolean isImage(String fileName) {
        String ext = getExtension(fileName);

        return (
                    ext.equalsIgnoreCase("jpg") ||
                    ext.equalsIgnoreCase("png") ||
                    ext.equalsIgnoreCase("jpeg")
               );
    }

    public static File convertDocumentUriPathToFile(String uriPath) {
        String[] splitUriPath = uriPath.split(":");

        String filePath = Environment.getExternalStorageDirectory() + "/" + splitUriPath[1];

        return new File(filePath);
    }

    static public File[] getFilesInDir(String dirPath) {
        File file = new File(dirPath);
        return file.listFiles();
    }

    static public List<String> getListOfImageFileNamesInDir(String dirPath) {

        ArrayList<String> imageFileNames = new ArrayList<>();

        for (File file : getFilesInDir(dirPath)) {
            if (isImage(file.getName())) {
                imageFileNames.add(file.getName());
            }
        }
        return imageFileNames;
    }
}
