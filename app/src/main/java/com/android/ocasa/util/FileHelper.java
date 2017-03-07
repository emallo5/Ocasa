package com.android.ocasa.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.ocasa.map.MapsActivity;
import com.android.ocasa.model.LocationListByDay;
import com.android.ocasa.model.Site;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_APPEND;

/**
 * Created by ignacio on 07/02/17.
 */

public class FileHelper {

    private static FileHelper instance;
    private String FILE = "ocasa_log.txt";
    private String LOC_LOG;
    private File file;
    private Context context;

    public static FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }

    public void init (Context context) {
        this.context = context;
        LOC_LOG = DateTimeHelper.formatDate(new Date());
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

    public LocationListByDay readLocation() {
        LocationListByDay list = new LocationListByDay();

        File dir = new File(context.getExternalFilesDir(null).getAbsolutePath());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {

                if (children[i].equals(FILE)) continue;

                ArrayList<Site> sites = new ArrayList<>();
                File f = new File(dir, children[i]);

                try {
                    FileInputStream inputStream = new FileInputStream(f);

                    if ( inputStream != null ) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString;

                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            String[] loc = receiveString.split(" ");
                            sites.add(new Site(loc[0], Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
                        }
                        inputStream.close();
                    }
                }
                catch (Exception e) {
                    Log.e("write location", e.toString());
                }
                list.addLocationList(children[i].substring(0, 10), sites);
            }
        }

        return list;
    }

    public void saveLocation (String data) {
        LOC_LOG = DateTimeHelper.formatDate(new Date()) + ".txt";
        LOC_LOG = LOC_LOG.replace("/", "-");

        File f = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + LOC_LOG);
        if (!f.exists()) {
            File path = context.getExternalFilesDir(null);
            f = new File(path, LOC_LOG);
        }

        Log.d("LocationService", data);

        try {
            String date = DateTimeHelper.formatTime(new Date());
            date += " " + data + "\n";

            FileOutputStream stream = new FileOutputStream(f, true);
            try {
                stream.write(date.getBytes());
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            Log.e("FILE HELPER", "File write location: " + e.toString());
        }
    }

}