package com.example.a100535658.project;
public class Detail{
    private String id;
    private double latitude;
    private double longitude;
    private String sport;
    private String location;
    private String teamValue;


    public Detail(String id,double latitude, double longitude, String sport, String location, String teamValue) {
        this.setId(id);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setSport(sport);
        this.setLocation(location);
        this.setTeamValue(teamValue);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeamValue() {
        return teamValue;
    }

    public void setTeamValue(String teamValue) {
        this.teamValue = teamValue;
    }
}