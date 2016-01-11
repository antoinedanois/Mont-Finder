package com.antrodev.montfinder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;

import java.util.List;

public class MainActivity extends Activity {


        SommetDatabaseHandler dbMan;
        List<Sommet> sommets;

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
        }




}
