package edu.rasmussen.isthatedible;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.rasmussen.isthatedible.activities.MainActivity;
import edu.rasmussen.isthatedible.database.FoodsDataSource;

/**
 * Handles geofence transition events which signals when a user has entered within range of
 * one of their saved locations and has an expired item.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    private final String TAG = GeofenceTransitionsIntentService.class.getName();

    private SharedPreferences prefs;
    private Gson gson;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = getApplicationContext().getSharedPreferences(
                Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        gson = new Gson();

        // 1. Get the event
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
//                onError(event.getErrorCode());
            } else {

                // 2. Get the transition type
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                        transition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                        transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    List<String> geofenceIds = new ArrayList<>();

                    // 3. Accumulate a list of event geofences
                    for (Geofence geofence : event.getTriggeringGeofences()) {
                        geofenceIds.add(geofence.getRequestId());
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                            transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        // 4. Pass the geofence list to the notification method
                        onEnteredGeofences(geofenceIds);
                    }
                }
            }

        }
    }

    private void onEnteredGeofences(List<String> geofenceIds) {
        // 1. Outer loop over all geofenceIds
        for (String geofenceId : geofenceIds) {
            String geofenceName = "";

            FoodsDataSource dataSource = new FoodsDataSource(getApplicationContext());
            dataSource.open();
            List<Location> locations = dataSource.getAllLocations();
            // 2, Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreferences
            for (Location loc:locations) {
                if(loc.id.equals(geofenceId))
                    geofenceName = loc.name;
            }

            // 3. Set the notification text and send the notification
            notifyUser(this, this.getResources().getString(R.string.notification_title), geofenceName, 0);
        }
    }

    /**
     * Build and show notification to user. Plays default notification sound with default vibration
     * @param context context to create from
     * @param title title to display on notification
     * @param id id to use for notification
     */
    private void notifyUser(Context context, String title,String details, int id)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(details)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // play sound
                .build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;                              // vibrate device
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, notification);
    }
}
