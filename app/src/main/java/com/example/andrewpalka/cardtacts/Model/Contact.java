package com.example.andrewpalka.cardtacts.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrewpalka on 2/3/17.
 */

public class Contact implements Parcelable{


    private String TAG = Contact.class.getSimpleName();

    public String name, company, email, website;
    public Double favorite;
    public ImageURL imageURL;
    public Phone phone;
    public Address address;

    public Contact(JSONObject jsonObject) {

        try {

            name= jsonObject.getString("name");
            company=jsonObject.getString("company");
            email= jsonObject.getString("email");
            website= jsonObject.getString("website");


            imageURL = new ImageURL(jsonObject.getString("smallImageURL"),
                    jsonObject.getString("largeImageURL"));


            phone =  new Phone(jsonObject.getJSONObject("phone"));
            address = new Address(jsonObject.getJSONObject("address"));

        } catch (JSONException e) {

            e.printStackTrace();
        }


        try {

            favorite = jsonObject.getDouble("favorite");


        } catch (JSONException e) {

            try {

                Boolean bool = jsonObject.getBoolean("favorite");


                if (bool) {
                    favorite = 1d;
                } else {
                    favorite = 0d;
                }

            } catch (JSONException e1) {

                e1.printStackTrace();
                favorite = 0d;
            }

            e.printStackTrace();
        }

    }

    protected Contact(Parcel in) {

        name = in.readString();
        company = in.readString();
        email = in.readString();
        website = in.readString();
        favorite = in.readByte() == 0x00 ? null : in.readDouble();
        imageURL = (ImageURL) in.readValue(ImageURL.class.getClassLoader());
        phone = (Phone) in.readValue(Phone.class.getClassLoader());
        address = (Address) in.readValue(Address.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(email);
        dest.writeString(website);


        if (favorite == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(favorite);
        }


        dest.writeValue(imageURL);
        dest.writeValue(phone);
        dest.writeValue(address);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
