package fr.esiea.kokumo.carnetdevoyages.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDBHelper extends SQLiteOpenHelper {

    public static final String KEY_ID = "user_id";
    public static final String KEY_LOGIN = "user_login";
    public static final String KEY_PASSWORD = "user_password";
    public static final String DATABASE_NAME = "CarnetDeVoyages";
    public static final String DATABASE_TABLE = "Users";
    public static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
            " CREATE TABLE " + DATABASE_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_LOGIN + " TEXT NOT NULL UNIQUE, "
            + KEY_PASSWORD + " TEXT NOT NULL "
            + " ) ";

    public UsersDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Create the table Users
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the query DATABASE_CREATE
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Upgrade the database
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Warning
        Log.w("DATABASE", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + " : toutes les données seront perdues.");
        // Deletion of Users table
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // Creation (again) of Users table
        onCreate(db);
    }
}
