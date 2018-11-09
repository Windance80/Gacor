package com.kingstone.smith.gacor.HeatSpot;

import java.util.Date;

public class ItemHeader extends ItemType {

    private int dayOfWeek;

    public ItemHeader(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
