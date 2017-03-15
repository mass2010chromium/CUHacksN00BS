package app.minutemen;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        IReceiveLocation {

    public String ID;

    public static StartActivity inst;
    public static Class<? extends Activity> currentActivity;

    public volatile ValueEventListener receiveMSG;

    public volatile static Location loc;
    public volatile static boolean waitingForLocation;
    public static LocationRunnable runnable;

    GoogleApiClient client;

    private TextView debugPos;

    String textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        debugPos = Utils.getComponent(R.id.debugPos, this);
        debugPos.bringToFront();
    }

    //In meters
    private static final double EARTH_RADIUS = 6371e3;
    private static final double THRESHOLD_DISTANCE = 1500; //1.5 km

    /**
     * Check if I am close enough to help.
     * Formula from: http://www.movable-type.co.uk/scripts/latlong.html
     * @param lat
     * @param lon
     * @return
     */
    private double checkInRange(double lat, double lon) {
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lon);
        double myLat = Math.toRadians(loc.getLatitude());
        double myLon = Math.toRadians(loc.getLongitude());
        return Math.abs(Math.acos(Math.sin(lat1) * Math.sin(myLat) +
                Math.cos(lat1) * Math.cos(myLat) * Math.cos(myLon - lon1)) * EARTH_RADIUS);
    }

    @Override
    public void onStart() {
        super.onStart();

        inst = this;

        client.connect();
        currentActivity = StartActivity.class;

        textMessage = "Nothing";

        final Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                debugPos.setText(textMessage);
                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });
    }

    public void callHelp(View view) {
        Utils.helpRef.child(StartActivity.inst.ID).removeValue();
        if (loc != null) switchActivity(HelpOnWay.class);
    }

    public void displayRegistration(View view){
        switchActivity(RegistrationActivity.class);
    }

    public void switchActivity(Class<? extends Activity> activity) {
        currentActivity = activity;
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        textMessage = "Connected";
        waitingForLocation = false;
        Thread updateLocationThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    long startTime = System.nanoTime();
                    if (!waitingForLocation) {
                        runnable = Utils.getLocation(client, inst, inst);
                        waitingForLocation = true;
                    } else if (!runnable.isStarted()){
                        waitingForLocation = false;
                    }
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) { e.printStackTrace(); }
                        if (System.nanoTime() - startTime > 10e9) {
                            break;
                        }
                    }
                }
            }
        });

        updateLocationThread.start();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocation(Location location) {
        loc = location;
        textMessage = "Lat: " + loc.getLatitude() + ", Lon: " + loc.getLongitude();
        Utils.userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(ID)) return;
                if (receiveMSG != null) Utils.helpRef.removeEventListener(receiveMSG);
                Utils.helpRef.addValueEventListener(receiveMSG = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (currentActivity != StartActivity.class) return;
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {

                            if (child.getKey().startsWith("none")) {
                                if (child.hasChild(inst.ID)) continue;
                                if (child.child("lat").getValue() == null || child.child("lon").getValue() == null)
                                    continue;
                                String lat = child.child("lat").getValue().toString();
                                String lon = child.child("lon").getValue().toString();

                                double latN = Double.parseDouble(lat);
                                double lonN = Double.parseDouble(lon);
                                double dist = checkInRange(latN, lonN);

                                if (dist > THRESHOLD_DISTANCE) continue;

                                //Change status from "none" to "pending"
                                String id = child.getKey().substring(4);
                                if (id.equals(ID)) continue;
                                String newID = "pend" + id;
                                Utils.helpRef.child(child.getKey()).setValue(null);
                                DatabaseReference ref = Utils.helpRef.child(newID);
                                ref.child("lat").setValue(lat);
                                ref.child("lon").setValue(lon);
                                WaitActivity.lat = lat;
                                WaitActivity.lon = lon;
                                WaitActivity.toHelp = newID;
                                WaitActivity.distance = dist;
                                switchActivity(WaitActivity.class);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        debugPos.setText("Failed to connect");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
