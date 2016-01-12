package com.antrodev.montfinder;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends Activity {


    SommetDatabaseHandler dbMan;
    List<Sommet> sommets;
    LocalisationGPS lGPS = null;
    BroadcastReceiver br;
    OrientationPrecise op;
    BroadcastReceiver orientationReceiver;
    Location localisation = null;
    float[] orientation;
    int xAxisDegrees;

    ImageView ivArrowNord = null;
    ImageView ivArrow = null;
    ImageView ivArrow2 = null;
    ImageView ivArrow3 = null;
    ImageView ivArrow4 = null;
    ImageView ivArrow5 = null;

    TextView tvArrow1 = null;
    TextView tvArrow2 = null;
    TextView tvArrow3 = null;
    TextView tvArrow4 = null;
    TextView tvArrow5 = null;

    boolean verifArrow1 = false;
    boolean verifArrow2 = false;
    boolean verifArrow3 = false;
    boolean verifArrow4 = false;
    boolean verifArrow5 = false;

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




        if(dbMan.initializeValues()){
            initializeSommets();
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

                if(xAxis>180){
                    xAxis-=360;
                }

                //tvLatLong.setText("Boussole = " + xAxisDegrees);

                xAxisDegrees  = Math.round(xAxis);

                updateNord();
                updateSommets();

            }
        };




        br=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showLocation(intent);
            }
        };
    }

    private void initializeSommets(){
        sommets = dbMan.getSommets();

        for(int i=0; i<sommets.size(); i++){
            System.out.println("Sommets => " + sommets.get(i).getNomSommet());
        }
    }

    private void updateSommets(){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tailleEcran = size.x;

        int sizeImage = ivArrow.getWidth()/2;


        for(int i=0; i<sommets.size(); i++){



            Location loc = new Location(sommets.get(i).getNomSommet());

            loc.setLatitude(sommets.get(i).getLatitude());
            loc.setLongitude(sommets.get(i).getLongitude());


            if (localisation != null) {

                tvLatLong.setText("Boussole = " + xAxisDegrees);

                float locMontagne = localisation.bearingTo(loc);

                if(verifArrow1 == false){
                    verifArrow1 = true;
                    tvArrow1.setVisibility(View.VISIBLE);
                }else if(verifArrow2 == false){
                    verifArrow2 = true;
                    tvArrow2.setVisibility(View.VISIBLE);
                }else if(verifArrow3 == false){
                    verifArrow3 = true;
                    tvArrow3.setVisibility(View.VISIBLE);
                }else if(verifArrow4 == false){
                    verifArrow4 = true;
                    tvArrow4.setVisibility(View.VISIBLE);
                }else if(verifArrow5 == false){
                    verifArrow5 = true;
                    tvArrow5.setVisibility(View.VISIBLE);
                }


                if(xAxisDegrees-locMontagne >= -30 && xAxisDegrees+locMontagne <= 30) {
                    if (verifArrow1 == true) {
                        ivArrow.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow1.setText(sommets.get(i).getNomSommet());
                        tvArrow1.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));

                    }
                    if (verifArrow2 == true) {
                        ivArrow2.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow2.setText(sommets.get(i).getNomSommet());
                        tvArrow2.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                    }
                    if (verifArrow3 == true) {
                        ivArrow3.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow3.setText(sommets.get(i).getNomSommet());
                        tvArrow3.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                    }
                    if (verifArrow4 == true) {
                        ivArrow4.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow4.setText(sommets.get(i).getNomSommet());
                        tvArrow4.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                    }
                    if (verifArrow5 == true) {
                        ivArrow5.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow5.setText(sommets.get(i).getNomSommet());
                        tvArrow5.setX(((xAxisDegrees - locMontagne) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                    }
                }else{
                    if(verifArrow1 == true){
                        verifArrow1 = false;
                    }else if(verifArrow2 == true){
                        verifArrow2 = false;
                    }else if(verifArrow3 == true){
                        verifArrow3 = false;
                    }else if(verifArrow4 == true){
                        verifArrow4 = false;
                    }else if(verifArrow5 == true){
                        verifArrow5 = false;
                    }
                }


            }


        }

    }

    private void updateNord(){


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tailleEcran = size.x;

        int test = ivArrow.getWidth()/2;


        if(xAxisDegrees>-30 && xAxisDegrees<30){
            ivArrowNord.setX((xAxisDegrees*(-tailleEcran/60))+((tailleEcran/2)-test));
        }

    }


    private void showLocation(Intent intent){
        Location loc =(Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(loc!=null){
            //Toast.makeText(getApplicationContext(), "Latitude = " + loc.getLatitude() + " Longitude = " + loc.getLongitude(), Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.INVISIBLE);
            tvLatLong.setText("Latitude = " + loc.getLatitude() + " Longitude = " + loc.getLongitude());
            localisation = loc;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalisationGPS.get(this).startLocationUpdates();
        registerReceiver(br, new IntentFilter(LocalisationGPS.ACTION_LOCATION));
        tvLatLong = (TextView) findViewById(R.id.textViewLatLong);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivArrowNord = (ImageView) findViewById(R.id.imageViewArrowNord);

        ivArrow = (ImageView) findViewById(R.id.imageViewArrow);
        ivArrow2 = (ImageView) findViewById(R.id.imageViewArrow2);
        ivArrow3 = (ImageView) findViewById(R.id.imageViewArrow3);
        ivArrow4 = (ImageView) findViewById(R.id.imageViewArrow4);
        ivArrow5 = (ImageView) findViewById(R.id.imageViewArrow5);

        tvArrow1 = (TextView) findViewById(R.id.textViewMont1);
        tvArrow2 = (TextView) findViewById(R.id.textViewMont2);
        tvArrow3 = (TextView) findViewById(R.id.textViewMont3);
        tvArrow4 = (TextView) findViewById(R.id.textViewMont4);
        tvArrow5 = (TextView) findViewById(R.id.textViewMont5);

        tvArrow1.bringToFront();
        tvArrow2.bringToFront();
        tvArrow3.bringToFront();
        tvArrow4.bringToFront();
        tvArrow5.bringToFront();


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
