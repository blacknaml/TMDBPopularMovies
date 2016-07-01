package com.masdika.practice.vollone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by blacknaml on 01/07/16.
 */
public class MoviesAdapter extends ArrayAdapter<MovieItem> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();
    final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    public MoviesAdapter(Context context, List<MovieItem> movies){
        super(context, R.layout.movie_grid_item, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);
        MovieItem movieItem = getItem(position);
        String imageUrl = IMAGE_URL + movieItem.getUrlImage();
        double voteValue = movieItem.getVote();

        ImageView ivMovie = (ImageView) view.findViewById(R.id.ivMovie);
        TextView tvVote = (TextView) view.findViewById(R.id.tvVote);

        Picasso.with(getContext()).load(imageUrl).into(ivMovie);

        tvVote.setText(String.valueOf(voteValue));

        return view;
    }
}

