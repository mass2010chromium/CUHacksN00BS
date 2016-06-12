package app.minutemen;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HelpOnWay extends AppCompatActivity {

    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_on_way);
        mainText = Utils.getComponent(R.id.helptextbox, this);
        Utils.database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.child("minutemenHelp").child(Settings.Secure.ANDROID_ID).getChildren()) {
                    if (child.getKey().toString().length() > 3) {
                        mainText.setText(dataSnapshot.child("minutemen").child(child.getValue().toString()).child("name").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
