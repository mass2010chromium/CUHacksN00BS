package app.minutemen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VerificationCompletion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_completion);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
