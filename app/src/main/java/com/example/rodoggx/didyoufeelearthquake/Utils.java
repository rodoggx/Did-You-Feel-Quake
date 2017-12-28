package com.example.rodoggx.didyoufeelearthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 */

public class Utils {
    
    private static final String TAG = Utils.class.getSimpleName();
    
    public static Event getEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "getEarthquakeData: ", e);
        }
        Event earthquake = getFeatureFromJson(jsonResponse);
        return earthquake;
    }

    private static Event getFeatureFromJson(String earthquakeJSON) {
        //if JSON string is empty
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            //Extract the JSONArray associated with the key called "features" which represents a list of features/earthquakes).
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

            //if there is an earthquake event in the JSONArray, create Earthquake object
            if (featureArray.length() > 0) {
                //get a single earthquake at position i within the list of earthquakes
                JSONObject firstFeature = featureArray.getJSONObject(0);

                //Extract the JSONArray associated with the key called "properties" which represents a list of properties).
                JSONObject properties = firstFeature.getJSONObject("properties");

                //extract the values for each key
                String title = properties.getString("title");
                String numberOfPeople = properties.getString("felt");
                String perceivedStrength = properties.getString("cdi");

                // Create a new {@link Event} object
                return new Event(title, numberOfPeople, perceivedStrength);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return null;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest URL response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: URL did not connect", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "makeHttpRequest inputStream did not close: ", e);
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream,  Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUrl: ", e);
        }
        return url;
    }
}
