package com.bniky.nicholas.movies;

/**
 * Created by Nicholas on 22/09/2017.
 */

public class MovieImageData {
    private String image;
    private String title;
    private int id;

    public MovieImageData(String image, String title, int id){
        this.image = image;
        this.title = title;
        this.id = id;
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
}
