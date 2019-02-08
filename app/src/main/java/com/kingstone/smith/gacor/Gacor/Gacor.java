package com.kingstone.smith.gacor.Gacor;

public class Gacor {

    private String place;
    private String distance;
    private String time;
    private double lat;
    private double lng;

    public Gacor(String place, String distance, String time, double lat, double lng) {
        super();
        this.place = place;
        this.distance = distance;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
    }

    public String getPlace() {
        return place;
    }
    public String getDistance() {
        return distance;
    }
    public String getTime() { return time; }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
