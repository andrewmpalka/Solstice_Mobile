package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    private final Context mContext = DetailActivity.this;

    private Contact mContact;

    private ImageView mContactImage;
    private TextView mNameFill, mCompanyFill;
    private TextView mWorkPhone, mHomePhone, mMobilePhone, mMobilePhone_HEAD;
    private TextView mEmailFill, mWebsiteFill;
    private TextView mStreet, mCityStateZip;


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("contact", mContact);


        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        // restore saved Contact
        mContact = savedInstanceState.getParcelable("contact");


        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // PROFILE SECTION
        mContactImage = (ImageView) findViewById(R.id.iv_detail_image);
        mNameFill = (TextView) findViewById(R.id.tv_name_fill);
        mCompanyFill = (TextView) findViewById(R.id.tv_company_fill);


        // PHONE SECTION
        mWorkPhone = (TextView) findViewById(R.id.tv_phone_work);
        mHomePhone = (TextView) findViewById(R.id.ET_phone_home);
        mMobilePhone = (TextView) findViewById(R.id.tv_phone_mobile);
        mMobilePhone_HEAD= (TextView) findViewById(R.id.tv_phone_mobile_head);


        // INTERNET SECTION
        mEmailFill= (TextView) findViewById(R.id.tv_email_fill);
        mWebsiteFill = (TextView) findViewById(R.id.tv_website_fill);


        // ADDRESS SECTION
        mStreet = (TextView) findViewById(R.id.tv_address_street);
        mCityStateZip = (TextView) findViewById(R.id.tv_address_city_state);
        
        
        // Get reference to intent from MainActivity
        Intent IntentThatStartedThisActivity = getIntent();


        if (IntentThatStartedThisActivity != null) {

            if (IntentThatStartedThisActivity.hasExtra("CONTACT_DETAILS")) {

                Contact contact = IntentThatStartedThisActivity
                        .getParcelableExtra("CONTACT_DETAILS");
                mContact = contact;
                String[] splitStr = contact.name.trim().split("\\s+");
                setTitle(splitStr[0]);


                fillInContactInfo(contact);

            }

        }

        // Last minute fix since Picasso scales placeholder on Josephine
        if(mContact.imageURL.getLargeImgURL().equals("https://s3.amazonaws.com/technical-challenge/images/image12_large.jpeg")) {

            mContactImage.setPadding(180,150,180,150);
            
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
        Picasso.with(mContext)
                .load(contact.imageURL.getLargeImgURL())
                .placeholder(R.drawable.ic_action_name_lg)
                .resize(400,400)
                .into(mContactImage);
        mNameFill.setText(contact.name);
        mCompanyFill.setText(contact.company);


        // PHONE SECTION
        String[] splitStrWork = contact.phone.getWork().split("-");
        String[] splitStrHome = contact.phone.getHome().split("-");

        String sWork = "(" + splitStrWork[0] + ") " + splitStrWork[1] + "-" + splitStrWork[2];
        String sHome = "(" + splitStrHome[0] + ") " + splitStrHome[1] + "-" + splitStrHome[2];

        mWorkPhone.setText(sWork);
        mHomePhone.setText(sHome);

        if (contact.phone.getMobile().equals("")) {

            mMobilePhone.setVisibility(View.INVISIBLE);
            mMobilePhone_HEAD.setVisibility(View.INVISIBLE);

        } else {

            String[] splitStrMobile = contact.phone.getMobile().split("-");
            String sMobile = "(" + splitStrMobile[0] + ") " + splitStrMobile[1] + "-" + splitStrMobile[2];

            mMobilePhone.setText(sMobile);

        }


        // INTERNET SECTION
        mEmailFill.setText(contact.email);
        String[] splitStr = contact.website.split("://");
        mWebsiteFill.setText(splitStr[1]);


        // ADDRESS SECTION
        mStreet.setText(contact.address.getStreet());
        String stringConcat = contact.address.getCity() + ", " + contact.address.getState()
                + "  " + contact.address.getZip();
        mCityStateZip.setText(stringConcat);

    }

    //region ONCLICK METHODS
    // PHONE SECTION
    public void callWork(View v) {

        String[] splitStr = mContact.phone.getWork().split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];


        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);

        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }
    public void callHome(View v) {

        String[] splitStr = mContact.phone.getHome().split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];


        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);


        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }
    public void callMobile(View v) {

        String[] splitStr = mContact.phone.getMobile().split("-");
        String phoneNumber = splitStr[0] + splitStr[1] + splitStr[2];


        Uri uri = Uri.parse("tel: (+1)" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);


        if (intent.resolveActivity(this.getPackageManager()) != null ) startActivity(intent);

    }


    // INTERNET SECTION
    public void sendEmail(View v) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mContact.email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reference Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Andy Palka has listed you as a reference." +
                " Can you tell us about your experience with him?");


        // Needed for 4.2/4.3/4.4 Support
        String[] addresses  = new String[] { mContact.email };
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

        String lat = mContact.address.getLatitude().toString();
        String lon = mContact.address.getLongitude().toString();


        String query = mContact.address.getStreet() +" " +mContact.address.getCity()
                + ", " + mContact.address.getState();


        Uri uri = Uri.parse("geo:"+ lat + "," + lon  + "?q=" + query);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");


        if(intent.resolveActivity(getPackageManager()) != null) startActivity(intent);

    }

    //endregion


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Get favorites menu item, make it unclickable
        if(menu.getItem(0).getItemId() == R.id.action_favorite) {
            menu.findItem(R.id.action_favorite).setEnabled(false);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);


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

        return super.onOptionsItemSelected(item);
    }
}
