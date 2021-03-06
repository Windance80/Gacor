package com.kingstone.smith.gacor.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class GacorContract {
    private GacorContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.kingstone.smith.gacor";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PLACE = "places";
    public static final String PATH_HEATSPOT = "heatsport";


    public static final class PlaceEntry implements BaseColumns {
        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLACE);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of places.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single place.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;

        /** TABLE */
        public final static String TABLE_NAME = "places";

        /** FIELD**/
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PLACE_NAME ="place";
        public final static String COLUMN_PLACE_DETAIL = "detail";
        public final static String COLUMN_PLACE_LAT = "lat";
        public final static String COLUMN_PLACE_LANG = "lang";
    }

    public static final class HeatspotEntry implements BaseColumns {
        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HEATSPOT);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of places.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEATSPOT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single place.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEATSPOT;

        /** TABLE */
        public final static String TABLE_NAME = "heatspot";

        /** FIELD**/
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE ="datetime";
        public final static String COLUMN_NAME ="place";
        public final static String COLUMN_DETAIL = "detail";
        public final static String COLUMN_LAT = "lat";
        public final static String COLUMN_LANG = "lang";
    }
}
