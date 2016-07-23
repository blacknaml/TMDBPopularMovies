package com.masdika.practice.vollone.data;

import android.provider.BaseColumns;

/**
 * Created by blacknaml on 23/07/16.
 */
public class MoviesContract {
    public final static String CONTENT_AUTHORITY = "com.masdika.practive.vollone";
    
    public final static class MoviesTable implements BaseColumns {

        public final static String TABLE_NAME = "movies";

        public final static String COLUMN_POSTER_PATH = "poster_path";
        public final static String COLUMN_VOTE_AVERAGE = "vote_average";
        public final static String COLUMN_ORIGINAL_TITLE = "original_title";
        public final static String COLUMN_OVERVIEW = "overview";


    }
}
