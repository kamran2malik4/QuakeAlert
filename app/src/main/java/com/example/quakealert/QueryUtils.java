package com.example.quakealert;

import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link EarthquakeData} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthquakeData> extractEarthquakes(String USGS_URL)  {

        Log.v("MyLoader", "extractEarthquakes");

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthquakeData> earthquakes = new ArrayList<>();

        URL response = createURL(USGS_URL);

        String jsonResponse = makeHttpRequest(response);

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root = new JSONObject(jsonResponse);

            JSONArray featuresArray = root.getJSONArray("features");

            for (int i=0;   i< featuresArray.length(); i++){
                JSONObject feature = featuresArray.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                long dateAndTime = properties.getLong("time");
                String url = properties.getString("url");

                earthquakes.add(new EarthquakeData(magnitude, place, dateAndTime, url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static URL createURL(String usgsUrl){
        URL url = null;
        try{
            url = new URL(usgsUrl);
        }
        catch (Exception e){
            //TO DO
        }
        return url;
    }

    private static String makeHttpRequest(URL response){
        String jsonResponse = "";
        InputStream stream = null;
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) response.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();
            if(connection.getResponseCode() == 200){
                stream = connection.getInputStream();
                jsonResponse = getJSONobject(stream);
            }
            else{
                //To Do
            }
        }
        catch(Exception e){
            //TO DO
        }
        return jsonResponse;
    }

    private static String getJSONobject(InputStream stream){
        StringBuilder jsonResponse = new StringBuilder();
        try{
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            String read = reader.readLine();
            while(read != null){
                jsonResponse.append(read);
                read = reader.readLine();
            }
        }
        catch(Exception e){
            //TO DO
        }
        return jsonResponse.toString();
    }

}