package com.example.gustavo.exemploaula2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "appceiot.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + NodeContract.Node.TABLE_NAME + " (" +
                NodeContract.Node._ID                + " INTEGER PRIMARY KEY, " +
                NodeContract.Node.COLUMN_NAME        + " TEXT NOT NULL, " +
                NodeContract.Node.COLUMN_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NodeContract.Node.TABLE_NAME);
        onCreate(db);
    }
}
