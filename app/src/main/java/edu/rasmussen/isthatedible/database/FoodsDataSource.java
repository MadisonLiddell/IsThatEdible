package edu.rasmussen.isthatedible.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.Location;

/**
 * @author Madison Liddell
 * @since 12/14/2015
 *
 * Handles interactions with the SQLite database and DatabaseHelper
 */
public class FoodsDataSource
{
    // Handles
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    // Database fields/columns
    private String[] allFoodColumns = {DatabaseHelper.COLUMN_PRIMARY_KEY_FOOD,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_EXPIRATION,  // for created item, works like expiration date
            // for database item, works like a lasts for period
            DatabaseHelper.COLUMN_TYPE,
            DatabaseHelper.COLUMN_NOTIFY_PERIOD,
            DatabaseHelper.COLUMN_STORAGE,
            DatabaseHelper.COLUMN_NOTIFY,
            DatabaseHelper.COLUMN_LOCATION_SERVICES,
            DatabaseHelper.COLUMN_PINNED};
    private String[] allLocColumns = {DatabaseHelper.COLUMN_PRIMARY_KEY_LOC,
            DatabaseHelper.COLUMN_LOC_NAME,
            DatabaseHelper.COLUMN_LATITUDE,
            DatabaseHelper.COLUMN_LONGITUDE};

    /**
     * Constructor
     * @param context current context to use with DatabaseHelper
     */
    public FoodsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Open the connection
     */
    public void open()
    {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close the connection
     */
    public void close()
    {
        dbHelper.close();
    }

    /**
     * Inserts a table into the database with columns for a FoodItem and returns the new FoodItem
     * @param name FoodItem name
     * @param type FoodItem type
     * @return FoodList.FoodItem
     */
    public FoodList.FoodItem addFoodItem(String name, String expiration, String type, String period,
                                         String storage, int notify, int location, int pinned)
    {
        ContentValues values = new ContentValues();
        // add fields to table
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EXPIRATION, expiration);
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_NOTIFY_PERIOD, period);
        values.put(DatabaseHelper.COLUMN_STORAGE, storage);
        values.put(DatabaseHelper.COLUMN_NOTIFY, notify);
        values.put(DatabaseHelper.COLUMN_LOCATION_SERVICES, location);
        values.put(DatabaseHelper.COLUMN_PINNED, pinned);
        // add the table to the database
        long insertId = database.insert(DatabaseHelper.TABLE_FOOD, null, values);
        // create cursor to the database
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD,
                allFoodColumns, DatabaseHelper.COLUMN_PRIMARY_KEY_FOOD + " = " + insertId, null,
                null, null, null);
        // set to first entry
        cursor.moveToFirst();
        // create new task object using the cursor
        FoodList.FoodItem newItem = cursorToItem(cursor);
        cursor.close();
        return(newItem);
    }

    public Location addLocation(String name, double lat, double longi)
    {
        ContentValues values = new ContentValues();
        // add fields to table
        values.put(DatabaseHelper.COLUMN_LOC_NAME, name);
        values.put(DatabaseHelper.COLUMN_LATITUDE, lat);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longi);
        // add the table to the database
        long insertId = database.insert(DatabaseHelper.TABLE_LOCATION, null, values);
        // create cursor to the database
        Cursor cursor = database.query(DatabaseHelper.TABLE_LOCATION,
                allLocColumns, DatabaseHelper.COLUMN_PRIMARY_KEY_LOC + " = " + insertId, null,
                null, null, null);
        // set to first entry
        cursor.moveToFirst();
        // create new task object using the cursor
        Location newLocation = cursorToLocation(cursor);
        cursor.close();
        return(newLocation);
    }

    private Location cursorToLocation(Cursor cursor) {
        return(new Location(cursor.getString(1), cursor.getLong(2), cursor.getLong(3)));
    }

    public ArrayList<FoodList.FoodItem> getFoodsContaining(String name)
    {
        ArrayList<FoodList.FoodItem> foodList = new ArrayList<>();
        // Query all tasks
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD, allFoodColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            FoodList.FoodItem item = cursorToItem(cursor);
            // add task to list
            if (item.toString().contains(name))
                foodList.add(item);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return foodList;
    }

    /**
     * Delete a food from the database
     */
    public void deleteFood(FoodList.FoodItem foodItem)
    {
        int id = foodItem.id;
        // Delete entries matching the task id
        database.delete(DatabaseHelper.TABLE_FOOD, DatabaseHelper.COLUMN_PRIMARY_KEY_FOOD + "=" + id, null);
    }

    /**
     * Get all saved tasks from the database
     * @return List of matching tasks
     */
    public ArrayList<FoodList.FoodItem> getAllAddedFoods()
    {
        ArrayList<FoodList.FoodItem> foodList = new ArrayList<>();
        // Query all tasks
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD, allFoodColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            FoodList.FoodItem item = cursorToItem(cursor);
            // add task to list
            if (!item.isPinned)
                foodList.add(item);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return foodList;
    }

    public ArrayList<FoodList.FoodItem> getAllPinnedFoods()
    {
        ArrayList<FoodList.FoodItem> foodList = new ArrayList<>();
        // Query all tasks
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD, allFoodColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            FoodList.FoodItem item = cursorToItem(cursor);
            // add task to list
            if (item.isPinned)
                foodList.add(item);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return foodList;
    }
    public ArrayList<Location> getAllLocations()
    {
        ArrayList<Location> locationList = new ArrayList<>();
        // Query all tasks
        Cursor cursor = database.query(DatabaseHelper.TABLE_LOCATION, allLocColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Location item = cursorToLocation(cursor);
            locationList.add(item);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return locationList;
    }

    /**
     * Get only tasks ending today from the database
     * @return List of matching tasks
     */
    public ArrayList<FoodList.FoodItem> getTodaysTasks(GregorianCalendar today)
    {
        ArrayList<FoodList.FoodItem> todoList = new ArrayList<>();
        // Select * where deadline >= 12 hours ago and <= 12 hours from now
        String whereClause = DatabaseHelper.COLUMN_EXPIRATION + " >= " + " ? " +
                "and " + DatabaseHelper.COLUMN_EXPIRATION + " <= ?";
        // Compare to 12 hours past and 12 hours ahead
        Long currentTime = System.currentTimeMillis() - (TimeUnit.DAYS.toMillis(1)/2);
        Long currentDay = System.currentTimeMillis() + (TimeUnit.DAYS.toMillis(1));
        String[] whereArgs = {currentTime.toString(), currentDay.toString()};

        // Query tasks ending today
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD, allFoodColumns, whereClause,  whereArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            FoodList.FoodItem task = cursorToItem(cursor);
            // add task to list
            todoList.add(task);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return todoList;
    }

    /**
     * Create Task object from Cursor object
     * @param cursor to create Task from
     * @return Task
     */
    private FoodList.FoodItem cursorToItem(Cursor cursor)
    {
        return(new FoodList.FoodItem(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
    }
}
