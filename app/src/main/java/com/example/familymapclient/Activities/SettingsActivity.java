package com.example.familymapclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;
import com.google.android.gms.maps.model.PolylineOptions;

import shared.Model1.User;

public class SettingsActivity extends AppCompatActivity
{
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map: Settings");
        setSupportActionBar(toolbar);

        final Switch lifeStorySwitch = findViewById(R.id.life_story_switch);
        lifeStorySwitch.setChecked(UserInfo.getUserInfo().isLifeStoryLineOn());
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setLifeStoryLineOn(true);
                else UserInfo.getUserInfo().setLifeStoryLineOn(false);
            }
        });

        Switch familyTreeLinesSwitch = findViewById(R.id.family_tree_lines_switch);
        familyTreeLinesSwitch.setChecked(UserInfo.getUserInfo().isFamilyTreeLineOn());
        familyTreeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setFamilyTreeLineOn(true);
                else UserInfo.getUserInfo().setFamilyTreeLineOn(false);
            }
        });

        Switch spouseLinesSwitch = findViewById(R.id.spouse_lines_switch);
        spouseLinesSwitch.setChecked(UserInfo.getUserInfo().isSpouseLinesOn());
        spouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setSpouseLinesOn(true);
                else UserInfo.getUserInfo().setSpouseLinesOn(false);
            }
        });

        Switch fathersSideSwitch = findViewById(R.id.fathers_side_switch);
        fathersSideSwitch.setChecked(UserInfo.getUserInfo().isFathersSideOn());
        fathersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setFathersSideOn(true);
                else UserInfo.getUserInfo().setFathersSideOn(false);
            }
        });

        Switch mothersSideSwitch = findViewById(R.id.mothers_side_switch);
        mothersSideSwitch.setChecked(UserInfo.getUserInfo().isMothersSideOn());
        mothersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setMothersSideOn(true);
                else UserInfo.getUserInfo().setMothersSideOn(false);
            }
        });

        Switch maleEventsSwitch = findViewById(R.id.male_events_switch);
        maleEventsSwitch.setChecked(UserInfo.getUserInfo().isMaleEventsOn());
        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setMaleEventsOn(true);
                else UserInfo.getUserInfo().setMaleEventsOn(false);
            }
        });

        Switch femaleEventsSwitch = findViewById(R.id.female_events_switch);
        femaleEventsSwitch.setChecked(UserInfo.getUserInfo().isFemaleEventsOn());
        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) UserInfo.getUserInfo().setFemaleEventsOn(true);
                else UserInfo.getUserInfo().setFemaleEventsOn(false);
            }
        });

        RelativeLayout logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MainActivity.keyGoToMap, false);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
