package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
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
implements Recycler_View_Adapter.Adapter_OnClickHandler,
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Contact>> {

    private Boolean favoriteToggle = false;
    private ProgressBar progressBar;
    private TextView mErrorMessageDisplay;

    private RecyclerView mRecyclerView;
    private Recycler_View_Adapter mRecyclerViewAdapter;

    private String TAG = MainActivity.class.getSimpleName();

    private List<Contact> fullContactList;
    private ArrayList<Contact> favoriteContactList;

    private static final int CONTACT_LOADER_ID = 0;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("size", fullContactList.size());

        for (int i = 0; i < fullContactList.size(); i++) {
            String key = "contact_" + i;
            outState.putParcelable(key, fullContactList.get(i));
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

        fullContactList = contactArrayList;




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //grabs reference to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fullContactList = new ArrayList<>();
        favoriteContactList = new ArrayList<>();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Set adapter to custom RecyclerView.Adapter with loaded dataset
        mRecyclerViewAdapter = new Recycler_View_Adapter(getApplication(),this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
         * This setting improveS performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */


               /*
         * This ID will uniquely identify the Loader. We can use it, for example, to get a handle
         * on our Loader at a later point in time through the support LoaderManager.
         */
        int loaderId = CONTACT_LOADER_ID;

    /*
     * From MainActivity, we have implemented the LoaderCallbacks interface with the type of
     * String array. (implements LoaderCallbacks<String[]>) The variable callback is passed
     * to the call to initLoader below. This means that whenever the loaderManager has
     * something to notify us of, it will do so through this callback.
     */
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Contact>> callback = MainActivity.this;

    /*
     * The second parameter of the initLoader method below is a Bundle. Optionally, you can
     * pass a Bundle to initLoader that you can then access from within the onCreateLoader
     * callback. In our case, we don't actually use the Bundle, but it's here in case we wanted
     * to.
     */
        Bundle bundleForLoader = null;

    /*
     * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
     * created and (if the activity/fragment is currently started) starts the loader. Otherwise
     * the last created loader is re-used.
     */
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        mRecyclerView.setHasFixedSize(true);


        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        loadContactData();


    }

    private void loadContactData() {


        //Handles onCreate being called upon Orientation change
        if (!fullContactList.isEmpty()) {
            fullContactList.clear();
        }

        showRecyclerView();
//        new RetrieveFeedTask().execute();

    }

    private void showRecyclerView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    //region CustomAsyncTaskLoader

    @Override
    public Loader<ArrayList<Contact>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Contact>>(this) {


                ArrayList<Contact> mContactData = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */

            @Override
            protected void onStartLoading() {
                if (mContactData != null) {
                    deliverResult(mContactData);
                } else {
                    mContactData = new ArrayList<>();
                    forceLoad();
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onStartLoading();
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from OpenWeatherMap in the background.
             *
             * @return Weather data from OpenWeatherMap as an array of Strings.
             *         null if an error occurs
             */

            @Override
            public ArrayList<Contact> loadInBackground() {



                HttpHandler sh = new HttpHandler();

                String endpointUrl = "https://s3.amazonaws.com/technical-challenge/Contacts.json";

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(endpointUrl);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {

                        JSONArray results = new JSONArray(jsonStr);

                        // looping through All Contacts
                        for (int i = 0; i < results.length(); i++) {

                            Log.d(TAG, "loadInBackground: LENGTH = " + results.length());

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
                                    "Couldn't get json from server. Check LogCat for possible errors!",
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

        Collections.sort(data, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {

                String[] splitStr1 = c1.name.split("\\s+");
                String[] splitStr2 = c2.name.split("\\s+");


                return splitStr1[1].compareTo(splitStr2[1]);
            }
        });


        progressBar.setVisibility(View.INVISIBLE);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

