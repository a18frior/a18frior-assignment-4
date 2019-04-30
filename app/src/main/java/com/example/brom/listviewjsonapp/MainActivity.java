package com.example.brom.listviewjsonapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.Arrays;
import android.content.Intent;
import android.support.v7.widget.Toolbar;


import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




// Create a new class, Mountain, that can hold your JSON data

// Create a ListView as in "Assignment 1 - Toast and ListView"

// Retrieve data from Internet service using AsyncTask and the included networking code

// Parse the retrieved JSON and update the ListView adapter

// Implement a "refresh" functionality using Android's menu system


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    ArrayList<Mountain> berg2 =new ArrayList<>();




    private class FetchData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            String s1= o
                        .replace("\"{\\\"img", "{\\\"img")
                        .replace("\\\"}\"", "\\\"}")
                        .replace("\\\"", "\"");

            Log.d("Jennas log",s1);

            try {

                Log.d("jennas log", "okej1");
                JSONArray mountains = new JSONArray(s1);
                Log.d("jennas log", "okej2");

                for (int i = 0; i < mountains.length(); i++) {
                    JSONObject json1 = mountains.getJSONObject(i);
                    JSONObject auxdata = json1.getJSONObject("auxdata");
                    String img = auxdata.getString("img");
                    Log.d("img", img);


                    String location = json1.getString("location");
                    String name = json1.getString("name");
                    Log.d("jennas log", "" + name);
                    int height = json1.getInt("size");
                    Log.d("jennas log", "" + height);
                    Mountain m1 = new Mountain(name, location, height,img);
                    berg2.add(m1);
                    Log.d("jennas log", "okej3");

                }

                } catch(Exception e){
                    Log.d("jennas log", "E:" + e.getMessage());
                }

            Log.d("jennas log","berg is ok");
            ArrayList<String> berg = new ArrayList();
            for (int i=0; i<berg2.size();i++){
                berg.add(berg2.get(i).getName());
            }

            String temp="";
            for (int i=0; i<berg.size();i++){
                temp+= berg.get(i) + " ";
            }
            Log.d("jennas log", "berg has: " + temp);




                /*berg2.add(m);
                berg2.add(m2);
                berg2.add(m3);*/

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listview, R.id.fridasItem, berg);
                ListView lista= findViewById(R.id.fridaslist);
                lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent appInfo = new Intent(MainActivity.this, MountainDetailsActivity.class);

                    appInfo.putExtra("berget", berg2.get(position).info());
                    appInfo.putExtra("berget2",berg2.get(position).bild() );




                    startActivity(appInfo);
                }
            });

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            berg2.clear();
            new FetchData().execute();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
