package app.minutemen.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jcpen on 6/11/2016.
 */
public class MapWrapper {
    private GoogleMap map;

    /**
     * Wrapper for googlemap.
     * @param map
     */
    public MapWrapper(GoogleMap map) {
        this.map = map;
    }

    public LatLng addMarker(int lat, int lng) {
        return addMarker(lat, lng, ("Lat: " + lat + ", Lng: " + lng));
    }

    /**
     * Add a marker to the map.
     * @param lat
     * @param lng
     * @param message
     * @return The marker added.
     */
    public LatLng addMarker(int lat, int lng, String message) {
        LatLng pos = new LatLng(lat, lng);
        this.map.addMarker(new MarkerOptions().position(pos).title(message));
        return pos;
    }
}
