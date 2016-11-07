package com.android.ocasa.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ignacio on 04/06/2015.
 */
public class AppCache {

    public static File getDiskCacheDir(Context context, String uniqueName) {

        final String cachePath;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cachePath = context.getExternalCacheDir() == null ? "" : context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    public static void copyAndDeleteFile(File fromFile, File toFile) throws IOException{

        InputStream inStream = null;
        OutputStream outStream = null;

        inStream = new FileInputStream(fromFile);
        outStream = new FileOutputStream(toFile);

        byte[] buffer = new byte[1024];

        int length;
        //copy the file content in bytes
        while ((length = inStream.read(buffer)) > 0){
            outStream.write(buffer, 0, length);
        }

        inStream.close();
        outStream.close();

        //delete the original file
        fromFile.delete();
    }
}
