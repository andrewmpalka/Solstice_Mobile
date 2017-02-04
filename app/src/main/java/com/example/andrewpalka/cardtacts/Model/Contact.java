package com.example.andrewpalka.cardtacts.Model;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrewpalka on 2/3/17.
 */

public class Contact {
    public String name;
    public String company;
    public String favorite;
    public String email;
    public String website;
    public ImageURL imageURL;
    public Phone phone;
    public Address address;

    private class ImageURL {
        private String smallImageURL;
        private String largeImageURL;

        ImageURL(String smallImageURL, String largeImageURL) {
            this.smallImageURL = smallImageURL;
            this.largeImageURL = largeImageURL;
        }
    }

    private class Phone {
        private String work;
        private String home;
        private String mobile;

        Phone(JSONObject jsonObject) {
            try {

                this.work = jsonObject.getString("work");
                this.home = jsonObject.getString("home");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                this.mobile = jsonObject.getString("mobile");
            } catch (JSONException e) {
//                e.printStackTrace();
                this.mobile = "";
            }
        }
    }

    private class Address {
        private String street;
        private String city;
        private String state;
        private String country;
        private String zip;
        private Double latitude;
        private Double longitude;

        Address(JSONObject jsonObject) {

            try {

                this.street = jsonObject.getString("street");
                this.city = jsonObject.getString("city");
                this.state = jsonObject.getString("state");
                this.country = jsonObject.getString("country");
                this.zip = jsonObject.getString("zip");
                this.latitude = jsonObject.getDouble("latitude");
                this.longitude = jsonObject.getDouble("longitude");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public Contact(JSONObject jsonObject) {


        try {

        this.name= jsonObject.getString("name");
        this.company=jsonObject.getString("company");
        this.favorite= jsonObject.getString("favorite");
        this.email= jsonObject.getString("email");
        this.website= jsonObject.getString("website");

            this.imageURL = new ImageURL(jsonObject.getString("smallImageURL"),
                    jsonObject.getString("largeImageURL"));

            this.phone =  new Phone(jsonObject.getJSONObject("phone"));

            this.address = new Address(jsonObject.getJSONObject("address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
