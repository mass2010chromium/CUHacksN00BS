package app.minutemen;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        helpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static <T> T getComponent(int id, AppCompatActivity activity) {
        return (T) activity.findViewById(id);
    }
}
