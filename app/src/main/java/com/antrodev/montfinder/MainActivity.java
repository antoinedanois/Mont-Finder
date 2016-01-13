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
import android.media.Image;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;
import com.antrodev.montfinder.db.SommetInsertionTask;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends Activity {


    SommetDatabaseHandler dbMan;
    List<Sommet> sommets;
    BroadcastReceiver DBStatusReceiver;
    LocalisationGPS lGPS = null;
    BroadcastReceiver br;
    OrientationPrecise op;
    BroadcastReceiver orientationReceiver;
    Location localisation = null;
    float[] orientation;
    int xAxisDegrees;
    Animation animationNord = null;
    Animation animationNordCote = null;
    Animation lancementAnimIconeLancement = null;


    ImageView ivArrowNord = null;
    ImageView ivArrow = null;
    ImageView ivArrow2 = null;
    ImageView ivArrow3 = null;
    ImageView ivArrow4 = null;
    ImageView ivArrow5 = null;
    ImageView ivLogo = null;


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
    boolean lancement = true;

    float locMontagne1 = 0;
    float locMontagne2 = 0;
    float locMontagne3 = 0;
    float locMontagne4 = 0;
    float locMontagne5 = 0;

    TextView tvLatLong = null;
    ProgressBar progressBar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        tvLatLong = (TextView) findViewById(R.id.textViewLatLong);



        //lancementAnimIconeLancement = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.iconelancement);

        //ivAnimationLancement.startAnimation(lancementAnimIconeLancement);



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

        DBStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initializeSommets();
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

    private void updateSommets() {



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tailleEcran = size.x;

        boolean passageAutorise1 = false;
        boolean passageAutorise2 = false;
        boolean passageAutorise3 = false;
        boolean passageAutorise4 = false;
        boolean passageAutorise5 = false;
        boolean placeLibre = false;
        boolean tourComplet = false;

        int sizeImage = ivArrow.getWidth() / 2;


        for (int i = 0; i < sommets.size(); i++) {


            if (localisation != null) {

                tvLatLong.setText("Boussole = " + xAxisDegrees);


                if (verifArrow1 == false) {
                    Location loc1 = new Location(sommets.get(i).getNomSommet());


                    loc1.setLatitude(sommets.get(i).getLatitude());
                    loc1.setLongitude(sommets.get(i).getLongitude());

                    locMontagne1 = localisation.bearingTo(loc1);

                    if (xAxisDegrees - locMontagne1 >= -30 && xAxisDegrees + locMontagne1 <= 30) {

                        verifArrow1 = true;
                        ivArrow.setVisibility(View.VISIBLE);
                        tvArrow1.setVisibility(View.VISIBLE);

                        passageAutorise1 = true;
                    } else {
                        placeLibre = true;
                    }
                } else if (verifArrow2 == false) {

                    Location loc2 = new Location(sommets.get(i).getNomSommet());


                    loc2.setLatitude(sommets.get(i).getLatitude());
                    loc2.setLongitude(sommets.get(i).getLongitude());

                    locMontagne2 = localisation.bearingTo(loc2);

                    if (xAxisDegrees - locMontagne2 >= -30 && xAxisDegrees + locMontagne2 <= 30) {

                        verifArrow2 = true;
                        ivArrow2.setVisibility(View.VISIBLE);
                        tvArrow2.setVisibility(View.VISIBLE);
                        passageAutorise2 = true;
                    } else {
                        placeLibre = true;
                    }

                } else if (verifArrow3 == false) {
                    Location loc3 = new Location(sommets.get(i).getNomSommet());


                    loc3.setLatitude(sommets.get(i).getLatitude());
                    loc3.setLongitude(sommets.get(i).getLongitude());

                    locMontagne3 = localisation.bearingTo(loc3);

                    if (xAxisDegrees - locMontagne3 >= -30 && xAxisDegrees + locMontagne3 <= 30) {
                        verifArrow3 = true;
                        ivArrow3.setVisibility(View.VISIBLE);
                        tvArrow3.setVisibility(View.VISIBLE);
                        passageAutorise3 = true;
                    } else {
                        placeLibre = true;
                    }
                } else if (verifArrow4 == false) {
                    Location loc4 = new Location(sommets.get(i).getNomSommet());


                    loc4.setLatitude(sommets.get(i).getLatitude());
                    loc4.setLongitude(sommets.get(i).getLongitude());

                    locMontagne4 = localisation.bearingTo(loc4);


                    if (xAxisDegrees - locMontagne4 >= -30 && xAxisDegrees + locMontagne4 <= 30) {

                        verifArrow4 = true;
                        ivArrow4.setVisibility(View.VISIBLE);
                        tvArrow4.setVisibility(View.VISIBLE);
                        passageAutorise4 = true;
                    } else {
                        placeLibre = true;
                    }

                } else if (verifArrow5 == false) {
                    Location loc5 = new Location(sommets.get(i).getNomSommet());


                    loc5.setLatitude(sommets.get(i).getLatitude());
                    loc5.setLongitude(sommets.get(i).getLongitude());

                    locMontagne5 = localisation.bearingTo(loc5);

                    if (xAxisDegrees - locMontagne5 >= -30 && xAxisDegrees + locMontagne5 <= 30) {
                        verifArrow5 = true;
                        ivArrow5.setVisibility(View.VISIBLE);
                        tvArrow5.setVisibility(View.VISIBLE);
                        passageAutorise5 = true;
                    } else {
                        placeLibre = true;
                    }

                }


                if(verifArrow1 || verifArrow2 || verifArrow3 || verifArrow4 || verifArrow5){
                    placeLibre = false;
                }



                if (placeLibre == false) {
                    if (verifArrow1 == true && passageAutorise1 == true) {
                        ivArrow.setX(((xAxisDegrees - locMontagne1) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow1.setText("1 = " + sommets.get(0).getNomSommet());
                        tvArrow1.setX(((xAxisDegrees - locMontagne1) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        //System.out.println("1 = " + sommets.get(i).getNomSommet());
                        passageAutorise1 = false;
                    } else if (verifArrow2 == true && passageAutorise2 == true) {
                        ivArrow2.setX(((xAxisDegrees - locMontagne2) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow2.setText("2 = " + sommets.get(i).getNomSommet());
                        tvArrow2.setX(((xAxisDegrees - locMontagne2) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        passageAutorise2 = false;

                        //System.out.println("2 = " + sommets.get(23).getNomSommet());
                    } else if (verifArrow3 == true && passageAutorise3 == true) {
                        ivArrow3.setX(((xAxisDegrees - locMontagne3) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow3.setText("3 = " + sommets.get(i).getNomSommet());
                        tvArrow3.setX(((xAxisDegrees - locMontagne3) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        passageAutorise3 = false;
                    } else if (verifArrow4 == true && passageAutorise4 == true) {
                        ivArrow4.setX(((xAxisDegrees - locMontagne4) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow4.setText("4 = " + sommets.get(i).getNomSommet());
                        tvArrow4.setX(((xAxisDegrees - locMontagne4) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));

                        passageAutorise4 = false;
                    } else if (verifArrow5 == true && passageAutorise5 == true) {
                        ivArrow5.setX(((xAxisDegrees - locMontagne5) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow5.setText("5 = " + sommets.get(i).getNomSommet());
                        tvArrow5.setX(((xAxisDegrees - locMontagne5) * (-tailleEcran / 60)) + ((tailleEcran / 2) - sizeImage));

                        passageAutorise5 = false;
                        tourComplet = true;
                    }
                }

                if (tourComplet == true) {
                    if (verifArrow1 == true) {
                        verifArrow1 = false;
                        passageAutorise1 = false;
                    }
                    if (verifArrow2 == true) {
                        verifArrow2 = false;
                        passageAutorise2 = false;
                    }
                    if (verifArrow3 == true) {
                        passageAutorise3 = false;
                        verifArrow3 = false;
                    }
                    if (verifArrow4 == true) {
                        passageAutorise4 = false;
                        verifArrow4 = false;
                    }
                    if (verifArrow5 == true) {
                        verifArrow5 = false;
                        passageAutorise5 = false;
                    }
                    tourComplet = false;
                }
            }


                }

    }

    private void updateNord(){


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tailleEcran = size.x;

        int test = ivArrowNord.getWidth()/2;

        if(xAxisDegrees>=-30 && xAxisDegrees<30 && animationNord == null) {
            animationNord = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.floatingnorddroit);
            animationNord.setRepeatCount(-1);
            animationNord.setRepeatMode(2);
            ivArrowNord.startAnimation(animationNord);
            animationNordCote= null;
            ivArrowNord.setRotation(0);

        }

        if(xAxisDegrees>=-29 && xAxisDegrees<=29){
            ivArrowNord.setX((xAxisDegrees * (-tailleEcran / 60)) + ((tailleEcran / 2) - test));
            animationNordCote = null;
            ivArrowNord.setRotation(0);
        }

        if(xAxisDegrees<=-30 && animationNordCote == null){



            animationNordCote = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.floatingnordcote);
            animationNordCote.setRepeatCount(-1);
            animationNordCote.setRepeatMode(2);
            ivArrowNord.startAnimation(animationNordCote);

            ivArrowNord.setRotation(-90);

            animationNord = null;
        }

        if(xAxisDegrees>=30 && animationNordCote == null){



            animationNordCote= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.floatingnordcote);
            animationNordCote.setRepeatCount(-1);
            animationNordCote.setRepeatMode(2);
            ivArrowNord.startAnimation(animationNordCote);

            ivArrowNord.setRotation(90);

            animationNord = null;
        }

        animationLancement();

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

    protected void animationLancement(){
        if(lancement == true) {
            ivLogo = (ImageView) findViewById(R.id.imageViewIconeLancement);

            lancementAnimIconeLancement = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.iconelancement);
            ivLogo.setAnimation(lancementAnimIconeLancement);
            lancement = false;
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


        tvLatLong.setText("Localisation par GPS en cours...");

        registerReceiver(orientationReceiver, new IntentFilter(OrientationPrecise.MESSAGE_ORIENTATION));
        registerReceiver(DBStatusReceiver, new IntentFilter(SommetInsertionTask.MESSAGE_TYPE));
        op.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);

        unregisterReceiver(orientationReceiver);
        unregisterReceiver(DBStatusReceiver);
        op.stop();
    }







}
