package com.kingstone.smith.gacor.HeatSpot;

import java.util.Date;

// POJO
public class ModelData {

    private int dayOfWeek;
    private long date;
    private String place;
    private String day;

    public ModelData(int dayOfWeek, long date, String place, String day) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.place = place;
        this.day = day;
    }

    public String getDay() {
        return day;
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
}
