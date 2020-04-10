package com.example.familymapclient;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import shared.Request1.LoginRequest;
import shared.Request1.RegisterRequest;
import shared.Response1.EventResponse;
import shared.Response1.LoginResponse;
import shared.Response1.PersonResponse;
import shared.Response1.RegisterResponse;
import shared.Response1.SinglePersonResponse;

public class Client
{
    private static final String HTTP = "http";
    private static final int TIME_OUT = 5000;
    //private static final String EVENT = "/event";
    //private static final String PERSON = "/person";
    //private static final String SLASH = "/";
    private static final String LOGIN = "/user/login";
    private static final String REGISTER = "/user/register";

    private final String serverHost;
    private final int serverPort;

    public Client (String host, int port)
    {
        serverHost = host;
        serverPort = port;
    }

    public LoginResponse login(LoginRequest loginRequest)
    {
        URL loginURL;
        try
        {
            loginURL = new URL(Client.HTTP, serverHost, serverPort, LOGIN);
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, login");
            return null;
        }
        Gson gson = new Gson();
        String stringifiedRequest = gson.toJson(loginRequest);
        String stringifiedResponse = postURL(loginURL, stringifiedRequest);
        return gson.fromJson(stringifiedResponse, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest registerRequest)
    {
        URL registerURL;
        try
        {
            registerURL = new URL(Client.HTTP, serverHost, serverPort, REGISTER);
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, register");
            return null;
        }
        Gson gson = new Gson();
        String stringifiedRequest = gson.toJson(registerRequest);
        String stringifiedResponse = postURL(registerURL, stringifiedRequest);
        return gson.fromJson(stringifiedResponse, RegisterResponse.class);
    }

    /*public String getPerson(String rootPersonID, String request)
    {
        URL personURL;
        try
        {
            personURL = new URL(Client.HTTP, serverHost, serverPort, PERSON + SLASH + rootPersonID);
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, getPerson");
            return null;
        }
        return postURL(personURL, request);
    }*/

    /*public String getEvent(String eventID, String request)
    {
        URL eventURL;
        try
        {
            eventURL = new URL(Client.HTTP, serverHost, serverPort, EVENT + SLASH + eventID);
        } catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, getEvent");
            return null;
        }
        return postURL(eventURL, request);
    }*/

    private String postURL(URL url, String request)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIME_OUT);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(request.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return readString(connection.getInputStream());
            }
            else
            {
                Log.e("Client", "HttpURLConnection response was not HTTP_OK");
                return readString(connection.getErrorStream());
            }
        }
        catch (IOException e)
        {
            System.out.println("couldn't open url connection");
            e.printStackTrace();
        }
        return null;
    }

    private String readString(InputStream paramInputStream) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(paramInputStream);
        char[] arrayOfChar = new char[1024];
        int i;
        while ((i = inputStreamReader.read(arrayOfChar)) > 0)
            stringBuilder.append(arrayOfChar, 0, i);
        return stringBuilder.toString();
    }

    public SinglePersonResponse getPerson(String personID, String authToken)
    {
        URL personURL;
        try
        {
            personURL = new URL(Client.HTTP, serverHost, serverPort, "/person/" + personID);
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, getPerson");
            return null;
        }
        Gson gson = new Gson();
        String stringifiedResponse = getURL(personURL, authToken);
        return gson.fromJson(stringifiedResponse, SinglePersonResponse.class);
    }

    public PersonResponse getPersons (String authToken)
    {
        URL personsURL;
        try
        {
            personsURL = new URL(Client.HTTP, serverHost, serverPort, "/person");
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, getPersons");
            return null;
        }
        Gson gson = new Gson();
        String stringifiedResponse = getURL(personsURL, authToken);
        return gson.fromJson(stringifiedResponse, PersonResponse.class);
    }

    public EventResponse getEvents (String authToken)
    {
        URL eventsURL;
        try
        {
            eventsURL = new URL(Client.HTTP, serverHost, serverPort, "/event");
        }
        catch (MalformedURLException e)
        {
            Log.e("Client", "malformed URL, getEvents");
            return null;
        }
        Gson gson = new Gson();
        String stringifiedResponse = getURL(eventsURL, authToken);
        return gson.fromJson(stringifiedResponse, EventResponse.class);
    }

    private String getURL(URL url, String authToken)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIME_OUT);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authToken);
            connection.setDoOutput(false);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return readString(connection.getInputStream());
            }
            else
            {
                Log.e("Client", "HttpURLConnection response was not HTTP_OK");
                return readString(connection.getErrorStream());
            }
        }
        catch (IOException e)
        {
            System.out.println("couldn't open url connection");
            e.printStackTrace();
        }
        return null;
    }
}
