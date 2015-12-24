package edu.rasmussen.isthatedible.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;

/**
 * ArrayAdapter for the list of existing foods
 */
public class ExistingFoodsArrayAdapter extends ArrayAdapter<FoodList.FoodItem> {

    private Context context;
    private ArrayList<FoodList.FoodItem> items;
    private final int layoutResourceId;
    private LayoutInflater inflater;

    public ExistingFoodsArrayAdapter(Context context, int resource, ArrayList<FoodList.FoodItem> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        layoutResourceId = resource;
        items = objects;
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textViewName = (TextView)row.findViewById(R.id.existingId);
            holder.textViewExpire = (TextView)row.findViewById(R.id.existingExpiration);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        FoodList.FoodItem item = items.get(position);

        holder.textViewName.setText(String.valueOf(item.id));
        holder.textViewExpire.setText(item.content);

        return row;
    }

    static class ViewHolder
    {
        TextView textViewName;
        TextView textViewExpire;
    }
}

