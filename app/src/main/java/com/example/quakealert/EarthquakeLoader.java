package com.example.quakealert;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthquakeData>> {

    private static String USGS_URL;

    public EarthquakeLoader(Context context, String USGS) {
        super(context);
        USGS_URL = USGS;
    }

    public ArrayList<EarthquakeData> loadInBackground() {
        ArrayList<EarthquakeData> location = QueryUtils.extractEarthquakes(USGS_URL);
        return location;
    }
}
