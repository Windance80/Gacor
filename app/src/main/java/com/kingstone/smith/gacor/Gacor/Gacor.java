package com.kingstone.smith.gacor.Gacor;

public class Gacor {

    private String place;
    private String distance;
    private String time;

    public Gacor(String place, String distance, String time) {
        super();
        this.place = place;
        this.distance = distance;
        this.time = time;
    }

    public String getPlace() {
        return place;
    }
    public String getDistance() {
        return distance;
    }
    public String getTime() { return time; }
}
