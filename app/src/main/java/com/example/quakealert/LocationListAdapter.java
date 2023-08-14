package com.example.quakealert;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.animation.DrawableAlphaProperty;

import java.util.ArrayList;

public class LocationListAdapter extends ArrayAdapter<EarthquakeData> {

    public LocationListAdapter(Activity context, ArrayList<EarthquakeData>list){
        super(context, 0, list);
    }

    private int getMagnitudeColor(double magnitude){
        int mag = (int)(magnitude);

        switch (mag){
            case 0:
            case 1: return ContextCompat.getColor(getContext(), R.color.magnitude1);
            case 2: return ContextCompat.getColor(getContext(), R.color.magnitude2);
            case 3: return ContextCompat.getColor(getContext(), R.color.magnitude3);
            case 4: return ContextCompat.getColor(getContext(), R.color.magnitude4);
            case 5: return ContextCompat.getColor(getContext(), R.color.magnitude5);
            case 6: return ContextCompat.getColor(getContext(), R.color.magnitude6);
            case 7: return ContextCompat.getColor(getContext(), R.color.magnitude7);
            case 8: return ContextCompat.getColor(getContext(), R.color.magnitude8);
            case 9: return ContextCompat.getColor(getContext(), R.color.magnitude9);
            default:
                return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View earthquakeList = convertView;
        if(earthquakeList == null){
            earthquakeList = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthqauke_list, parent, false
            );
        }

        EarthquakeData currentItem = getItem(position);

        TextView magnitude = earthquakeList.findViewById(R.id.magnitude);

        GradientDrawable magnitudeBackground = (GradientDrawable) magnitude.getBackground();

        int color = getMagnitudeColor(currentItem.getMagnitude());

        magnitudeBackground.setColor(color);

        magnitude.setText("" + currentItem.getMagnitude());

        TextView offsetLocation = earthquakeList.findViewById(R.id.offset_location);
        offsetLocation.setText(currentItem.getOffsetLocation());

        TextView primaryLocation = earthquakeList.findViewById(R.id.primary_location);
        primaryLocation.setText(currentItem.getPrimaryLocation());

        TextView date = earthquakeList.findViewById(R.id.date);
        date.setText(currentItem.getDate());

        TextView time = earthquakeList.findViewById(R.id.time);
        time.setText(currentItem.getTime());

        return earthquakeList;
    }
}
