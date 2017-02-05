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

    public static class ImageURL implements Parcelable {
        private String smallImageURL, largeImageURL;

        ImageURL(String smallImageURL, String largeImageURL) {
            this.smallImageURL = smallImageURL;
            this.largeImageURL = largeImageURL;
        }

        public String getSmallImgURL() {
            return this.smallImageURL;
        }

        public String getLargeImgURL() {
            return this.largeImageURL;
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
        public static final Parcelable.Creator<ImageURL> CREATOR = new Parcelable.Creator<ImageURL>() {
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

    public static class Phone implements Parcelable {
        private String work, home, mobile;

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
                e.printStackTrace();
                this.mobile = "";
            }
        }

        public String getPhone(int type) {
            switch (type) {
                case 0:
                    return this.work;
                case 1:
                    return this.home;
                case 2:
                    return this.mobile;
                default:
                    return "";
            }
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

    public static class Address implements Parcelable {
        private String street, city, state, country, zip;
        private Double latitude, longitude;

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

        public String getAddress(int type) {
            switch (type) {
                case 0:
                    return this.street;
                case 1:
                    return this.city;
                case 2:
                    return this.state;
                case 3:
                    return this.country;
                case 4:
                    return this.zip;
                default:
                    return "";
            }
        }

        public Double getCoordinates(int type) {
            switch (type) {
                case 0:
                    return this.latitude;
                case 1:
                    return this.longitude;
                default:
                    return 0.0;
            }
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
        public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
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

    public Contact(JSONObject jsonObject) {

        try {

            this.name= jsonObject.getString("name");
            this.company=jsonObject.getString("company");
            this.email= jsonObject.getString("email");
            this.website= jsonObject.getString("website");

            this.imageURL = new ImageURL(jsonObject.getString("smallImageURL"),
                    jsonObject.getString("largeImageURL"));

            this.phone =  new Phone(jsonObject.getJSONObject("phone"));

            this.address = new Address(jsonObject.getJSONObject("address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            this.favorite = jsonObject.getDouble("favorite");
        } catch (JSONException e) {
            try {
                Boolean bool = jsonObject.getBoolean("favorite");

                if (bool) {
                    this.favorite = 1d;
                } else {
                    this.favorite = 0d;
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
                this.favorite = 0d;
            }

            e.printStackTrace();
        }

        Log.d(TAG, "Contact: " + this.favorite);

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
