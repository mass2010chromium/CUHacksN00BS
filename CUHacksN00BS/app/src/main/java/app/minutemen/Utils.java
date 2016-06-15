package app.minutemen;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent on 6/11/2016.
 */
public class Utils {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference userRef;
    public static DatabaseReference helpRef;

    static {
        userRef = database.getReference("minutemen");
        helpRef = database.getReference("minutemenHelp");
    }

    public static <T> T getComponent(int id, AppCompatActivity activity) {
        return (T) activity.findViewById(id);
    }

    public static void requestPermission(AppCompatActivity activity, int requestId,
                                         String permission, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
//            PermissionUtils.RationaleDialog.newInstance(requestId, finishActivity)
//                    .show(activity.getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

        }
    }

    static volatile Location loc;

    public static LocationRunnable getLocation(GoogleApiClient client, Activity inst, IRecieveLocation locHandler) {
        LocationRunnable running = new LocationRunnable(client, inst, locHandler);
        Thread thread = new Thread(running);
        thread.start();
        return running;
    }
}
