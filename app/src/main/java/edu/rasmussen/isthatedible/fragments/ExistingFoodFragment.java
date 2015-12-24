package edu.rasmussen.isthatedible.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.rasmussen.isthatedible.activities.FoodDetailsActivity;
import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.database.FoodsDataSource;

/**
 * A fragment representing a list of FoodItems that were added by the user.
 */
public class ExistingFoodFragment extends Fragment {

    private ListView listView;
    public FoodList existingFoods;
    private FoodsDataSource foodsDataSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExistingFoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // open database and populate foods list from it
        foodsDataSource = new FoodsDataSource(getActivity().getApplicationContext());
        foodsDataSource.open();
        existingFoods = new FoodList();
        existingFoods.ITEMS = foodsDataSource.getAllAddedFoods();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_existing_food, container, false);
        listView = (ListView) view.findViewById(R.id.listViewExistingFoods);

        // Set the adapter
        listView.setAdapter(new ExistingFoodsArrayAdapter(getActivity().getApplicationContext(),
                R.layout.list_item_existing_food, existingFoods.ITEMS, inflater));
        // Set on click for items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = parent.getItemAtPosition(position).toString();
                // launch details activity for this item
                Intent intent = new Intent(getActivity().getApplicationContext(), FoodDetailsActivity.class);
                FoodList.FoodItem food = existingFoods.getItemWithName(item);
                intent.putExtra(FoodDetailsActivity.ITEM_KEY, food);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        foodsDataSource.close();
    }

}
