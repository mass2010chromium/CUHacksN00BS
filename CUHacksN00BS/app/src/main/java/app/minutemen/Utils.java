package app.minutemen;

import android.app.Activity;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent on 6/11/2016.
 */
class Utils {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    static DatabaseReference userRef;
    static DatabaseReference helpRef;

    static {
        userRef = database.getReference("minutemen");
        helpRef = database.getReference("minutemenHelp");
    }

    static <T> T getComponent(int id, AppCompatActivity activity) {
        return (T) activity.findViewById(id);
    }

    static volatile Location loc;

    static LocationRunnable getLocation(GoogleApiClient client, Activity inst, IReceiveLocation locHandler) {
        LocationRunnable running = new LocationRunnable(client, inst, locHandler);
        Thread thread = new Thread(running);
        thread.start();
        return running;
    }
}
