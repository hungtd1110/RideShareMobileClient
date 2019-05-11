package com.example.admin.ridesharemobileclient.utils;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.config.App;

import java.util.Locale;

public class PlaceUtils {
    private static String TAG = "PlaceUtils";

    public static void setNamePosition(String startLatitude, String startLongitude, TextView view) {
        try {
            Geocoder geocoder = new Geocoder(App.getInstance(), Locale.getDefault());
            Address address = geocoder.getFromLocation(Double.parseDouble(startLatitude), Double.parseDouble(startLongitude),
                    1).get(0);

            view.setText(address.getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "setNamePosition: " + e.toString());
        }
    }
}
