package com.example.andrewpalka.cardtacts.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrewpalka on 2/5/17.
 */

public class Address implements Parcelable {
    private String street, city, state, country, zip;
    private Double latitude, longitude;

    Address(JSONObject jsonObject) {

        try {

            street = jsonObject.getString("street");
            city = jsonObject.getString("city");
            state = jsonObject.getString("state");
            country = jsonObject.getString("country");
            zip = jsonObject.getString("zip");
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }


    public String getStreet() {

        return street;

    }

    public String getCity() {

        return  city;

    }

    public String getState() {

        return state;

    }

    public String getCountry() {

        return country;

    }

    public String getZip() {

        return zip;

    }

    public Double getLatitude() {

        return  latitude;

    }

    public Double getLongitude() {

        return longitude;

    }

    protected Address(Parcel in) {

        street = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zip = in.readString();


        latitude = in.readByte() == 0x00 ? null : in.readDouble();
        longitude = in.readByte() == 0x00 ? null : in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zip);


        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitude);
        }


        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitude);
        }


    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>()
    {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}

