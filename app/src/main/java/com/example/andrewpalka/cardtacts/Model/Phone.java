package com.example.andrewpalka.cardtacts.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrewpalka on 2/5/17.
 */

public class Phone implements Parcelable {
    private String work, home, mobile;

    Phone(JSONObject jsonObject) {

        try {

            work = jsonObject.getString("work");
            home = jsonObject.getString("home");

        } catch (JSONException e) {

            e.printStackTrace();
        }

        try {

            mobile = jsonObject.getString("mobile");

        } catch (JSONException e) {

            e.printStackTrace();
            mobile = "";
        }
    }

    public String getWork() {

        return work;

    }

    public String getHome() {

        return home;

    }

    public String getMobile() {

        return mobile;

    }

    protected Phone(Parcel in) {

        work = in.readString();
        home = in.readString();
        mobile = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(work);
        dest.writeString(home);
        dest.writeString(mobile);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Phone> CREATOR = new Parcelable.Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel in) {
            return new Phone(in);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}

