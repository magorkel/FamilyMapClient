package com.example.familymapclient;

import android.app.Person;

import java.util.TreeMap;

import shared.Model1.Event;

public class UserInfo
{
    private static UserInfo userInfo;
    private String authToken;
    private String userID;
    //private LoginResponse loginResponse;
    //private RegisterResponse registerResponse;
    private TreeMap<String, Person> persons;
    private TreeMap<String, Event> events;

    private UserInfo() {}

    public static UserInfo getUserInfo()
    {
        if (userInfo == null) { userInfo = new UserInfo(); }
        return userInfo;
    }

    public Person getPerson (String personID)
    {
        try { return persons.get(personID); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public String getAuthToken() { return authToken; }

    public void setAuthToken (String authToken) { this.authToken = authToken; }
}
