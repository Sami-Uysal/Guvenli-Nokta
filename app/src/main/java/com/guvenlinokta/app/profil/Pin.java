package com.guvenlinokta.app.profil;

public class Pin {
    private String id;
    private String ad;
    private double lat;
    private double lng;

    public Pin() {
    }

    public Pin(String id, String ad, double lat, double lng) {
        this.id = id;
        this.ad = ad;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
