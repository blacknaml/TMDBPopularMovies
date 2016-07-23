package com.masdika.practice.vollone;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.masdika.practice.vollone.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?";
    final String API_KEY = "796b78a940cdb3ba4ac81d7d423b34a6";

    List<MovieItem> mMovies = new ArrayList<>();
    MoviesAdapter mAdapter;
    private GridView gvMovies;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        View emptyView = findViewById(R.id.emptyView);

        gvMovies = (GridView) findViewById(R.id.gvMovies);
        gvMovies.setEmptyView(emptyView);
        mAdapter = new MoviesAdapter(this, mMovies);
        gvMovies.setAdapter(mAdapter);
        gvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon().appendQueryParameter("api_key", API_KEY).build();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response : " + response);

                        /*List<MovieItem> result = parseJson(response);

                        if(result != null){
                            mAdapter.clear();
                            mAdapter.addAll(result);
                        }*/
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                }
        );

        queue.add(stringRequest);
    }

    private List<MovieItem> parseJson(String response) {
        final String TMDB_RESULT = "results";
        final String TMDB_URL_IMAGE = "poster_path";
        final String TMDB_VOTE = "vote_average";
        final String IMDB_ID = "id";
        final String IMDB_OVERVIEW = "overview";
        final String IMDB_TITLE = "original_title";

        List<MovieItem> resultMovies = new ArrayList<>();

        try {
            JSONObject objResponse = new JSONObject(response);
            JSONArray resultArray = objResponse.getJSONArray(TMDB_RESULT);

            int arraySize = resultArray.length();
            Vector<ContentValues> csVector = new Vector<>(arraySize);

            for(int i=0; i < arraySize; i++){
                JSONObject movieObject = resultArray.getJSONObject(i);
                String urlImage = movieObject.getString(TMDB_URL_IMAGE);
                double voteMovie = movieObject.getDouble(TMDB_VOTE);
                int id = movieObject.getInt(IMDB_ID);
                String overview = movieObject.getString(IMDB_OVERVIEW);
                String title = movieObject.getString(IMDB_TITLE);

                ContentValues value = new ContentValues();
                value.put(MoviesContract.MoviesTable._ID, id);
                value.put(MoviesContract.MoviesTable.COLUMN_ORIGINAL_TITLE, title);
                value.put(MoviesContract.MoviesTable.COLUMN_POSTER_PATH, urlImage);
                value.put(MoviesContract.MoviesTable.COLUMN_OVERVIEW, overview);
                value.put(MoviesContract.MoviesTable.COLUMN_VOTE_AVERAGE, voteMovie);

                csVector.add(value);

                /*MovieItem movieItem = new MovieItem(urlImage, voteMovie);
                resultMovies.add(movieItem);*/
            }

            if(csVector.size() > 0){
                ContentValues[] cvArray = new ContentValues[csVector.size()];
                csVector.toArray(cvArray);

                getContentResolver().bulkInsert(MoviesContract.MoviesTable.CONTENT_URI, cvArray);
            }

            getContentResolver().query(MoviesContract.MoviesTable.CONTENT_URI, null, null, null, null);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return resultMovies;
    }
}
