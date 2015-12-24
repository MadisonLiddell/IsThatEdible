package edu.rasmussen.isthatedible.database;

/**
 * @author Madison Liddell
 * @since 12/14/2015
 *
 * SQLite Database Helper class
 * Adds abstraction for working with the Foods database. It has a Foods table with columns
 * for id (primary key), name, expiration date, food type, time period to notify for expiration,
 * storage(frozen or refrigerated), boolean for notifications, and boolean for location services.
 * Also has a Locations table with an id, latitude value, longitude value, and a name.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "foods.db";
    private static final int DB_VERSION = 1;

    // food table has PK, name, type, and deadline
    public static final String TABLE_FOOD = "Food";
    public static final String COLUMN_PRIMARY_KEY_FOOD = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXPIRATION = "expiration";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NOTIFY_PERIOD = "notify_before";
    public static final String COLUMN_STORAGE ="storage_type";
    public static final String COLUMN_NOTIFY = "has_notify_on";
    public static final String COLUMN_LOCATION_SERVICES = "has_location_on";
    public static final String COLUMN_PINNED = "is_pinned";
    // Locations table
    public static final String TABLE_LOCATION = "Locations";
    public static final String COLUMN_PRIMARY_KEY_LOC = "_id";
    public static final String COLUMN_LOC_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    // SQL Statement to create a new database.
    private static final String CREATE_TABLE_FOOD = "create table " + TABLE_FOOD
            + "(" + COLUMN_PRIMARY_KEY_FOOD + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EXPIRATION + " text not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_NOTIFY_PERIOD + " text, "
            + COLUMN_STORAGE + " text not null, "
            + COLUMN_NOTIFY + " integer not null, "
            + COLUMN_LOCATION_SERVICES + " integer not null, "
            + COLUMN_PINNED + " integer not null);";
    private static final String CREATE_TABLE_LOCATION = "create table " + TABLE_LOCATION
            + "(" + COLUMN_PRIMARY_KEY_LOC + " integer primary key autoincrement, "
            + COLUMN_LOC_NAME + " text not null, "
            + COLUMN_LATITUDE + " real not null, "
            + COLUMN_LONGITUDE + " real not null);";
    private SQLiteDatabase m_myDatabase;

    /**
     * Constructor
     * @param context context to use
     */
    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Close database session
     */
    @Override
    public synchronized void close()
    {
        if (m_myDatabase != null) {
            m_myDatabase.close();
            m_myDatabase = null;
        }

        super.close();
    }

    /**
     * Called when no database exists in disk and the helper class need to create a new one.
     * @param db database to use for creation
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_LOCATION);
    }

    /**
     * Upgrade database, deleting old version and creating new one
     * @param db database to upgrade
     * @param oldVersion old version number
     * @param newVersion new version number to use
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }
}