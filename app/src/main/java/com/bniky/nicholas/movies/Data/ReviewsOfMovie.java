package com.bniky.nicholas.movies.Data;

/**
 * Created by Nicholas on 08/12/2017.
 */

public class ReviewsOfMovie {

    private String name ;
    private String content;

    public ReviewsOfMovie(String name, String content){
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
