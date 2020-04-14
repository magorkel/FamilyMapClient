package com.example.familymapclient;

import com.example.familymapclient.UserInfo;

import java.util.ArrayList;

import shared.Model1.Event;

public class Filter
{
    private String filter;
    private boolean filterOn;

    public Filter(String filter)
    {
        this.filter = filter;
        filterOn = true;
    }

    public ArrayList<String> eventTypes()
    {
        UserInfo userInfo = UserInfo.getUserInfo();
        ArrayList<String> eventTypes = new ArrayList<>();

        for (Event event : userInfo.getEvents().values())
        {
            if (!eventTypes.contains(event.getEventType()))
            {
                eventTypes.add(event.getEventType());
            }
        }
        return eventTypes;
    }

    public String getFilter() { return filter; }

    public void setFilter(String filter) { this.filter = filter; }

    public boolean isFilterOn() { return filterOn; }

    public void setFilterOn(boolean onOff)
    {
        filterOn = onOff;
        System.out.println("filtering by " + filter + filterOn);
    }
}
