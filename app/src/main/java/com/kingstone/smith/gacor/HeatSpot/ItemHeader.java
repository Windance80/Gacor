package com.kingstone.smith.gacor.HeatSpot;

import java.util.Date;

public class ItemHeader extends ItemType {

    private int dayOfWeek;
    private String day;

    public ItemHeader(int dayOfWeek, String day) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDay() {
        return day;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
