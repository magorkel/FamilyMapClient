package com.example.familymapclient.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.Client;

import shared.Request1.LoginRequest;
import shared.Response1.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse>
{
    private final String serverHost;
    private final int serverPort;
    private final Listener listener;

    public interface Listener
    {
        void onLoginComplete(LoginResponse loginResponse);
    }

    public LoginTask (String host, int port, Listener listener)
    {
        serverHost = host;
        serverPort = port;
        this.listener = listener;//where to send its answer
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
        return client.login(loginRequest);//goes down to next: onPostExecute
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse)//gets return response from function above
    {
        super.onPostExecute(loginResponse);
        listener.onLoginComplete(loginResponse);
    }
}
