package com.example.familymapclient.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.Client;
import com.example.familymapclient.UserInfo;

import java.util.TreeMap;

import shared.Model1.Event;
import shared.Model1.Person;
import shared.Response1.EventResponse;
import shared.Response1.PersonResponse;
import shared.Response1.SinglePersonResponse;

public class UserInfoTask extends AsyncTask<String, Void, Void>
{
    private final String serverHost;
    private final int serverPort;
    private final UserInfoTask.Listener listener;

    private UserInfo userInfo = UserInfo.getUserInfo();

    public interface Listener
    {
        void onUserInfoComplete();
        void onUserInfoFailed();
    }

    public UserInfoTask (String host, int port, UserInfoTask.Listener listener)
    {
        serverHost = host;
        serverPort = port;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... strings)
    {
        int numberOfRequests = 2;
        if (strings.length != numberOfRequests)
        {
            Log.e("UserInfoTask", "doInBackground received too many requests");
            return null;
        }
        String personID = strings[0];
        userInfo.setPersonID(personID);
        String authToken = strings[1];
        userInfo.setAuthToken(authToken);

        Client client = new Client (serverHost, serverPort);

        PersonResponse personResponse = client.getPersons(authToken);
        if (personResponse == null)
        {
            listener.onUserInfoFailed();
            return null;
        }
        TreeMap<String, Person> persons = new TreeMap<>();
        for (Person person : personResponse.getData())
        {
            persons.put(person.getPersonID(), person);
        }
        userInfo.setPersons(persons);

        EventResponse eventResponse = client.getEvents(authToken);
        TreeMap<String, Event> events = new TreeMap<>();
        for (Event event : eventResponse.getData())
        {
            events.put(event.getEventID(), event);
        }
        userInfo.setEvents(events);

        listener.onUserInfoComplete();
        return null;
    }

    public UserInfo getUserInfo() { return userInfo; }
}
