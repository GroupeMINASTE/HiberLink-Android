package me.nathanfallet.hiberlink.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    // Static instance
    private static Database instance;

    // Initialize
    private Database(Context context) {
        super(context, "HiberLink", null, 1);
    }

    public static synchronized Database getInstance(Context context) {
        // Use the application context
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    // Called when the database connection is being configured
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    // Called when the database is created (first time only)
    public void onCreate(SQLiteDatabase db) {
        // Initialize tables
        db.execSQL("CREATE TABLE links ('short' VARCHAR UNIQUE NOT NULL, 'full' VARCHAR NOT NULL, 'generated' LONG NOT NULL);");
    }

    // Called when the database needs to be upgraded
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing here
    }

    // Get links
    public List<Pair<String, String>> getLinks() {
        // Initialize an array
        List<Pair<String, String>> list = new ArrayList<>();

        // Create a SQL query
        String query = "SELECT * FROM links ORDER BY generated DESC";

        // Get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Iterate data
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Create link in list
                    list.add(Pair.with(cursor.getString(cursor.getColumnIndex("short")), cursor.getString(cursor.getColumnIndex("full"))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("HIBERLINK", "Error while trying to get links from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        // Return found algorithms
        return list;
    }

    // Add a link
    public void addLink(Pair<String, String> link) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // Get data
            ContentValues values = new ContentValues();
            values.put("short", link.getValue0());
            values.put("full", link.getValue1());
            values.put("generated", new Date().getTime());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow("links", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("HIBERLINK", "Error while trying to add a link to database");
        } finally {
            db.endTransaction();
        }
    }

    // Clear history
    public void clearHistory() {
        // Get database
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete data
            db.delete("links", null, new String[]{});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("HIBERLINK", "Error while trying to delete links");
        } finally {
            db.endTransaction();
        }
    }

}
