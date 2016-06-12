package app.minutemen;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vincent on 6/11/2016.
 */
public class Utils {
    public static <T> T getComponent(int id, AppCompatActivity activity) {
        return (T) activity.findViewById(id);
    }
}
