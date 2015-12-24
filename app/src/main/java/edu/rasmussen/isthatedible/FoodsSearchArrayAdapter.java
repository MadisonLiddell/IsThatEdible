package edu.rasmussen.isthatedible;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.FoodList.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ArrayAdapter for the food search result list
 */
public class FoodsSearchArrayAdapter extends ArrayAdapter<FoodItem> {

    private Context context;
    private ArrayList<FoodItem> items;
    private final int layoutResourceId;

    public FoodsSearchArrayAdapter(Context context, int resource, ArrayList<FoodList.FoodItem> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutResourceId = resource;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textViewName = (TextView)row.findViewById(R.id.searchMatchId);
            holder.textViewExpire = (TextView)row.findViewById(R.id.searchMatchExpiration);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        FoodList.FoodItem item = items.get(position);

        holder.textViewName.setText(item.id);
        holder.textViewExpire.setText(item.content);

        return row;
    }

    static class ViewHolder
    {
        TextView textViewName;
        TextView textViewExpire;
    }
}
