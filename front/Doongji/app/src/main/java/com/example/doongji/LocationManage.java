package com.example.doongji;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LocationManage extends Service implements LocationListener {
    private final Context mContext;
    int count = 0;

    boolean isGPSEnable = false;
    boolean isNetWorkEnable = false;
    boolean isGetLocation = false;

    Location location;
    double lat;
    double lon;

    private static final long MIN_DISTANCE_UPDATE = 0;
    private static final long MIN_TIME_UPDATE = 0;
    private HttpTask conn;

    ArrayList<Group> groupList=new ArrayList<>();
    protected LocationManager locationManager;

    public LocationManage(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetWorkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnable && !isNetWorkEnable) {
            } else {
                this.isGetLocation = true;
                if (isNetWorkEnable) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, MIN_DISTANCE_UPDATE, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnable) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, MIN_DISTANCE_UPDATE, this);
                    if (location == null) {

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
        if(User.getAccessList().size()==0) {
            for (int i = 0; i < groupList.size(); i++)
                User.getAccessList().add(false);
        }
    }

    public double getLatitude() {
        if (location != null)
            lat = location.getLatitude();
        return lat;
    }

    public double getLongitude() {
        if (location != null)
            lon = location.getLongitude();
        return lon;
    }

    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    public void stopUsingGPS() {
        if (locationManager != null)
            locationManager.removeUpdates(LocationManage.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        for(int i=0;i<groupList.size();i++){
            Location loc=new Location("group");
            loc.setLatitude(groupList.get(i).getXpos());
            loc.setLongitude(groupList.get(i).getYpos());
            Toast.makeText(mContext,"둥지 " + groupList.get(i).getName() + " 와의 거리 " + location.distanceTo(loc), Toast.LENGTH_SHORT).show();
            if( !User.getAccessList().get(i) && location.distanceTo(loc)<groupList.get(i).getRadius() ) {

                conn=new HttpTask();
                conn.execute("/services/sentry/" + User.getToken()+"/come/"+groupList.get(i).getId()+"/public","POST",null);
                User.getAccessList().set(i, true);
            }
            else if(User.getAccessList().get(i) && location.distanceTo(loc)>groupList.get(i).getRadius() ){
                User.getAccessList().set(i, false);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast toast = Toast.makeText(mContext, "2", Toast.LENGTH_SHORT);
//        toast.show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast toast = Toast.makeText(mContext, "3", Toast.LENGTH_LONG);
//        toast.show();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast toast = Toast.makeText(mContext, "4", Toast.LENGTH_LONG);
//        toast.show();
    }
}

