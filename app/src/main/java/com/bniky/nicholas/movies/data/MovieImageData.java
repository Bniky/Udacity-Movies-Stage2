package com.bniky.nicholas.movies.data;


/**
 * Created by Nicholas on 22/09/2017.
 */

public class MovieImageData {
    private int id;
    private String title;
    private String image;

    private double vote_average;
    private String backDrop;
    private String overView;
    private String releaseOfFilm;

    public MovieImageData(String image, String title, int id, double vote_average, String backDrop, String overView, String releaseOfFilm){
        this.image = image;
        this.title = title;
        this.id = id;
        this.vote_average = vote_average;
        this.backDrop = backDrop;
        this.overView = overView;
        this.releaseOfFilm = releaseOfFilm;
    }



    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public double getVote_average() { return vote_average; }

    public String getBackDrop() { return backDrop;}

    public String getOverView() { return overView; }

    public String getReleaseOfFilm() { return releaseOfFilm; }

}
