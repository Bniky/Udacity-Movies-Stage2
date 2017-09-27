package com.bniky.nicholas.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicholas on 22/09/2017.
 */

public class MovieImageData implements Parcelable {
    private String image;
    private String title;
    private int id;

    public MovieImageData(String image, String title, int id){
        this.image = image;
        this.title = title;
        this.id = id;
    }

    private MovieImageData(Parcel in){
        image = in.readString();
        title = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public static final Creator<MovieImageData> CREATOR = new Creator<MovieImageData>() {
        @Override
        public MovieImageData createFromParcel(Parcel in) {
            return new MovieImageData(in);
        }

        @Override
        public MovieImageData[] newArray(int size) {
            return new MovieImageData[size];
        }
    };
}
