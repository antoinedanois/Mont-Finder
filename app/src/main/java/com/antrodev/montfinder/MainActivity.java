package com.antrodev.montfinder;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;
import com.antrodev.montfinder.db.SommetInsertionTask;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "ANGLECAMERASHAREDPREF";

    SommetDatabaseHandler dbMan;
    List<Sommet> sommets = new ArrayList<>();
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
    Animation animationParametre = null;
    Animation animationParametreFadeOut = null;
    Animation animationParametreFadeIn = null;
    public int valeurAngleCamera = 60;


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
    SeekBar seekBarAngle = null;
    ImageView parametreAngle = null;
    TextView tvAngle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        tvLatLong = (TextView) findViewById(R.id.textViewLatLong);



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        valeurAngleCamera = settings.getInt("angleCamera", 60);



        lGPS = LocalisationGPS.get(this.getApplicationContext());
        lGPS.startLocationUpdates();


        dbMan=SommetDatabaseHandler.getSommetDatabaseHandler(this);


        if(dbMan.initializeValues()){
            initializeSommets();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sommets.isEmpty()){
                    dbMan=SommetDatabaseHandler.getSommetDatabaseHandler(getParent());
                    if(dbMan.initializeValues()){
                        initializeSommets();
                    }
                }
            }
        }, 4000);




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
                if(sommets!=null) {
                    updateSommets();
                }

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



                tvLatLong.setText(getCompass());


                if (!verifArrow1) {
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
                } else if (!verifArrow2) {

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

                } else if (!verifArrow3) {
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
                } else if (!verifArrow4) {
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

                } else if (!verifArrow5) {
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
                    if (verifArrow1 && passageAutorise1) {
                        ivArrow.setX(((xAxisDegrees - locMontagne1) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow1.setText("1 = " + sommets.get(0).getNomSommet());
                        tvArrow1.setX(((xAxisDegrees - locMontagne1) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        //System.out.println("1 = " + sommets.get(i).getNomSommet());
                        passageAutorise1 = false;
                    } else if (verifArrow2 && passageAutorise2) {
                        ivArrow2.setX(((xAxisDegrees - locMontagne2) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow2.setText("2 = " + sommets.get(i).getNomSommet());
                        tvArrow2.setX(((xAxisDegrees - locMontagne2) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        passageAutorise2 = false;
                    } else if (verifArrow3 && passageAutorise3) {
                        ivArrow3.setX(((xAxisDegrees - locMontagne3) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow3.setText("3 = " + sommets.get(i).getNomSommet());
                        tvArrow3.setX(((xAxisDegrees - locMontagne3) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        passageAutorise3 = false;
                    } else if (verifArrow4 && passageAutorise4) {
                        ivArrow4.setX(((xAxisDegrees - locMontagne4) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow4.setText("4 = " + sommets.get(i).getNomSommet());
                        tvArrow4.setX(((xAxisDegrees - locMontagne4) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));

                        passageAutorise4 = false;
                    } else if (verifArrow5 && passageAutorise5) {
                        ivArrow5.setX(((xAxisDegrees - locMontagne5) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));
                        tvArrow5.setText("5 = " + sommets.get(i).getNomSommet());
                        tvArrow5.setX(((xAxisDegrees - locMontagne5) * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - sizeImage));

                        passageAutorise5 = false;
                        tourComplet = true;
                    }
                }

                if (tourComplet) {
                    if (verifArrow1) {
                        verifArrow1 = false;
                        passageAutorise1 = false;
                    }
                    if (verifArrow2) {
                        verifArrow2 = false;
                        passageAutorise2 = false;
                    }
                    if (verifArrow3) {
                        passageAutorise3 = false;
                        verifArrow3 = false;
                    }
                    if (verifArrow4) {
                        passageAutorise4 = false;
                        verifArrow4 = false;
                    }
                    if (verifArrow5) {
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

            ivArrowNord.setImageResource(R.drawable.arrownord);

            ivArrowNord.setRotation(0);
        }

        if(xAxisDegrees>=-29 && xAxisDegrees<=29){
            ivArrowNord.setX((xAxisDegrees * (-tailleEcran / valeurAngleCamera)) + ((tailleEcran / 2) - test));
            animationNordCote = null;
            ivArrowNord.setImageResource(R.drawable.arrownord);

            ivArrowNord.setRotation(0);
        }

        if(xAxisDegrees<=-30 && animationNordCote == null){



            animationNordCote = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.floatingnordcote);
            animationNordCote.setRepeatCount(-1);
            animationNordCote.setRepeatMode(2);
            ivArrowNord.startAnimation(animationNordCote);


            ivArrowNord.setImageResource(R.drawable.arrownordcote);
            ivArrowNord.setRotation(0);
            animationNord = null;
        }

        if(xAxisDegrees>=30 && animationNordCote == null){


            animationNordCote= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.floatingnordcote);
            animationNordCote.setRepeatCount(-1);
            animationNordCote.setRepeatMode(2);
            ivArrowNord.startAnimation(animationNordCote);


            ivArrowNord.setImageResource(R.drawable.arrownordcote);
            ivArrowNord.setRotation(180);
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
        if(lancement) {
            ivLogo = (ImageView) findViewById(R.id.imageViewIconeLancement);

            lancementAnimIconeLancement = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.iconelancement);
            ivLogo.setAnimation(lancementAnimIconeLancement);
            lancement = false;
        }
    }

    protected void animationParametre(){
        animationParametre = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotatesetting);
        animationParametreFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        animationParametreFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        parametreAngle.setAnimation(animationParametre);

        if(tvAngle.getVisibility() == View.INVISIBLE){
            tvAngle.setText("Angle de la caméra : " + valeurAngleCamera + "°");
            seekBarAngle.setProgress(valeurAngleCamera - 40);
            seekBarAngle.setAnimation(animationParametreFadeIn);
            tvAngle.setAnimation(animationParametreFadeIn);


            seekBarAngle.setVisibility(View.VISIBLE);
            tvAngle.setVisibility(View.VISIBLE);
        }else{
            seekBarAngle.setAnimation(animationParametreFadeOut);
            tvAngle.setAnimation(animationParametreFadeOut);


            seekBarAngle.setVisibility(View.INVISIBLE);
            tvAngle.setVisibility(View.INVISIBLE);
        }
    }

    private String getCompass() {
        String degrees="";
        String dir="N";
        int absValDegrees=xAxisDegrees;

        if(xAxisDegrees>5){
            dir="NE";
            if(xAxisDegrees>85){
                dir="SE";
                if(xAxisDegrees<95){
                    dir="E";
                }
                if(xAxisDegrees>168){
                    dir="S";
                }
            }
        } else if(xAxisDegrees<-5){
            dir="NO";
            if(xAxisDegrees<-85){
                dir="SO";
                if(xAxisDegrees>-95){
                    dir="O";
                }
                if(xAxisDegrees<-168){
                    dir="S";
                }
            }
        }

        if(absValDegrees<0){
            absValDegrees=-absValDegrees;
        }

        degrees=absValDegrees+"° "+dir;

        return degrees;
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

        seekBarAngle = (SeekBar) findViewById(R.id.seekBarAngle);
        tvAngle = (TextView) findViewById(R.id.textViewAngleCamera);
        parametreAngle = (ImageView) findViewById(R.id.imageViewAccesReglage);


        parametreAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animationParametre();


            }
        });


        seekBarAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChange = 0;
            int prog = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChange = progress+40;
                tvAngle.setText("Angle de la caméra : " + progressChange + "°");
                valeurAngleCamera = progressChange;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prog = progressChange;
                tvAngle.setText("Angle de la caméra : " + prog + "°");
                valeurAngleCamera = prog;
            }
        });



        tvLatLong.setText("Localisation par GPS en cours...");

        registerReceiver(orientationReceiver, new IntentFilter(OrientationPrecise.MESSAGE_ORIENTATION));
        registerReceiver(DBStatusReceiver, new IntentFilter(SommetInsertionTask.MESSAGE_TYPE));
        op.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
                return;
            }
        }
    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new ConfirmationDialog().onCreateDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("angleCamera", valeurAngleCamera);

        // Commit the edits!
        editor.commit();

        unregisterReceiver(orientationReceiver);
        unregisterReceiver(DBStatusReceiver);
        op.stop();
    }

}

