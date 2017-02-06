package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewpalka.cardtacts.Model.Contact;
import com.example.andrewpalka.cardtacts.Utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements ContactListRecyclerViewAdapter.AdapterOnClickHandler,
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Contact>> {

    private Boolean mFavoriteToggle = false;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageDisplay;

    private RecyclerView mRecyclerView;
    private ContactListRecyclerViewAdapter mRecyclerViewAdapter;

    private String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Contact> mFullContactList;
    private ArrayList<Contact> mFavoriteContactList;

    private static final int CONTACT_LOADER_ID = 0;

    @SuppressWarnings("deprecation")
    private boolean haveNetworkConnection() {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        // closest thing to fast enumeration :)
        for (NetworkInfo ni : netInfo) {

            if (ni.getTypeName().equalsIgnoreCase("WIFI"))

                if (ni.isConnected())

                    haveConnectedWifi = true;

            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))

                if (ni.isConnected())

                    haveConnectedMobile = true;

        }

        return haveConnectedWifi || haveConnectedMobile;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("size", mFullContactList.size());

        for (int i = 0; i < mFullContactList.size(); i++) {
            String key = "contact_" + i;
            outState.putParcelable(key, mFullContactList.get(i));
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int s = savedInstanceState.getInt("size");

        ArrayList<Contact> contactArrayList = new ArrayList<>();

        for (int i = 0; i < s; i++) {
            String key = "contact_" + i;
            Contact c = savedInstanceState.getParcelable(key);
            contactArrayList.add(c);
        }

        mFullContactList = contactArrayList;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //grabs reference to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mFullContactList = new ArrayList<Contact>();
        mFavoriteContactList = new ArrayList<Contact>();


        mFavoriteToggle = true;


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // Set adapter to custom RecyclerView.Adapter with loaded dataset
        mRecyclerViewAdapter = new ContactListRecyclerViewAdapter(getApplication(),this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // This setting improves performance regarding the the child layout size in the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        loadContactData();

    }

    private void loadContactData() {

        // This ID will uniquely identify the Loader
        // Used to get a handle on Loader through the support LoaderManager.
        int loaderId = CONTACT_LOADER_ID;


        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Contact>> callback
                = MainActivity.this;

        // unused for convenience
        Bundle bundleForLoader = null;

        // ensures a loader is active
        if(haveNetworkConnection()) {

            getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);


            showRecyclerView();

        } else {

            Toast.makeText(getApplicationContext(),
                    "There seems to be an issue with your connection!",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void showRecyclerView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);


        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);


        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    //region CustomAsyncTaskLoader

    @Override
    public Loader<ArrayList<Contact>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Contact>>(this) {

                ArrayList<Contact> mContactData = null;


             // Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
            @Override
            protected void onStartLoading() {

                if (mContactData != null) {

                    deliverResult(mContactData);

                } else {

                    mContactData = new ArrayList<Contact>();
                    forceLoad();
                    mProgressBar.setVisibility(View.VISIBLE);

                }

                super.onStartLoading();

            }


            @Override
            public ArrayList<Contact> loadInBackground() {

                HttpHandler urlConnection = new HttpHandler();


                String endpointUrl = "https://s3.amazonaws.com/technical-challenge/Contacts.json";

                // Making a request to url and getting response
                String jsonStr = urlConnection.makeServiceCall(endpointUrl);


                Log.e(TAG, "Response from url: " + jsonStr);


                if (jsonStr != null) {

                    try {

                        JSONArray results = new JSONArray(jsonStr);

                        // looping through All Contacts
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject r = results.getJSONObject(i);
                            Contact contact = new Contact(r);
                            mContactData.add(contact);

                        }

                    } catch (final Exception e) {

                        Log.e(TAG, "JSON parsing error: " + e.getMessage());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                        });

                        return null;
                    }

                } else {

                    Log.e(TAG, "Couldn't get json from server.");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(),
                                    "There seems to be an issue with your connection!",
                                    Toast.LENGTH_LONG)
                                    .show();

                        }
                    });

                    return null;

                }

                return mContactData;

            }

            @Override
            public void deliverResult(ArrayList<Contact> data) {

                mContactData = data;

                if(null == data) {

                    showErrorMessage();

                }

                super.deliverResult(data);

            }
        };

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Contact>> loader, ArrayList<Contact> data) {

        // Anonymous class used to sort Contact data alphabetically
        Collections.sort(data, new Comparator<Contact>() {

            @Override
            public int compare(Contact c1, Contact c2) {

                String[] splitStr1 = c1.name.split("\\s+");
                String[] splitStr2 = c2.name.split("\\s+");


                return splitStr1[1].compareTo(splitStr2[1]);

            }

        });


        ArrayList<Contact> contactArrayList =  new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {

            if(data.get(i).favorite == 1d) {

                contactArrayList.add(data.get(i));

            }

        }


        mFavoriteContactList = contactArrayList;
        mProgressBar.setVisibility(View.INVISIBLE);
        mFullContactList = data;
        mRecyclerViewAdapter.updateContactData(data);


        showRecyclerView();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Contact>> loader) { }


    private void invalidateData() {
        mRecyclerViewAdapter.updateContactData(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_favorites) {

            if(mFavoriteToggle) {

                mRecyclerViewAdapter.updateContactData(mFavoriteContactList);
                item.setTitle("All Contacts");
                mFavoriteToggle = false;

            } else {

                mRecyclerViewAdapter.updateContactData(mFullContactList);
                item.setTitle("Favorites");
                mFavoriteToggle = true;

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(Contact selectedContact) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);


        intentToStartDetailActivity.putExtra("CONTACT_DETAILS", selectedContact);


        startActivity(intentToStartDetailActivity);

    }
}

