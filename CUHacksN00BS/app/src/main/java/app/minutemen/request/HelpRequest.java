package app.minutemen.request;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by jcpen on 6/11/2016.
 */
public class HelpRequest {

    private DatabaseReference ref;

    public HelpRequest(DatabaseReference ref) {
        this.ref = ref;
    }
}
