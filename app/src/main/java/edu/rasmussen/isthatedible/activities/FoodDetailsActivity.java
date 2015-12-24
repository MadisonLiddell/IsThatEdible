package edu.rasmussen.isthatedible.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.fragments.FoodDetailsFragment;

/**
 * Handles the FoodDetailsActivity which takes a FoodItem and displays data from it.
 */
public class FoodDetailsActivity extends AppCompatActivity {
    public static final String ITEM_KEY = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // change to Pin item button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get name of food to show and pass to fragment
        FoodList.FoodItem item = getIntent().getParcelableExtra(FoodDetailsActivity.ITEM_KEY);
        Bundle b = new Bundle();
        b.putParcelable(FoodDetailsActivity.ITEM_KEY, item);
        FoodDetailsFragment fragment = new FoodDetailsFragment();
        fragment.setArguments(b);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFoodDetails, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
}
