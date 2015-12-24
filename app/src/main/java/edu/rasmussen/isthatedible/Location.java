package edu.rasmussen.isthatedible;

import com.google.android.gms.location.Geofence;

import java.util.UUID;

/**
 * Class representing a location added by the user to be used in a geofence.
 */
public class Location {
    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public float radius = 1000.0f;    // to use with geofence, the radius around this location

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Geofence createGeofence() {
        id = UUID.randomUUID().toString();
        return new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }
}
