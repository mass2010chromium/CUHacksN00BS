package app.minutemen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HelpOnWay extends AppCompatActivity {

    private TextView mainText;
    private Button finish;
    private boolean hasHelp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_on_way);
        mainText = Utils.getComponent(R.id.helptextbox, this);
        finish = Utils.getComponent(R.id.finish, this);
    }

    @Override
    public void onStart() {
        super.onStart();

        postLocation();

        Utils.helpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (hasHelp) return;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(StartActivity.inst.ID) && child.child("helper").getValue() != null) {
                        mainText.setText(child.child("helper").getValue() + " is coming here to help!");
                        finish.setVisibility(View.VISIBLE);
                        hasHelp = true;
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
    public void onStop() {
        super.onStop();
    }

    private void postLocation() {
        DatabaseReference ref = Utils.helpRef.child("none" + StartActivity.inst.ID);
        ref.child("lat").setValue(StartActivity.loc.getLatitude());
        ref.child("lon").setValue(StartActivity.loc.getLongitude());
    }

    public void onFinish(View v) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}