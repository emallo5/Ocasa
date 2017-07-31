package com.android.ocasa.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, Address> addressToLocation(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        HashMap<String, Address> addressHashMap = new HashMap<>();

        try {
            address = coder.getFromLocationName(inputtedAddress, 5);

            if (address == null)
                return null;
            else if (address.size() == 0)
                return null;

            for (int i=0; i<address.size(); i++) {
                addressHashMap.put(address.get(i).getAddressLine(0) + ", " + address.get(i).getLocality(), address.get(i));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return addressHashMap;
    }

    public static void ShowListResult(final Fragment context, String address) {

        final Map<String, Address> list = addressToLocation(context.getContext(), address);

        if (list == null || list.isEmpty()) {
            AlertDialogFragment.newInstance("Error de localización", "No se pude resolver la dirección").show(context.getChildFragmentManager(), "Map");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());

        final CharSequence[] cs = list.keySet().toArray(new CharSequence[list.size()]);

        builder.setTitle("Seleccione la opción que desea")
                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + list.get(cs[item]).getLatitude() + ", " + list.get(cs[item]).getLongitude() + "&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(context.getActivity().getPackageManager()) != null)
                            context.startActivity(mapIntent);
                        else
                            AlertDialogFragment.newInstance("Aplicación GoogleMaps faltante", "Debe instalar o actualizar GoogleMaps").show(context.getChildFragmentManager(), "MapApp");
                    }
                });

        builder.create().show();
    }
}
