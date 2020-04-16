package com.example.familymapclient;

import java.util.TreeSet;

import shared.Model1.Event;
import shared.Model1.Person;

public class Lines
{
    private TreeSet<Event> lifeStoryEvents;
    private TreeSet<Event> spouseEvents;

    public Lines(Event event)
    {
        lifeStoryEvents = new TreeSet<>();
        spouseEvents = new TreeSet<>();
        makeTheLines(event);
    }

    private void makeTheLines(Event event)
    {
        Person person = UserInfo.getUserInfo().getPersons().get(event.getPersonID());

        for(Event nextEvent : UserInfo.getUserInfo().getFilteredEvents())
        {
            if (nextEvent.getPersonID().equals(event.getPersonID()))
            {
                lifeStoryEvents.add(nextEvent);
            }
        }
    }
}
