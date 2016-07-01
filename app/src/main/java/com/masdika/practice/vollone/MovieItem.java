package com.masdika.practice.vollone;

/**
 * Created by blacknaml on 01/07/16.
 */
public class MovieItem {

    private String urlImage;
    private double vote;

    public MovieItem(String urlImage, double vote){
        this.urlImage = urlImage;
        this.vote = vote;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public double getVote() {
        return vote;
    }
}
