package app.minutemen;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent on 6/11/2016.
 */
public class Utils {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference ref;
    static {
        ref = database.getReference("minutemen");
    }

    public static <T> T getComponent(int id, AppCompatActivity activity) {
        return (T) activity.findViewById(id);
    }
}
