package com.antrodev.montfinder;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;

import java.util.List;

public class MainActivity extends Activity {


    SommetDatabaseHandler dbMan;
    List<Sommet> sommets;
    LocalisationGPS lGPS = null;
    BroadcastReceiver br;
    OrientationPrecise op;
    BroadcastReceiver orientationReceiver;
    float[] orientation;
    int xAxisDegrees;

    TextView tvLatLong = null;
    ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        tvLatLong = (TextView) findViewById(R.id.textViewLatLong);

        lGPS = LocalisationGPS.get(this.getApplicationContext());
        lGPS.startLocationUpdates();


        dbMan=SommetDatabaseHandler.getSommetDatabaseHandler(this);

        //dbMan.initializeValues();
        sommets=dbMan.getSommets();



        for(int i=0; i<sommets.size(); i++){
            System.out.println("FUHGQDFLJK " + sommets.get(i).getNomSommet());
        }


        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }


        op=OrientationPrecise.getOrientationPrecise(this);

        orientationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                orientation = new float[]{intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_0, 0), intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_1,0), intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_2, 0)};
                float xAxis = (float) (orientation[0]*180/Math.PI+90);
                System.out.println("Boussole = " + xAxisDegrees);
                tvLatLong.append("\nBoussole = " + xAxisDegrees);

                xAxisDegrees  = Math.round(xAxis);
            }
        };




        br=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showLocation(intent);
            }
        };
    }


    private void showLocation(Intent intent){
        Location loc =(Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(loc!=null){
            //Toast.makeText(getApplicationContext(), "Latitude = " + loc.getLatitude() + " Longitude = " + loc.getLongitude(), Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.INVISIBLE);
            tvLatLong.setText("Latitude = " + loc.getLatitude() + " Longitude = " + loc.getLongitude());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalisationGPS.get(this).startLocationUpdates();
        registerReceiver(br, new IntentFilter(LocalisationGPS.ACTION_LOCATION));
        tvLatLong = (TextView) findViewById(R.id.textViewLatLong);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvLatLong.setText("Localisation par GPS en cours...");

        registerReceiver(orientationReceiver, new IntentFilter(OrientationPrecise.MESSAGE_ORIENTATION));
        op.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);

        unregisterReceiver(orientationReceiver);
        op.stop();
    }







}
