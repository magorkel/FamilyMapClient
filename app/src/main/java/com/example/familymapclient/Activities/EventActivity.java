package com.example.familymapclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.Fragments.MapFragment;
import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;

import shared.Model1.Event;

public class EventActivity extends AppCompatActivity
{
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager manager = getSupportFragmentManager();

        MapFragment mapFragment = new MapFragment();
        manager.beginTransaction().add(R.id.map, mapFragment).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            //finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.keyGoToMap, true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
