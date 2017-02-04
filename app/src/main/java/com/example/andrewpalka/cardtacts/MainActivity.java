package com.example.andrewpalka.cardtacts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.andrewpalka.cardtacts.Model.Data;
import com.example.andrewpalka.cardtacts.Utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Boolean favoriteToggle = false;
    private ProgressBar progressBar;
    private TextView mErrorMessageDisplay;

    private RecyclerView mRecyclerView;
    private Recycler_View_Adapter mRecyclerViewAdapter;

    private String TAG = MainActivity.class.getSimpleName();

    private List <Contact> fullContactList;
    private ArrayList<Contact> favoriteContactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //grabs reference to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fullContactList = new ArrayList<>();
        favoriteContactList = new ArrayList<>();


        //Populate dataset needed for recycler_view
        List<Data> data = fill_with_data();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Set adapter to custom RecyclerView.Adapter with loaded dataset
        mRecyclerViewAdapter= new Recycler_View_Adapter(data, getApplication(), fullContactList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        loadContactData();


    }

    private void loadContactData() {


        //Handles onCreate being called upon Orientation change
        if (fullContactList.isEmpty() == false) {
            fullContactList.clear();
        }

        showRecyclerView();
        new RetrieveFeedTask().execute();
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

    //region CustomAsyncTask
    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;
        private String email;

        @Override protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            if (!favoriteToggle) {
                setTitle(R.string.app_name);
            } else {
                setTitle("Favorites");
            }

        }

        @Override protected Void doInBackground(Void... arg0) {

            // Simple validation of URL

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
                        JSONObject r = results.getJSONObject(i);

                        Contact contact = new Contact(r);

                        fullContactList.add(contact);
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

            }
//            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if(fullContactList.isEmpty()) {
                showErrorMessage();

            } else {

                  showRecyclerView();
                Log.d(TAG, "onPostExecute: " + fullContactList.size());

                // TODO: THIS IS WHERE THE FUNCTION THAT UPDATES THE CONTACT LIST IS CALLED
                  mRecyclerViewAdapter.updateContactData(favoriteContactList);
                Log.d(TAG, "onPostExecute: " + fullContactList.size());
//                showMovieDataView();
//                mRecyclerViewAdapter.setMovieData(itemList);
            }

        }


    }
    //endregion

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
//TODO: REPLACE WITH DATA
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("Batman vs Superman",
                "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ",
                R.drawable.ic_action_movie,
                false));
        data.add(new Data("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero" +
                " film based on the X-Men characters that appear in Marvel Comics ",
                R.drawable.ic_action_movie,
                false));
        data.add(new Data("Captain America: Civil War", "A feud between Captain America and Iron Man" +
                " leaves the Avengers in turmoil.  ",
                R.drawable.ic_action_movie,
                true));
        data.add(new Data("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must " +
                "train a village of pandas",
                R.drawable.ic_action_movie,
                false));
        data.add(new Data("Warcraft", "Fleeing their dying home to colonize another, fearsome orc " +
                "warriors invade the peaceful realm of Azeroth. ",
                R.drawable.ic_action_movie,
                true));
        data.add(new Data("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ",
                R.drawable.ic_action_movie
                ,true));

        return data;
    }
}
