package edu.rasmussen.isthatedible.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.FoodsSearchArrayAdapter;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.database.FoodsDataSource;

/**
 * Handles searching for an item in the database and then sending it to FoodDetails when selected.
 */
public class SearchActivity extends AppCompatActivity {
    public static final String SEARCH_ACTION = "search";
    public static final String SEARCH_VALUE = "value";

    private ListView listView;
    private FoodList matchingFoods;
    private FoodsDataSource foodsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodsDataSource = new FoodsDataSource(this);
        foodsDataSource.open();
        matchingFoods = new FoodList();
        Intent received = getIntent();

        if (received.getAction().equals(SEARCH_ACTION)) {
            String value = received.getStringExtra(SEARCH_VALUE);
            matchingFoods.ITEMS = foodsDataSource.getFoodsContaining(value);
        }

        setContentView(R.layout.activity_search_results);
        listView = (ListView) findViewById(R.id.listViewMatchingFoods);

        // add call to set matchingFoods with matching items
        // not needed for project
        // Set the adapter
        listView.setAdapter(new FoodsSearchArrayAdapter(this,
                R.layout.list_item_food_search, matchingFoods.ITEMS));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = parent.getItemAtPosition(position).toString();
                // launch details activity for this item
                Intent intent = new Intent(getApplicationContext(), FoodDetailsActivity.class);
                FoodList.FoodItem food = matchingFoods.getItemWithName(item);
                intent.putExtra(FoodDetailsActivity.ITEM_KEY, food);
            }
        });
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
}
