package com.example.qrmonsters;

import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public interface GPSListener extends LocationListener{

    public void onLocationChanged(Location location);
    public void onProviderDisabled(String provider);
    public void onProviderEnabled(String provider);
    public void onStatusChanged(String provider, int status, Bundle extras);

}
