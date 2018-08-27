package com.kingstone.smith.gacor;

import android.support.annotation.NonNull;

public class Gacor {

    private String mPlace;
    private String mDistance;

    public Gacor(String place, String distance) {
        super();
        mPlace = place;
        mDistance = distance;
    }

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

}
