package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewpalka.cardtacts.Model.Contact;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String TAG = DetailActivity.class.getSimpleName();


    private final Context context = DetailActivity.this;

    private Contact mContact;

    private ImageView contactImage;
    private TextView nameFill, companyFill;
    private TextView workPhone, homePhone, mobilePhone, mobilePhone_HEAD;
    private TextView emailFill, websiteFill;
    private TextView street, city_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // PROFILE SECTION
        contactImage = (ImageView) findViewById(R.id.iv_detail_image);
        nameFill = (TextView) findViewById(R.id.tv_name_fill);
        companyFill = (TextView) findViewById(R.id.tv_company_fill);


        // PHONE SECTION
        workPhone = (TextView) findViewById(R.id.tv_phone_work);
        homePhone = (TextView) findViewById(R.id.ET_phone_home);
        mobilePhone = (TextView) findViewById(R.id.tv_phone_mobile);
        mobilePhone_HEAD = (TextView) findViewById(R.id.tv_phone_mobile_head);


        // INTERNET SECTION
        emailFill = (TextView) findViewById(R.id.tv_email_fill);
        websiteFill = (TextView) findViewById(R.id.tv_website_fill);


        // ADDRESS SECTION
        street = (TextView) findViewById(R.id.tv_address_street);
        city_state = (TextView) findViewById(R.id.tv_address_city_state);


        // Get referenec to intent from MainActivity
        Intent IntentThatStartedThisActivity = getIntent();


        if (IntentThatStartedThisActivity != null) {
            if (IntentThatStartedThisActivity.hasExtra("CONTACT_DETAILS")) {
                Contact contact = IntentThatStartedThisActivity.getParcelableExtra("CONTACT_DETAILS");

                mContact = contact;
                String[] splitStr = contact.name.trim().split("\\s+");
                setTitle(splitStr[0]);


                fillInContactInfo(contact);
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendEmail(view);
            }
        });
    }


    private void fillInContactInfo(Contact contact) {
        // PROFILE SECTION
        Picasso.with(context)
                .load(contact.imageURL.getLargeImgURL())
                .placeholder(R.drawable.ic_action_movie)
                .resize(400,400)
                .into(contactImage);
        nameFill.setText(contact.name);
        companyFill.setText(contact.company);


        // PHONE SECTION
        workPhone.setText(contact.phone.getPhone(0));
        homePhone.setText(contact.phone.getPhone(1));
        if (contact.phone.getPhone(2).equals("")) {
            mobilePhone.setVisibility(View.INVISIBLE);
            mobilePhone_HEAD.setVisibility(View.INVISIBLE);
        } else {
            mobilePhone.setText(contact.phone.getPhone(2));
        }


        // INTERNET SECTION
        emailFill.setText(contact.email);
        String[] splitStr = contact.website.split("://");
        websiteFill.setText(splitStr[1]);


        // ADDRESS SECTION
        street.setText(contact.address.getAddress(0));
        String stringConcat = contact.address.getAddress(1) + ", " + contact.address.getAddress(2)
                + "  " + contact.address.getAddress(4);
        city_state.setText(stringConcat);

    }

    //region ONCLICK METHODS
    // PHONE SECTION
    public void callWork(View v) {

        String[] splitStr = mContact.phone.getPhone(0).split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];


        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);

        /*
         * This is a check we perform with every implicit Intent that we launch. In some cases,
         * the device where this code is running might not have an Activity to perform the action
         * with the data we've specified. Without this check, in those cases your app would crash.
         */

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }
    public void callHome(View v) {

        String[] splitStr = mContact.phone.getPhone(1).split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];

        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }
    public void callMobile(View v) {

        String[] splitStr = mContact.phone.getPhone(2).split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];


        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }


    // INTERNET SECTION
    public void sendEmail(View v) {

        String[] addresses  = new String[] { mContact.email };

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mContact.email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reference Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Andy Palka has listed you as a reference." +
                " Can you tell us about your experience with him?");
        // Needed for 4.2/4.3/4.4 Support
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(Intent
                .createChooser(intent, "Send email..."));
    }
    public void goToWebsite(View v) {

        Uri uri = Uri.parse(mContact.website);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);
    }


    // ADDRESS SECTION
    public void navigate(View v) {

        String lat = mContact.address.getCoordinates(0).toString();
        String lon = mContact.address.getCoordinates(1).toString();

        String query = mContact.address.getAddress(0) +" " +mContact.address.getAddress(1)
                + ", " + mContact.address.getAddress(2);


        Uri uri = Uri.parse("geo:"+ lat + "," + lon  + "?q=" + query);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if(intent.resolveActivity(getPackageManager()) != null) startActivity(intent);

    }


    // FAVORITE TOOLBAR
    public void favoriteClicked(MenuItem m) {

    }
    //endregion


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(menu.getItem(0).getItemId() == R.id.action_favorite) {

            menu.findItem(R.id.action_favorite).setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        Log.d(TAG, "onCreateOptionsMenu: " + mContact.favorite);

        if(mContact.favorite == 0d) {
            menu.getItem(0).setIcon(R.drawable.ic_action_favorite_empty);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_action_favorite_full);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_favorite) {


            item.setIcon(R.drawable.ic_action_favorite_full);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
