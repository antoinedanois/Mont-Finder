package com.antrodev.montfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.antrodev.montfinder.db.Sommet;
import com.antrodev.montfinder.db.SommetDatabaseHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SommetDatabaseHandler dbMan;
    List<Sommet> sommets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbMan=SommetDatabaseHandler.getSommetDatabaseHandler(this);
        dbMan.initializeValues();
        sommets=dbMan.getSommets();
    }
}
