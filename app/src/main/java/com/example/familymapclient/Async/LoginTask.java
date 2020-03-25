package com.example.familymapclient.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import shared.Request1.LoginRequest;
import shared.Response1.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse>
{
    private final String serverHost;
    private final int serverPort;

    public LoginTask (String host, int port)
    {
        serverHost = host;
        serverPort = port;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... loginRequests)
    {
        int numberOfRequests = 1;
        if (loginRequests.length != numberOfRequests)
        {
            Log.e("LoginTask", "doInBackground received too many requests");
            return null;
        }
        LoginRequest loginRequest = loginRequests[0];
        Client client = new Client(serverHost, serverPort);
        //LoginResponse response = client.login(loginRequest);
        return null;
    }
}
