package edu.rasmussen.isthatedible.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.activities.FoodDetailsActivity;

/**
 */
public class FoodDetailsFragment extends Fragment {
    private FoodList.FoodItem item;
    private TextView name;
    private TextView refrigPeriod;
    private TextView frozPeriod;
    private TextView roomTPeriod;

    public FoodDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_details, container, false);

        name = (TextView) view.findViewById(R.id.textViewName);
        refrigPeriod = (TextView) view.findViewById(R.id.refridgeratedLabel);
        frozPeriod = (TextView) view.findViewById(R.id.frozenLabel);
        roomTPeriod = (TextView) view.findViewById(R.id.roomTLabel);
        //get passed item
        // populate fields with its data
        Bundle bundle = getArguments();
        if (bundle!= null) {
            item = bundle.getParcelable(FoodDetailsActivity.ITEM_KEY);
            updateDetails();
        }
        return view;
    }

    private void updateDetails()
    {
        name.setText(item.toString());
        switch (item.storage)
        {
            case "Frozen":
                frozPeriod.setText(item.details);
                refrigPeriod.setVisibility(TextView.GONE);
                roomTPeriod.setVisibility(TextView.GONE);
                break;
            case "Refrigerated":
                refrigPeriod.setText(item.details);
                frozPeriod.setVisibility(TextView.GONE);
                roomTPeriod.setVisibility(TextView.GONE);
                break;
            case "Room-temperature":
                roomTPeriod.setText(item.details);
                refrigPeriod.setVisibility(TextView.GONE);
                frozPeriod.setVisibility(TextView.GONE);
                break;
        }
    }
}
