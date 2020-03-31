package com.example.familymapclient;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;

public class MainActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInfo userInfo = UserInfo.getUserInfo();

        FragmentManager manager = getSupportFragmentManager();

        if (userInfo.getAuthToken() == null)
        {
            LoginFragment loginFragment = new LoginFragment();
            manager.beginTransaction().add(R.id.login_fragment, loginFragment).commit();
        }
        else
        {
            MapFragment mapFragment = new MapFragment();
            manager.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        //mapFragment.getMapAsync(this);
    }



}
