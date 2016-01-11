package com.antrodev.montfinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;

import java.util.List;

public class MainActivity extends Activity {


    SommetDatabaseHandler dbMan;
    List<Sommet> sommets;
    OrientationPrecise op;
    BroadcastReceiver orientationReceiver;
    float[] orientation;
    float xAxisDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        dbMan=SommetDatabaseHandler.getSommetDatabaseHandler(this);
        dbMan.initializeValues();
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
        orientationReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                orientation=new float[]{intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_0,0),intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_1,0),intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_2,0)};
                xAxisDegrees= (float) (orientation[0]*180/Math.PI+90);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(orientationReceiver, new IntentFilter(OrientationPrecise.MESSAGE_ORIENTATION));
        op.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(orientationReceiver);
        op.stop();
    }


}
