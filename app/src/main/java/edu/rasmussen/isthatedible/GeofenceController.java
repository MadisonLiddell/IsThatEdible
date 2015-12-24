package edu.rasmussen.isthatedible;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alice on 12/16/2015.
 */
public class GeofenceController {
    private final String TAG = GeofenceController.class.getName();

    private Context context;
    private GoogleApiClient googleApiClient;
    private Gson gson;
    private SharedPreferences prefs;
    private GeofenceControllerListener listener;

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    // 1. Create an IntentService PendingIntent
                    Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
                    PendingIntent pendingIntent =
                            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // 2. Associate the service PendingIntent with the geofence and call addGeofences
                    PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(
                            googleApiClient, getAddGeofencingRequest(), pendingIntent);

                    // 3. Implement PendingResult callback
                    result.setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                // 4. If successful, save the geofence
                                saveGeofence();
                            } else {
                                // 5. If not successful, log and send an error
                                Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                        " : " + status.getStatusCode());
                                sendError();
                            }
                        }
                    });
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {

                }
            };

    private List<Location> geofences;
    public List<Location> getGeofences() {
        return geofences;
    }

    private List<Location> namedGeofencesToRemove;

    private Geofence geofenceToAdd;
    private Location existingGeofenceToAdd;

    private static GeofenceController INSTANCE;

    public static GeofenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();

        gson = new Gson();
        geofences = new ArrayList<>();
        namedGeofencesToRemove = new ArrayList<>();
        prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        // add fences to list
//        ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();
//        FoodsDataSource dataSource = new FoodsDataSource(this);
//        dataSource.open();
//        ArrayList<edu.rasmussen.isthatedible.Location> locations = dataSource.getAllLocations();
//        dataSource.close();
//        // use the data from each location to add a geofence for it
//        for (edu.rasmussen.isthatedible.Location loc: locations) {
//            mGeofenceList.add(new Geofence.Builder()
//                    // Set the request ID of the geofence. This is a string to identify this
//                    // geofence.
//                    .setCircularRegion(
//                            loc.latitude,
//                            loc.latitude,
//                            500
//                    )
//                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                            Geofence.GEOFENCE_TRANSITION_EXIT)
//                    .build());
//        }
    }

    public interface GeofenceControllerListener {
        void onGeofencesUpdated();
        void onError();
    }

    private GeofencingRequest getAddGeofencingRequest() {
        List<Geofence> geofencesToAdd = new ArrayList<>();
        geofencesToAdd.add(geofenceToAdd);
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofencesToAdd);
        return builder.build();
    }

    private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        googleApiClient.connect();
    }

    private void sendError() {
        if (listener != null) {
            listener.onError();
        }
    }

    private void saveGeofence() {
        geofences.add(existingGeofenceToAdd);
        if (listener != null) {
            listener.onGeofencesUpdated();
        }
        String json = gson.toJson(existingGeofenceToAdd);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(existingGeofenceToAdd.id, json);
        editor.apply();
    }

    public void addGeofence(Location namedGeofence, GeofenceControllerListener listener) {
        this.existingGeofenceToAdd = namedGeofence;
        this.geofenceToAdd = namedGeofence.createGeofence();
        this.listener = listener;

        connectWithCallbacks(connectionAddListener);
    }
    public void addGeofence(Location namedGeofence) {
        this.existingGeofenceToAdd = namedGeofence;
        this.geofenceToAdd = namedGeofence.createGeofence();

        connectWithCallbacks(connectionAddListener);
    }
}
