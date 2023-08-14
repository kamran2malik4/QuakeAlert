package com.example.quakealert;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthquakeData>> {

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private ListView m_locationList;
    private ProgressBar m_spinningBar;
    private TextView m_noQuakeFound;
    private TextView m_internetInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_locationList = findViewById(R.id.locations_list);
        m_spinningBar = findViewById(R.id.spinner_progress);
        m_noQuakeFound = findViewById(R.id.earthquakes_not_found);
        m_internetInfo = findViewById(R.id.internet_info);


        m_locationList.setEmptyView(m_noQuakeFound);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = connectivityManager.getActiveNetworkInfo();

        boolean internetStatus = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        if(internetStatus){
            getSupportLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
        }
        else{
            m_internetInfo.setText("No Internet Connection");
            m_spinningBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateData(ArrayList<EarthquakeData> location){


        LocationListAdapter adapter = new LocationListAdapter(this, location);

        m_locationList.setAdapter(adapter);


        m_locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = location.get(i).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private String getSortOrder(SharedPreferences preferences){
        String orderList = preferences.getString(getString(R.string.settings_order_by_key), "-1");
        int index = (Integer) Integer.parseInt(orderList);
        String order[] = getResources().getStringArray(R.array.order_by_entries);
        if(index>=0){
            return order[index];
        }
        else{
            return order[0];
        }
    }

    @Override
    public Loader<ArrayList<EarthquakeData>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = preferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        String listLimit = preferences.getString(getString(R.string.settings_recent_earthquakes_key), getString(R.string.settings_recent_earthquakes_default));

        String sortOrder = getSortOrder(preferences);

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("format", "geojson");
        builder.appendQueryParameter("limit", listLimit);
        builder.appendQueryParameter("minmag", minMagnitude);
        builder.appendQueryParameter("orderby", sortOrder);

        return new EarthquakeLoader(MainActivity.this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthquakeData>> loader, ArrayList<EarthquakeData> data) {
        m_spinningBar.setVisibility(View.INVISIBLE);
        if(!data.isEmpty() && data != null){
            updateData(data);
        }
        else{
            m_noQuakeFound.setText("Earthquakes not found.");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthquakeData>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}