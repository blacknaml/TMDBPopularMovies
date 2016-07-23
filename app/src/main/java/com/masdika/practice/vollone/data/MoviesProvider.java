package com.masdika.practice.vollone.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by blacknaml on 23/07/16.
 */
public class MoviesProvider extends ContentProvider {

    MoviesOpenHelper mMoviesOpenHelper;

    private final static int MOVIES = 100;
    private final static int MOVIES_WITH_ID = 101;

    final UriMatcher mUriMatcher;
    private final static SQLiteQueryBuilder mMoviesQueryBuilder;

    static {
        mMoviesQueryBuilder = new SQLiteQueryBuilder();
        mMoviesQueryBuilder.setTables((MoviesContract.MoviesTable.TABLE_NAME));
    }

    private final static String mSelection = MoviesContract.MoviesTable.TABLE_NAME + "." + MoviesContract.MoviesTable._ID + "=?";

    public MoviesProvider(UriMatcher mUriMatcher) {
        this.mUriMatcher = mUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesOpenHelper = new MoviesOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        int match = mUriMatcher.match(uri);
        switch(match){
            case MOVIES:
                retCursor = mMoviesQueryBuilder.query(
                        mMoviesOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_WITH_ID:
                retCursor = getMovieById(
                        uri,
                        projection,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Uri : " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mMoviesOpenHelper.getWritableDatabase();
        int retCount = 0;
        int match = mUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesTable.TABLE_NAME, null, value);
                        if (_id != -1) {
                            retCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return retCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private UriMatcher buildUriMatcher(Uri uri){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/*", MOVIES_WITH_ID);

        return uriMatcher;
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder){
        long id = MoviesContract.MoviesTable.getIdFromUri(uri);

        Cursor retCursor = mMoviesQueryBuilder.query(
                mMoviesOpenHelper.getReadableDatabase(),
                projection,
                mSelection,
                new String[]{String.valueOf(id)},
                null,
                null,
                sortOrder
        );

        return retCursor;
    }
}
