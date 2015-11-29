package fr.esiea.kokumo.carnetdevoyages.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class UsersContentProvider extends ContentProvider {

    // URI
    public static final String PROVIDER_NAME = "fr.esiea.kokumo.carnetdevoyages";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + UsersDBHelper.DATABASE_TABLE);

    // Constantes pour identifier les requêtes
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/kokumo.carnetdevoyages" + UsersDBHelper.DATABASE_TABLE;
    private static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/kokumo/carnetdevoyages" + UsersDBHelper.DATABASE_TABLE;

    // Uri matcher
    private static final UriMatcher uriMatcher;
    private UsersDBHelper usersHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, UsersDBHelper.DATABASE_TABLE, ALLROWS);
        uriMatcher.addURI(PROVIDER_NAME, UsersDBHelper.DATABASE_TABLE + "/#", SINGLE_ROW);
    }

    /**
     * Create the database
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        this.usersHelper = new UsersDBHelper(
                getContext(),
                UsersDBHelper.DATABASE_NAME,
                null,
                UsersDBHelper.DATABASE_VERSION
        );
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.usersHelper.getWritableDatabase();
        String groupBy = null;
        String having = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UsersDBHelper.DATABASE_TABLE);
        switch(uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowId = uri.getLastPathSegment();
                queryBuilder.appendWhere(this.usersHelper.KEY_ID + "=" + rowId);
                break;
            case ALLROWS :

                break;
            default :
                break;
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,
                groupBy, having, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                return CONTENT_TYPE;
            case SINGLE_ROW:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.usersHelper.getWritableDatabase();
        String nullColumnHack = null;
        long id = db.insert(UsersDBHelper.DATABASE_TABLE, nullColumnHack, values);
        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = usersHelper .getWritableDatabase();
        switch (uriMatcher .match(uri)) {
            case SINGLE_ROW :
                String rowId = uri.getPathSegments().get(1);
                selection = UsersDBHelper.KEY_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" +
                        selection + ')' : "" );
            default :
                break;
        }
        if (selection == null ) {
            selection = "1";
        }
        int deleteCount = db.delete(UsersDBHelper.DATABASE_TABLE ,
                selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = usersHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW :
                String rowId = uri.getPathSegments().get(1);
                selection = UsersDBHelper.KEY_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" +
                        selection + ')' : "" );
            default :
                break;
        }
        if (selection == null ) {
            selection = "1";
        }
        int updateCount = db.update(UsersDBHelper.DATABASE_TABLE,
                values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}