package app.minutemen;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by jcpen on 6/14/2016.
 */
public class LocationRunnable implements Runnable{

    private GoogleApiClient client;
    private Activity inst;
    private IRecieveLocation locHandler;
    private boolean started;

    public LocationRunnable(GoogleApiClient client, Activity inst, IRecieveLocation locHandler) {
        this.client = client;
        this.inst = inst;
        this.locHandler = locHandler;
        started = true;
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        Utils.loc = null;
        if (ActivityCompat.checkSelfPermission(inst, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(inst,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
//            inst.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("Connect", "No Perms!");
            stop();
            return;
        }
        while (Utils.loc == null) {
            if (!started) {
                Log.i("Connect", "Stopped.");
                return;
            }
            Utils.loc = LocationServices.FusedLocationApi.getLastLocation(
                    client);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //TODO
            Log.i("Connect", "Trying to get location");
        }
        Log.i("Connect", "Found! " + Utils.loc.getLatitude() + ", " + Utils.loc.getLongitude());
        locHandler.onLocation(Utils.loc);
        started = false;
    }

    public boolean isStarted() {
        return started;
    }
}
