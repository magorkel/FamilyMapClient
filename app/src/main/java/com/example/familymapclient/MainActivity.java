package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;

public class MainActivity extends AppCompatActivity
{
    public final static String key = "familymapclient.MainActivity.Key";
    boolean goToMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToMap = getIntent().getBooleanExtra(key, false);

        FragmentManager manager = getSupportFragmentManager();

        if (!goToMap)
        {
            LoginFragment loginFragment = new LoginFragment();
            manager.beginTransaction().add(R.id.login_fragment, loginFragment).commit();
        }
        else
        {
            MapFragment mapFragment = new MapFragment();
            manager.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu)
    {
        //inflate menu resource - xml - map_toolbar_menu
        if(goToMap)
        {
            getMenuInflater().inflate(R.menu.map_toolbar_menu, menu);
            MenuItem search_icon = menu.findItem(R.id.search_icon);
            search_icon.setEnabled(true);
            MenuItem settings_icon = menu.findItem(R.id.settings_icon);
            settings_icon.setEnabled(true);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        return super.onOptionsItemSelected(item);
        //when they click on a button in toolbar this one gets activated
    }


}
