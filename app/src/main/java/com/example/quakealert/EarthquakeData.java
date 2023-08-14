package com.example.quakealert;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EarthquakeData {
    private double m_magnitude;
    private String m_place;
    private Date m_dateAndTime;
    private String m_url;

    public EarthquakeData(double magnitude, String place, long dateAndTime, String url){
        m_magnitude = magnitude;
        m_place = place;
        m_dateAndTime = new Date(dateAndTime);
        m_url = url;
    }

    public double getMagnitude(){ return m_magnitude; }
    public String getOffsetLocation(){
        String offSetLocation;

        if(!m_place.contains("of")){
            offSetLocation =  "Near the";
        }
        else{
            int length = m_place.indexOf("of");
            offSetLocation = m_place.substring(0, length + 3);
        }

        return offSetLocation;
    }
    public String getPrimaryLocation(){
        String primaryLocation;
        if(!m_place.contains("of")){
            primaryLocation =  m_place;
        }
        else{
            int length = m_place.indexOf("of");
            primaryLocation = m_place.substring(length + 3);
        }
        return primaryLocation;
    }
    public String getDate(){
        String date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
        date = dateFormat.format(m_dateAndTime);
        return date;
    }
    public String getTime(){
        String time;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM a");
        time = timeFormat.format(m_dateAndTime);
        return time;
    }
    public String getUrl(){ return m_url; }
}
