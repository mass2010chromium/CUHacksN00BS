package app.minutemen;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

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
}
