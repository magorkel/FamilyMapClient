package com.example.familymapclient;

import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import shared.Request1.LoginRequest;

public class Client
{
    private static final String HTTP = "http";
    private static final int TIME_OUT = 5000;
    private static final String EVENT = "/event";
    private static final String PERSON = "/person";
    private static final String SLASH = "/";
    private static final String LOGIN = "/user/login";
    private static final String REGISTER = "/user/register";

    private final String serverHost;
    private final int serverPort;

    //URLs to create
    private URL baseURL;
    private URL loginURL;
    private URL registerURL;
    private URL peopleURL;
    private URL eventsURL;

    public Client (String host, int port)
    {
        serverHost = host;
        serverPort = port;
    }

    public String login(LoginRequest loginRequest)
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

        return getURL(loginURL, loginRequest.serialize());
    }

    public String getPerson(String rootPersonID, String request)
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
        return getURL(personURL, request);
    }

    public String getEvent(String eventID, String request)
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
        return getURL(eventURL, request);
    }

    private String getURL(URL url, String request)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIME_OUT);
            connection.setRequestMethod("post");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(request.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = connection.getInputStream();
                Scanner scanner = new Scanner(responseBody).useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
            else
            {
                Log.e("Client", "HttpURLConnection response was not HTTP_OK");
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
