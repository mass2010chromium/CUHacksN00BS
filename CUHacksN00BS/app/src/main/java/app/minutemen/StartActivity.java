package app.minutemen;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity
//        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    GPSTracker tracker;
//    GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //TODO FAIL
//        tracker = new GPSTracker(this);
//        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).addApi(LocationServices.API)
//                .build();
//        Thread connectThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    client.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        connectThread.start();
    }

    public void callHelp(View view) {
//        updateLocation();
//        Location loc = tracker.getLocation();
//        if (loc != null) {
//            Log.i("idk", "Pushing position: " + loc.getLatitude() + ", " + loc.getLongitude());
//            Utils.helpRef.push().setValue(new double[] {loc.getLatitude(), loc.getLongitude()});
//        }
//        else {
//            Log.i("idk", "Location Fail");
//        }
        Intent intent = new Intent(this, HelpOnWay.class);
        startActivity(intent);

//        //Temp must change later
//        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);
    }

    public void displayRegistration(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void standby(View view) {
        Utils.helpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {

                    if (child.getKey().toString().startsWith("none")) {
                        if (child.child("lat").getValue() == null) continue;
                        String lat = child.child("lat").getValue().toString();
                        String lon = child.child("lon").getValue().toString();
                        String newID = child.getKey().toString().substring(4);
                        Utils.helpRef.child(child.getKey()).setValue(newID);
                        Utils.helpRef.child(newID).child("helper").setValue(Settings.Secure.ANDROID_ID);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        updateLocation();
//    }
//
//    public void updateLocation() {
//        if ((ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED)) {
//            Location location = LocationServices.FusedLocationApi.getLastLocation(client);
//            if (location != null) {
//                handleNewLocation(location);
//            }
//            else {
//
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i("idk", "Suspended!");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.i("idk", "Failed!");
////        Log.i("idk", connectionResult.getErrorMessage() == null ?
////                String.valueOf(connectionResult.getErrorCode()) : connectionResult.getErrorMessage());
//    }
//
//    private void handleNewLocation(Location location) {
//        this.loc = location;
//    }
}
