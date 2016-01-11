package com.antrodev.montfinder.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 11/01/2016.
 */
public class SommetDatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sommetsManager";
    private static final String TABLE_SOMMETS = "sommets";

    private static final String KEY_ID="id";
    private static final String KEY_LONGITUDE="longitude";
    private static final String KEY_LATITUDE="latitude";
    private static final String KEY_NOM="nom";
    private static final String KEY_ALTITUDE="altitude";
    public static SommetDatabaseHandler gbs = null;

    public static boolean created = false;
    public static boolean initialized = false;


    public static SommetDatabaseHandler getSommetDatabaseHandler(Context context) {
        if (created == false){
            gbs = new SommetDatabaseHandler(context);
            created = true;
        }
        return gbs;
    }

    private SommetDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.onCreate(this.getWritableDatabase());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_SOMMETS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SOMMETS + " ("
                + KEY_ID + " INT PRIMARY KEY,"
                + KEY_LONGITUDE + " varchar(16),"
                + KEY_LATITUDE + " varchar(16),"
                + KEY_NOM + " varchar,"
                + KEY_ALTITUDE + " int)";

        db.execSQL(CREATE_SOMMETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOMMETS);
        System.out.println("YOLOOOOO");
        onCreate(db);
        initialized = false;
    }

    private void addSommet(String correctQuery) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        db.execSQL(correctQuery);
        db.close(); // Closing database connection
    }

    public void initializeValues(){
        if(!initialized){
            addSommet("INSERT INTO sommets VALUES (1116621810, '6.84486789904718', '47.8224908948974', 'Ballon d''Alsace', 1247);");
            addSommet("INSERT INTO sommets VALUES (938282869, '6.7736388990571', '47.7672319948997', 'Planche des Belles Filles', 1148);");
            addSommet("INSERT INTO sommets VALUES (1762485578, '6.92214789903643', '47.7727801948995', 'Le Baerenkopf', 1074);");
            initialized=true;
        }
    }

    public int getSize(){
        String query="Select count(*) from "+TABLE_SOMMETS;
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL(query);
        return 0;
    }

    public List<Sommet> getSommets(){
        String query="SELECT * FROM sommets;";
        List<Sommet> values=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_SOMMETS,new String[]{KEY_ID,KEY_LONGITUDE,KEY_LATITUDE,KEY_NOM,KEY_ALTITUDE},null,null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            do{
                Sommet sommet = new Sommet(Long.valueOf(cursor.getString(0)), Double.valueOf(cursor.getString(1)),Double.valueOf(cursor.getString(2)), cursor.getString(3), Integer.valueOf(cursor.getString(4)));
                values.add(sommet);
            } while(cursor.moveToNext());
        }
        return values;
    }
}
