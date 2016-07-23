package com.masdika.practice.vollone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by blacknaml on 23/07/16.
 */
public class MoviesOpenHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "dilo_db";
    private final static int DATABASE_VERSION = 1;

    public MoviesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesTable.TABLE_NAME + "( " +
                MoviesContract.MoviesTable._ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesContract.MoviesTable.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MoviesContract.MoviesTable.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                MoviesContract.MoviesTable.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                " UNIQUE ( " + MoviesContract.MoviesTable._ID + " ) ON CONFLICT REPLACE );";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+ MoviesContract.MoviesTable.TABLE_NAME);
        onCreate(db);
    }
}
