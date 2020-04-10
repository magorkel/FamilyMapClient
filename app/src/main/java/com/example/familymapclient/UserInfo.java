package com.example.familymapclient;

import shared.Model1.Person;

import java.util.TreeMap;

import shared.Model1.Event;

public class UserInfo
{
    private static UserInfo userInfo;
    private String authToken;
    private String personID;
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

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public TreeMap<String, Person> getPersons() { return persons; }

    public void setPersons(TreeMap<String, Person> persons) { this.persons = persons; }

    public TreeMap<String, Event> getEvents() { return events; }

    public void setEvents(TreeMap<String, Event> events) { this.events = events; }
}
