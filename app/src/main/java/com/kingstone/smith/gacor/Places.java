package com.kingstone.smith.gacor;

public class Places {
    private String name;
    private String detail;
    private Double lattitude;
    private Double longitude;

    public Places(String name, String detail, Double lattitude, Double longitude) {
        this.name = name;
        this.detail = detail;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
