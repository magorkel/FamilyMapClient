package com.example.familymapclient.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.Client;

import shared.Request1.RegisterRequest;
import shared.Response1.RegisterResponse;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse>
{
    private final String serverHost;
    private final int serverPort;
    private final Listener listener;

    public interface Listener
    {
        void onRegisterComplete(RegisterResponse registerResponse);
    }

    public RegisterTask (String host, int port, Listener listener)
    {
        serverHost = host;
        serverPort = port;
        this.listener = listener;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests)
    {
        int numberOfRequests = 1;
        if (registerRequests.length != numberOfRequests)
        {
            Log.e("RegisterTask", "doInBackground received too many requests");
            return null;
        }
        RegisterRequest registerRequest = registerRequests[0];
        Client client = new Client(serverHost, serverPort);
        return client.register(registerRequest);
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse)//gets return response from function above
    {
        super.onPostExecute(registerResponse);
        listener.onRegisterComplete(registerResponse);
    }
}
