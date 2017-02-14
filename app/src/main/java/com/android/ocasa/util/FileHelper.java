package com.android.ocasa.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

import static android.content.Context.MODE_APPEND;

/**
 * Created by ignacio on 07/02/17.
 */

public class FileHelper {

    private static FileHelper instance;
    private String FILE = "ocasa_log.txt";
    private File file;

    public static FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }

    public void init (Context context) {
        try {
            File path = context.getExternalFilesDir(null);
            file = new File(path, FILE);
        } catch (Exception e) {
            file = null;
        }
    }

    public void writeToFile(String data) {
        if (file == null) return;
        try {
            String date = DateTimeHelper.formatDateTime(new Date());
            date += " " + data + "\n";

            FileOutputStream stream = new FileOutputStream(file, true);
            try {
                stream.write(date.getBytes());
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            Log.e("FILE HELPER", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
