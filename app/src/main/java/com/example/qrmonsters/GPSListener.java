package com.example.qrmonsters;

import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
/**
 * GPSListener is an interface that extends LocationListener and provides additional methods for
 * handling changes in GPS status.
 *
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public interface GPSListener extends LocationListener{

    /**
     * Called when the location has changed.
     *
     * @param location The new location.
     */
    public void onLocationChanged(Location location);
    /**
     * Called when the provider is disabled by the user.
     *
     * @param provider The name of the disabled provider.
     */
    public void onProviderDisabled(String provider);
    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider The name of the enabled provider.
     */
    public void onProviderEnabled(String provider);
    /**
     * Called when the GPS status has changed.
     *
     * @param provider The name of the provider.
     * @param status   The new status.
     * @param extras   An optional Bundle containing additional status information.
     */
    public void onStatusChanged(String provider, int status, Bundle extras);

}
