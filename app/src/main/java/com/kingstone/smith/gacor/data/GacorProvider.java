package com.kingstone.smith.gacor.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.kingstone.smith.gacor.data.GacorDbHelper.LOG_TAG;

public class GacorProvider extends ContentProvider {

    /*
     * These constant will be used to match URIs with the data they are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */
    public static final int CODE_PLACE = 100;
    public static final int CODE_PLACE_WITH_ID = 101;

    public static final int CODE_HEATSPOT = 200;
    public static final int CODE_HEATSPOT_WITH_ID = 201;

    /*
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of GacorProvider and is a
     * common convention in Android programming.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private GacorDbHelper mOpenHelper;

    /**
     * Creates the UriMatcher that will match each URI to the CODE_PLACE and
     * CODE_PLACE_WITH_DATE constants defined above.
     * <p>
     * It's possible you might be thinking, "Why create a UriMatcher when you can use regular
     * expressions instead? After all, we really just need to match some patterns, and we can
     * use regular expressions to do that right?" Because you're not crazy, that's why.
     * <p>
     * UriMatcher does all the hard work for you. You just have to tell it which code to match
     * with which URI, and it does the rest automagically. Remember, the best programmers try
     * to never reinvent the wheel. If there is a solution for a problem that exists and has
     * been tested and proven, you should almost always use it unless there is a compelling
     * reason not to.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_PLACE and CODE_PLACE_WITH_DATE
     */
    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GacorContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Sunshine, we use CODE_PLACE or CODE_PLACE_WITH_DATE.
         */

        /* This URI is content://com.kingstone.smith.gacor/places/ */
        matcher.addURI(authority, GacorContract.PATH_PLACE, CODE_PLACE);

        /*
         * This URI would look something like content://com.kingstone.smith.gacor/places/1472214172
         * The "/#" signifies to the UriMatcher that if PATH_PLACE is followed by ANY number,
         * that it should return the CODE_PLACE_WITH_ID code
         */
        matcher.addURI(authority, GacorContract.PATH_PLACE + "/#", CODE_PLACE_WITH_ID);

        //heatspot table matcher
        matcher.addURI(authority, GacorContract.PATH_HEATSPOT, CODE_HEATSPOT);
        matcher.addURI(authority, GacorContract.PATH_HEATSPOT + "/#", CODE_HEATSPOT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        /*
         * As noted in the comment above, onCreate is run on the main thread, so performing any
         * lengthy operations will cause lag in your app. Since GacorDbHelper's constructor is
         * very lightweight, we are safe to perform that initialization here.
         */
        mOpenHelper = new GacorDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            /*
             * When sUriMatcher's match method is called with a URI that looks EXACTLY like this
             *
             *      content://com.kingstone.smith.gacor/places/
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return all of the places in our places table.
             *
             * In this case, we want to return a cursor that contains every row of places data
             * in our places table.
             */
            case CODE_PLACE:
                cursor = mOpenHelper.getReadableDatabase().query(
                        GacorContract.PlaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_PLACE_WITH_ID:
                selection = GacorContract.PlaceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = mOpenHelper.getReadableDatabase().query(
                        GacorContract.PlaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_HEATSPOT:
                cursor = mOpenHelper.getReadableDatabase().query(
                        GacorContract.HeatspotEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_HEATSPOT_WITH_ID:
                selection = GacorContract.HeatspotEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = mOpenHelper.getReadableDatabase().query(
                        GacorContract.HeatspotEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * In Sunshine, we aren't going to do anything with this method. However, we are required to
     * override it as WeatherProvider extends ContentProvider and getType is an abstract method in
     * ContentProvider. Normally, this method handles requests for the MIME type of the data at the
     * given URI. For example, if your app provided images at a particular URI, then you would
     * return an image URI from this method.
     *
     * @param uri the URI to query.
     * @return nothing in Sunshine, but normally a MIME type string, or null if there is no type.
     */
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_PLACE:
                return GacorContract.PlaceEntry.CONTENT_LIST_TYPE;
            case CODE_PLACE_WITH_ID:
                return GacorContract.PlaceEntry.CONTENT_ITEM_TYPE;
            case CODE_HEATSPOT:
                return GacorContract.HeatspotEntry.CONTENT_LIST_TYPE;
            case CODE_HEATSPOT_WITH_ID:
                return GacorContract.HeatspotEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case CODE_PLACE:
                // Check that the name is not null
                String name = contentValues.getAsString(GacorContract.PlaceEntry.COLUMN_PLACE_NAME);
                if (name == null) {
                    throw new IllegalArgumentException("Place requires a name");
                }

                Double lat = contentValues.getAsDouble(GacorContract.PlaceEntry.COLUMN_PLACE_LAT);
                if (lat == null) {
                    throw new IllegalArgumentException("Place requires valid lattitude");
                }

                Double lang = contentValues.getAsDouble(GacorContract.PlaceEntry.COLUMN_PLACE_LANG);
                if (lang == null) {
                    throw new IllegalArgumentException("Place requires valid longitude");
                }

                // Insert the new place with the given contentValues
                long id = mOpenHelper.getWritableDatabase().insert(GacorContract.PlaceEntry.TABLE_NAME, null, contentValues);
                // If the ID is -1, then the insertion failed. Log an error and return null.
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }

                // Notify all listeners that the data has changed for the pet content URI
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the new URI with the ID (of the newly inserted row) appended at the end
                return ContentUris.withAppendedId(uri, id);

            case CODE_HEATSPOT:
                // Check that the name is not null
                if (contentValues.getAsString(GacorContract.HeatspotEntry.COLUMN_NAME) == null) {
                    throw new IllegalArgumentException("Place requires a name");
                }

                if (contentValues.getAsDouble(GacorContract.HeatspotEntry.COLUMN_LAT) == null) {
                    throw new IllegalArgumentException("Place requires valid lattitude");
                }

                if (contentValues.getAsDouble(GacorContract.HeatspotEntry.COLUMN_LANG) == null) {
                    throw new IllegalArgumentException("Place requires valid longitude");
                }

                // Insert the new place with the given contentValues
                long idHeatspot = mOpenHelper.getWritableDatabase().insert(GacorContract.HeatspotEntry.TABLE_NAME, null, contentValues);
                // If the ID is -1, then the insertion failed. Log an error and return null.
                if (idHeatspot == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }

                // Notify all listeners that the data has changed for the pet content URI
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the new URI with the ID (of the newly inserted row) appended at the end
                return ContentUris.withAppendedId(uri, idHeatspot);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_PLACE:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GacorContract.PlaceEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_PLACE_WITH_ID:
                // Delete a single row given by the ID in the URI
                selection = GacorContract.PlaceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GacorContract.PlaceEntry.TABLE_NAME, 
                        selection, 
                        selectionArgs);
                break;
            case CODE_HEATSPOT:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GacorContract.HeatspotEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_HEATSPOT_WITH_ID:
                // Delete a single row given by the ID in the URI
                selection = GacorContract.HeatspotEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GacorContract.HeatspotEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_PLACE:
                return updatePlace(uri, contentValues, selection, selectionArgs);
            case CODE_PLACE_WITH_ID:
                // For the CODE_PLACE_WITH_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = GacorContract.PlaceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePlace(uri, contentValues, selection, selectionArgs);
            case CODE_HEATSPOT:
                return updateHeatspot(uri, contentValues, selection, selectionArgs);
            case CODE_HEATSPOT_WITH_ID:
                // For the CODE_PLACE_WITH_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = GacorContract.HeatspotEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateHeatspot(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateHeatspot(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check the date
        if (values.containsKey(GacorContract.HeatspotEntry.COLUMN_DATE)) {
            long date = values.getAsLong(GacorContract.HeatspotEntry.COLUMN_DATE);
            if (date == 0) {
                throw new IllegalArgumentException("Time and Date required");
            }
        }

        // If the {@link PlaceEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(GacorContract.HeatspotEntry.COLUMN_NAME)) {
            String name = values.getAsString(GacorContract.HeatspotEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Place requires a name");
            }
        }

        // If the {@link PlaceEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(GacorContract.HeatspotEntry.COLUMN_LAT)) {
            Double lat = values.getAsDouble(GacorContract.HeatspotEntry.COLUMN_LAT);
            if (lat == null) {
                throw new IllegalArgumentException("Place requires lattitude");
            }
        }

        // If the {@link PlaceEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(GacorContract.HeatspotEntry.COLUMN_LANG)) {
            // Check that the lang is greater than or equal to 0 kg
            Double lang = values.getAsDouble(GacorContract.HeatspotEntry.COLUMN_LANG);
            if (lang ==null) {
                throw new IllegalArgumentException("Place requires longitude");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mOpenHelper.getWritableDatabase().update(GacorContract.HeatspotEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePlace(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PlaceEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(GacorContract.PlaceEntry.COLUMN_PLACE_NAME)) {
            String name = values.getAsString(GacorContract.PlaceEntry.COLUMN_PLACE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Place requires a name");
            }
        }

        // If the {@link PlaceEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(GacorContract.PlaceEntry.COLUMN_PLACE_LAT)) {
            Double lat = values.getAsDouble(GacorContract.PlaceEntry.COLUMN_PLACE_LAT);
            if (lat == null) {
                throw new IllegalArgumentException("Place requires lattitude");
            }
        }

        // If the {@link PlaceEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(GacorContract.PlaceEntry.COLUMN_PLACE_LANG)) {
            // Check that the lang is greater than or equal to 0 kg
            Double lang = values.getAsDouble(GacorContract.PlaceEntry.COLUMN_PLACE_LANG);
            if (lang ==null) {
                throw new IllegalArgumentException("Place requires longitude");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mOpenHelper.getWritableDatabase().update(GacorContract.PlaceEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

}
