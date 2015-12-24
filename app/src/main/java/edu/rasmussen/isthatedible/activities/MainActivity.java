package edu.rasmussen.isthatedible.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

import edu.rasmussen.isthatedible.Constants;
import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.GeofenceController;
import edu.rasmussen.isthatedible.GeofenceTransitionsIntentService;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.database.FoodsDataSource;

/**
 * Main activity in the app. Sets up listeners for toolbar and connects to Google's location
 * services by indirectly starting the service.
 *
 * @author Madison Liddell
 * @since 12/13/15
 *
 */
public class MainActivity extends AppCompatActivity {
    private static int VOICE_RECOGNITION = 1234; //request code
    private static final String PREFERENCES_KEY = "edible shared prefs";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        LinearLayout searchContainer = (LinearLayout) findViewById(R.id.search_container);
//        EditText toolbarSearchView = (EditText) findViewById(R.id.searchBox);
//        ImageView searchClearButton = (ImageView) findViewById(R.id.searchClear);
        Button voiceSearchButton = (Button) findViewById(R.id.voiceSearchButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Float add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                startActivityForResult(intent, AddFoodActivity.ADD_FOOD_REQUEST);
            }
        });

        // Voice search button starts listening
        voiceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the name of a food, i.e. lamb chop");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, VOICE_RECOGNITION);
            }
        });

        // Enter button listener
//        toolbarSearchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    // launch search activity with search value
//                    String search = v.getText().toString();
//                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//                    intent.setAction(SearchActivity.SEARCH_ACTION);
//                    intent.putExtra(SearchActivity.SEARCH_VALUE, search);
//                    startActivity(intent);
//                    return true;
//                }
//                return false;
//            }
//        });

//        // Clear search text when clear button is tapped
//        searchClearButton.setOnClickListener(new View.OnClickListener() {
//            EditText toolbarSearchView = (EditText) findViewById(R.id.search_view);
//            @Override
//            public void onClick(View v) {
//                toolbarSearchView.setText("");
//            }
//        });

        // Hide the search view
        //searchContainer.setVisibility(View.GONE);

        GeofenceController.getInstance().init(this);
    }

    /**
     * Receive spoken command and process it
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // start search activity using spoken item
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.setAction(SearchActivity.SEARCH_ACTION);
            intent.putExtra(SearchActivity.SEARCH_VALUE, results.get(0));
            startActivity(intent);
        } else {
            Fragment existingFoods = getFragmentManager().findFragmentById(R.id.existing_food_fragment);
            Fragment pinnedFoods = getFragmentManager().findFragmentById(R.id.pinned_food_fragment);
            final android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(existingFoods);
            ft.attach(existingFoods);
            ft.detach(pinnedFoods);
            ft.attach(pinnedFoods);
            ft.commit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // launch settings activity
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }
}
