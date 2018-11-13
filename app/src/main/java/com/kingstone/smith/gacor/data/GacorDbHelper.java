package com.kingstone.smith.gacor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GacorDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GacorDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "gacor.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 3;

    /**
     * Constructs a new instance of {@link GacorDbHelper}.
     *
     * @param context of the app
     */
    public GacorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PLACES_TABLE =  "CREATE TABLE " + GacorContract.PlaceEntry.TABLE_NAME + " ("
                + GacorContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GacorContract.PlaceEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, "
                + GacorContract.PlaceEntry.COLUMN_PLACE_DETAIL + " TEXT, "
                + GacorContract.PlaceEntry.COLUMN_PLACE_LAT + " REAL NOT NULL, "
                + GacorContract.PlaceEntry.COLUMN_PLACE_LANG + " REAL NOT NULL);";

        String SQL_CREATE_HEATSPOT_TABLE =  "CREATE TABLE " + GacorContract.HeatspotEntry.TABLE_NAME + " ("
                + GacorContract.HeatspotEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GacorContract.HeatspotEntry.COLUMN_DATE + " INTEGER, "
                + GacorContract.HeatspotEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + GacorContract.HeatspotEntry.COLUMN_DETAIL + " TEXT, "
                + GacorContract.HeatspotEntry.COLUMN_LAT + " REAL NOT NULL, "
                + GacorContract.HeatspotEntry.COLUMN_LANG + " REAL NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PLACES_TABLE);
        db.execSQL(SQL_CREATE_HEATSPOT_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL("DROP TABLE IF EXISTS " + GacorContract.PlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GacorContract.HeatspotEntry.TABLE_NAME);
        onCreate(db);
    }
}
