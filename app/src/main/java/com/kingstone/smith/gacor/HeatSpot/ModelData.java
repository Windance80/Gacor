package com.kingstone.smith.gacor.HeatSpot;

import java.text.SimpleDateFormat;
import java.util.Date;

// POJO
public class ModelData {

    private int dayOfWeek;
    private long date;
    private String place;
    private String day;
    private String time;
    private long id;

    public ModelData(int dayOfWeek, long date, String place, String day, long id) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.place = place;
        this.day = day;
        this.id = id;
        time = new SimpleDateFormat("HH:mm").format(date);
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public long getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public long getId() { return id;}
}
