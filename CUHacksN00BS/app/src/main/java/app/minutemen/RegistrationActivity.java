package app.minutemen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class RegistrationActivity extends AppCompatActivity {
    public boolean nameComplete = false;

    EditText name;
    CheckBox adult;
    ButtonToggle aToggle;
    CheckBox child;
    ButtonToggle cToggle;
    CheckBox infant;
    ButtonToggle iToggle;
    EditText expire;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
                DatabaseReference ref = Utils.userRef.child(name.getText().toString());
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

        public boolean clicked;

        public ButtonToggle(CheckBox box, boolean state) {
            box.setOnClickListener(this);
            this.clicked = state;
        }

        @Override
        public void onClick(View v) {
            clicked = !clicked;
        }
    }
}
