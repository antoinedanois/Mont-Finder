package com.antrodev.montfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by antoinedanois on 11/01/2016.
 */
public class LocalisationGPS {
    static LocalisationGPS localisationGPS = null;
    static final String ACTION_LOCATION = "loc";
    Context appContext;
    LocationManager locationManager;

    private LocalisationGPS(Context appContext) {
        this.appContext = appContext;
        locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocalisationGPS get(Context context) {
        if (localisationGPS == null) {
            localisationGPS = new LocalisationGPS(context);
        }
        return localisationGPS;
    }

    public void startLocationUpdates() {
        try {
            LocationListener locl = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        System.out.println("Passage là aussi" + location.getLatitude());

                        Log.d(this.getClass().getName(), location.toString());
                        broadcastLocation(location);

                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            System.out.println("Passage là");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    System.out.println("Passage ici");

                    return;
                }else{
                    System.out.println("Coucou");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locl);
                }
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locl);
            }

            } catch (Exception e){
                Log.e("LocalisationGPS","Erreur securite" + e);
            }
        }

        private void broadcastLocation(Location location) {
            Intent broadcast = new Intent(ACTION_LOCATION);
            broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
            appContext.sendBroadcast(broadcast);
        }
    }
