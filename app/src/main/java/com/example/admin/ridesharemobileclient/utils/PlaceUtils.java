package com.example.admin.ridesharemobileclient.utils;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.entity.RouteStep;

import java.util.List;
import java.util.Locale;

public class PlaceUtils {
    private static String TAG = "PlaceUtils";

    public static void setFullNamePosition(String startLatitude, String startLongitude, TextView view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(App.getInstance(), Locale.getDefault());
                    Address address = geocoder.getFromLocation(Double.parseDouble(startLatitude), Double.parseDouble(startLongitude),
                            1).get(0);

                    view.setText(address.getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setFullNamePosition(String startLatitude, String startLongitude, TextView view, ProgressDialog progressDialog) {
        progressDialog.show();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(App.getInstance(), Locale.getDefault());
                    Address address = geocoder.getFromLocation(Double.parseDouble(startLatitude), Double.parseDouble(startLongitude),
                            1).get(0);

                    view.setText(address.getAddressLine(0));
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public static void setNamePosition(String startLatitude, String startLongitude, TextView view) {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Geocoder geocoder = new Geocoder(App.getInstance(), Locale.getDefault());
//                    Address address = geocoder.getFromLocation(Double.parseDouble(startLatitude), Double.parseDouble(startLongitude),
//                            1).get(0);
//
//                    view.setText(address.getThoroughfare() +  ", " +
//                            address.getSubAdminArea() + ", " +
//                            address.getAdminArea() + ", " +
//                            address.getCountryName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public static void setListNamePosition(List<RouteStep> listRouteStep, TextView view, ProgressDialog progressDialog) {
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(App.getInstance(), Locale.getDefault());

                    String route = "";
                    for (int i = 0 ; i < listRouteStep.size() ; i++) {
                        RouteStep routeStep = listRouteStep.get(i);
                        Address address = geocoder.getFromLocation(Double.parseDouble(routeStep.getLatitude()),
                                Double.parseDouble(routeStep.getLongitude()),
                                1).get(0);

                        route += (i + 1) + ". " + address.getAddressLine(0);
                        if (i != listRouteStep.size() - 1) {
                            route += "\n\n";
                        }
                    }

                    view.setText(route);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
