package com.example.a100535658.project;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 100523158 on 12/2/2017.
 */

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String teamName;
    private String uid;
    private String fullName;

    public User (){ }
    public User(String uid,String firstName,
                String lastName,String fullName,
                String email,
                String password, String teamName)
    {
        this.setUid(uid);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setPassword(password);
        this.setTeamName(teamName);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}