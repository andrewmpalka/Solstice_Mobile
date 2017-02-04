package com.example.andrewpalka.cardtacts.Model;

/**
 * Created by andrewpalka on 2/3/17.
 */

public class Data {
    public String title;
    public String description;
    public int imageId;
    public boolean favorite;

    public Data(String title, String description, int imageId, boolean favorite) {
        this.title = title;
        this.description = description;
        this.imageId = imageId;
        this.favorite = favorite;
    }

}
