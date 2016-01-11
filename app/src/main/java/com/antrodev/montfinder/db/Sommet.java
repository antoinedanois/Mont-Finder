package com.antrodev.montfinder.db;

/**
 * Created by Leonardo on 04/01/2016.
 */
public class Sommet {
    private long idSommet;
    private double longitude;
    private double latitude;
    private String nomSommet;
    private int altitude;

    public Sommet() {
    }

    public Sommet(long idSommet, double longitude, double latitude, String nomSommet, int altitude) {
        this.idSommet = idSommet;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nomSommet = nomSommet;
        this.altitude = altitude;
    }

    public Sommet(double longitude, double latitude, String nomSommet, int altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.nomSommet = nomSommet;
        this.altitude = altitude;
    }

    public long getIdSommet() {
        return idSommet;
    }

    public void setIdSommet(long idSommet) {
        this.idSommet = idSommet;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNomSommet() {
        return nomSommet;
    }

    public void setNomSommet(String nomSommet) {
        this.nomSommet = nomSommet;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
}
