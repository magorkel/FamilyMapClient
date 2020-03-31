package com.example.familymapclient.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.Client;

import shared.Request1.LoginRequest;
import shared.Response1.LoginResponse;
import shared.Response1.SinglePersonResponse;

public class GetPersonTask extends AsyncTask<String, Void, SinglePersonResponse>
{
    private final String serverHost;
    private final int serverPort;
    private final GetPersonTask.Listener listener;

    public interface Listener
    {
        void onGetPersonComplete(SinglePersonResponse singlePersonResponse);
    }

    public GetPersonTask (String host, int port, GetPersonTask.Listener listener)
    {
        serverHost = host;
        serverPort = port;
        this.listener = listener;//where to send its answer
    }

    @Override
    protected SinglePersonResponse doInBackground(String... strings)
    {
        int numberOfRequests = 2;
        if (strings.length != numberOfRequests)
        {
            Log.e("GetPersonTask", "doInBackground received too many requests");
            return null;
        }
        String personID = strings[0];
        String authToken = strings[1];
        Client client = new Client(serverHost, serverPort);
        return client.getPerson(personID, authToken);//goes down to next: onPostExecute
    }

    @Override
    protected void onPostExecute(SinglePersonResponse singlePersonResponse)//gets return response from function above
    {
        super.onPostExecute(singlePersonResponse);
        listener.onGetPersonComplete(singlePersonResponse);
    }
}
