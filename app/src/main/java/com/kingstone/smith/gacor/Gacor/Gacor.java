package com.kingstone.smith.gacor.Gacor;

public class Gacor {

    private String place;
    private String distance;
    private String time;
    private int type;

    public Gacor(String place, String distance, String time, int type) {
        super();
        this.place = place;
        this.distance = distance;
        this.time = time;
        this.type = type;

    }

    public int getType() { return type; }
    public String getPlace() {
        return place;
    }
    public String getDistance() {
        return distance;
    }
    public String getTime() { return time; }
}
