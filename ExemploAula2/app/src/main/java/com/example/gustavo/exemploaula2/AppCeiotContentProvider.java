package com.example.gustavo.exemploaula2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AppCeiotContentProvider extends ContentProvider {

    public static final int NODES = 0;
    public static final int NODE_WITH_ID = 1;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NodeContract.AUTHORITY, NodeContract.PATH_NODES, NODES);
        uriMatcher.addURI(NodeContract.AUTHORITY, NodeContract.PATH_NODES + "/#", NODE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        //Inicializa ContentProvider
        Context context = getContext();
        mDbHelper = new DbHelper(context);
        return true;
    }

    /**
     * Implementação do método que realiza Consultas no SQLite.
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Recupera qual funcionalidade de acordo com a URL;
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case NODES:
                retCursor = db.query(NodeContract.Node.TABLE_NAME,
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
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Implementação do método que realiza o Inserts no SQLite.
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NODES:
                long id = db.insert(NodeContract.Node.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(NodeContract.Node.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /**
     * Implementação do método que realiza o deletes no SQLite.
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case NODE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(NodeContract.Node.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
