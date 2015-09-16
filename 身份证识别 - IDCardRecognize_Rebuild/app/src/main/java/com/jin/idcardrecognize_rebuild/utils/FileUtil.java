package com.jin.idcardrecognize_rebuild.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static final String DST_FOLDER_NAME = "tmpPhotos/";
    private static final String FILE_SUFFIX = ".jpg";
    private static String storagePath = DST_FOLDER_NAME + "tmp" + FILE_SUFFIX;


    public static String initPath(String name) {
        if (!TextUtils.isEmpty(name)) {
            storagePath = DST_FOLDER_NAME + name + FILE_SUFFIX;
        }
        File file = new File(parentPath, storagePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return Uri.fromFile(file).getPath();
    }

    public static String saveBitmap(Bitmap bitmap, String name) {
        String path = initPath(name);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

}
