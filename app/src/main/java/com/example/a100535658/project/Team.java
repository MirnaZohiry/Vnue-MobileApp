 package com.example.a100535658.project;

/**
 * Created by 100523158 on 12/2/2017.
 */

public class Team {
    private String teamName;
    private String sport;
    private double latitude;
    private double longitude;
    private String id;
    private CharSequence location;
    private float win;
    private float lost;
    private float draw;


    public Team(String id, String sport,
                String teamName,
                double latitude,
                double longitude, CharSequence location,float win, float lost, float draw) {
        this.setId(id);
        this.setSport(sport);
        this.setTeamName(teamName);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setLocation(location);
        this.setWin(win);
        this.setLost(lost);
        this.setDraw(draw);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CharSequence getLocation() {
        return location;
    }

    public void setLocation(CharSequence location) {
        this.location = location;
    }

    public float getWin() {
        return win;
    }

    public void setWin(float win) {
        this.win = win;
    }

    public float getLost() {
        return lost;
    }

    public void setLost(float lost) {
        this.lost = lost;
    }

    public float getDraw() {
        return draw;
    }

    public void setDraw(float draw) {
        this.draw = draw;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                ", sport='" + sport + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                ", location=" + location +
                ", win=" + win +
                ", lost=" + lost +
                ", draw=" + draw +
                '}';
    }
}