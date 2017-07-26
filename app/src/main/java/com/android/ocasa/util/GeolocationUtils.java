package com.android.ocasa.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leandro on 24/7/17.
 */

public class GeolocationUtils {

    public static void gotoWasePosition(Context context) {
        try {
            String url = "https://waze.com/ul?q=Hawaii";
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            context.startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  ) {
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            context.startActivity(intent);
        }
    }

    public static LatLng addressToLocation(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            address = coder.getFromLocationName(inputtedAddress, 5);

            if (address == null)
                return null;
            else if (address.size() == 0)
                return null;

//            for (int i=0; i<address.size(); i++) {
//                address.get(i).getAddressLine(i) + ", " + address.get(i).getLocality();
//            }

            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return resLatLng;
    }

    public static void ShowListResult(Context context, ArrayList<String> list) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);

        builder.setTitle("Selecciones la opción que desea")
                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        return;
                    }
                });

        builder.create().show();
    }
}
