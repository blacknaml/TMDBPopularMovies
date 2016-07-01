package com.masdika.practice.vollone;

import android.app.DownloadManager;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

                        List<MovieItem> result = parseJson(response);

                        if(result != null){
                            mAdapter.clear();
                            mAdapter.addAll(result);
                        }
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
        List<MovieItem> resultMovies = new ArrayList<>();

        try {
            JSONObject objResponse = new JSONObject(response);
            JSONArray resultArray = objResponse.getJSONArray(TMDB_RESULT);

            int arraySize = resultArray.length();
            for(int i=0; i < arraySize; i++){
                JSONObject movieObject = resultArray.getJSONObject(i);
                String urlImage = movieObject.getString(TMDB_URL_IMAGE);
                double voteMovie = movieObject.getDouble(TMDB_VOTE);

                MovieItem movieItem = new MovieItem(urlImage, voteMovie);

                resultMovies.add(movieItem);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return resultMovies;
    }
}
