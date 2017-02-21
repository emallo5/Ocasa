package com.android.ocasa.util;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import static android.support.v4.graphics.BitmapCompat.getAllocationByteCount;

public class MediaUtils {

    public static String convertMediaToBase64(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


//        int bytes = getAllocationByteCount(bm);
//        ByteBuffer buffer = ByteBuffer.allocate(bytes);
//        bm.copyPixelsToBuffer(buffer);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}