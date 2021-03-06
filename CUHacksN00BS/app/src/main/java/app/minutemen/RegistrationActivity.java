package app.minutemen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    EditText name;
    CheckBox adult;
    ButtonToggle aToggle;
    CheckBox child;
    ButtonToggle cToggle;
    CheckBox infant;
    ButtonToggle iToggle;
    EditText expire;
    Button done;

    public static String userName;

    public static void updateName() {
        Utils.userRef.child(StartActivity.inst.ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name")) {
                    userName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userName = "NULL";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //An object for each component.
        name = Utils.getComponent(R.id.editText, this);

        adult = Utils.getComponent(R.id.adultCheckBox, this);
        aToggle = new ButtonToggle(adult, false);
        child = Utils.getComponent(R.id.childCheckBox, this);
        cToggle= new ButtonToggle(child, false);
        infant = Utils.getComponent(R.id.infantCheckBox, this);
        iToggle = new ButtonToggle(infant, false);

        expire = Utils.getComponent(R.id.dateText, this);
        done = Utils.getComponent(R.id.doneButton, this);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Write each component to firebase.
                DatabaseReference ref = Utils.userRef.child(StartActivity.inst.ID);
                DatabaseReference id = ref.child("name");
                id.setValue(name.getText().toString());
                userName = name.getText().toString();
                DatabaseReference adultRef = ref.child("adult");
                adultRef.setValue(aToggle.clicked);
                DatabaseReference childRef = ref.child("child");
                childRef.setValue(cToggle.clicked);
                DatabaseReference infantRef = ref.child("infant");
                infantRef.setValue(iToggle.clicked);
                DatabaseReference expireRef = ref.child("expire");
                expireRef.setValue(expire.getText().toString());

                //Switch activities.
                switchActivity();
            }
        });
    }

    //TEMP
    public void switchActivity() {
        Intent intent = new Intent(this, VerificationCompletion.class);
        startActivity(intent);
    }

    private static class ButtonToggle implements View.OnClickListener{

        boolean clicked;

        ButtonToggle(CheckBox box, boolean state) {
            box.setOnClickListener(this);
            this.clicked = state;
        }

        @Override
        public void onClick(View v) {
            clicked = !clicked;
        }
    }
}
