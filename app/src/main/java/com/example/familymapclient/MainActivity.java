package com.example.familymapclient;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;

public class MainActivity extends FragmentActivity
{

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInfo userInfo = UserInfo.getUserInfo();

        FragmentManager manager = getSupportFragmentManager();
        // somehow need to see if the user is logged in
        // if not go to login fragment
        // else go to map fragment
        if (userInfo.getAuthToken() == null)
        {
            loginFragment = new LoginFragment();
            manager.beginTransaction().add(R.id.login_fragment,loginFragment).commit();

            //loginFragment = LoginFragment.newInstance();
            //fragmentManager.beginTransaction().replace(R.id.fragment_holder, loginFragment).commit();
            //fragmentManager.beginTransaction().add(R.id.fragment_holder, loginFragment).commit();
        }
        else
        {
            mapFragment = new MapFragment();
            manager.beginTransaction().add(R.id.map,mapFragment).commit();
            //mapFragment =   MapFragment.newInstance();
            //fragmentManager.beginTransaction().add(R.id.fragment_holder, mapFragment).commit();
        }
        //mapFragment.getMapAsync(this);
    }



}
