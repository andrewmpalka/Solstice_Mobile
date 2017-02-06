package com.example.andrewpalka.cardtacts.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andrewpalka on 2/5/17.
 */

public class ImageURL implements Parcelable {
    private String smallImageURL, largeImageURL;

    ImageURL(String smallImageURL, String largeImageURL) {

        this.smallImageURL = smallImageURL;
        this.largeImageURL = largeImageURL;

    }

    // This image is incredibly small :(
    @SuppressWarnings("unused")
    public String getSmallImgURL() {
        return smallImageURL;
    }

    public String getLargeImgURL() {
        return largeImageURL;
    }


    protected ImageURL(Parcel in) {

        smallImageURL = in.readString();
        largeImageURL = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(smallImageURL);
        dest.writeString(largeImageURL);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ImageURL> CREATOR =
            new Parcelable.Creator<ImageURL>
                    () {
                @Override
                public ImageURL createFromParcel(Parcel in) {
                    return new ImageURL(in);
                }

                @Override
                public ImageURL[] newArray(int size) {
                    return new ImageURL[size];
                }
            };
}

