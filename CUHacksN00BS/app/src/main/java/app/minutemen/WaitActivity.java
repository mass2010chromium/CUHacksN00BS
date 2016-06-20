package app.minutemen;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;

public class WaitActivity extends AppCompatActivity {

    public static String lat;
    public static String lon;
    public static double distance;
    public static String toHelp;

    public static boolean accepted;

    private TextView distText;

    private DatabaseReference ref;

    private static final DecimalFormat formatter = new DecimalFormat("#####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        distText = Utils.getComponent(R.id.distance, this);
        distText.setText("They are " + formatter.format(distance) + " meters away");
    }

    @Override
    public void onStart() {
        super.onStart();
        ref = Utils.helpRef.child(toHelp);
        RegistrationActivity.updateName();
        accepted = false;
    }

    //TODO
    public void onDecline(View v) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    //TODO
    public void onAccept(View v) {
        accepted = true;
        DatabaseReference reference = Utils.helpRef.child(ref.getKey().substring(4));
        ref.setValue(null);
        reference.child("lat").setValue(lat);
        reference.child("lon").setValue(lon);
        Log.i("INST", StartActivity.inst.ID + ", " + RegistrationActivity.userName);
        reference.child("helper").setValue(RegistrationActivity.userName);

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!accepted) {
            DatabaseReference reference = Utils.helpRef.child("none" + ref.getKey().substring(4));
            ref.setValue(null);
            reference.child("lat").setValue(lat);
            reference.child("lon").setValue(lon);
            reference.child(StartActivity.inst.ID).setValue("Ditched.");
        }
    }
}
