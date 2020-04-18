package com.example.knowyourgovernment_diepholz;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import static android.content.Context.LOCATION_SERVICE;

public class Locator {


    private MainActivity mainActivity;
    private LocationManager locationManager;
    private LocationListener locationListener;


    private boolean locationPermissionCheck(){
        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){ //asking for permissions from the user
            ActivityCompat.requestPermissions(mainActivity, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION}, 5); //request code must be 5
            return false;
        }
        return true; //permission is already allowed!
    }


    public Locator(MainActivity activity){

        mainActivity = activity;

        if(locationPermissionCheck()){

            locationManagerSet();
            locationDeterminer();
        }
    }

    public void locationManagerSet(){

        if (locationManager != null){
            return;
        }

        if(locationPermissionCheck() == false){
            return;
        }


        locationManager = (LocationManager) mainActivity.getSystemService(LOCATION_SERVICE);
        //^^ System location manager set as locationManager


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(mainActivity, "Updating from this Location: " + location.getProvider(), Toast.LENGTH_SHORT).show();
                mainActivity.getData(location.getLatitude(), location.getLongitude());
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

        locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER,1000,0,locationListener);
        }


        public void power(){
        locationManager.removeUpdates(locationListener);

        locationManager = null; //have to reset this to null to power down
        }

        public void locationDeterminer(){


        if(locationPermissionCheck() == false){
            return;
        }

        if(locationManager == null){
            locationManagerSet();
        }

        if(locationManager != null){

            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if(location!=null){
                mainActivity.getData(location.getLatitude(),location.getLongitude());
                Toast.makeText(mainActivity, "Location is set at: " + LocationManager.PASSIVE_PROVIDER, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        }

    }




