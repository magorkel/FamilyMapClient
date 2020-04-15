package com.example.familymapclient;

import com.google.android.gms.maps.model.Marker;

import shared.Model1.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import shared.Model1.Event;

public class UserInfo
{
    private static UserInfo userInfo;
    private String authToken;
    private String personID;
    private TreeMap<String, Person> persons;
    private TreeMap<String, Event> events;

    private ArrayList<String> eventTypes = new ArrayList<>();
    private TreeMap<String, Filter> filters = new TreeMap<>();
    private HashSet<Event> filteredEvents = new HashSet<>();
    private HashMap<Marker, Event>  markerEventHashMap = new HashMap<>();

    private Event selectedEvent;
    private TreeMap<String, Person> fatherSide = new TreeMap<>();
    private TreeMap<String, Person> motherSide = new TreeMap<>();
    private TreeMap<String, String> eventTypeColors = new TreeMap<>();

    //private HashMap<String, List<Event>> eventMap;
    //private HashMap<String, List<Person>> personMap;
    private ArrayList<Event> justEvents;
    private ArrayList<Person> justPersons;

    private UserInfo() {}

    public static UserInfo getUserInfo()
    {
        if (userInfo == null) { userInfo = new UserInfo(); }
        return userInfo;
    }

    /*public void makeFilters()
    {
        Filter filter = new Filter("");
        eventTypes = filter.eventTypes();
        filters.clear();

        for (String eventType : eventTypes)
        {
            filters.put(eventType + " Events", new Filter(eventType + " Events"));
        }

        filters.put("Father's Side", new Filter("Father's Side"));
        filters.put("Mother's Side", new Filter("Mother's Side"));
        filters.put("Male Events", new Filter("Male Events"));
        filters.put("Female Events", new Filter("Female Events"));
    }

    public void applyFilters()
    {
        filteredEvents.clear();

        Person person = persons.get(personID);

        if(person.getFatherID() != null && persons.containsKey(person.getFatherID()))
        {
            Person father = persons.get(person.getFatherID());
            fatherSide.put(father.getPersonID(), father);
            findParents(father, fatherSide);
        }
        if(person.getMotherID() != null && persons.containsKey(person.getMotherID()))
        {
            Person mother = persons.get(person.getMotherID());
            motherSide.put(mother.getPersonID(), mother);
            findParents(mother, motherSide);
        }

        TreeMap<String, Event> parentFilteredEvents = new TreeMap<>();
        if(filters.get("Father's Side").isFilterOn() && !filters.get("Mother's Side").isFilterOn())
        {
            for (Event event : events.values())
            {
                if (fatherSide.containsKey(event.getPersonID()))
                {
                    parentFilteredEvents.put(event.getEventID(), event);
                }
            }
        }
        else if (!filters.get("Father's Side").isFilterOn() && filters.get("Mother's Side").isFilterOn())
        {
            for (Event event : events.values())
            {
                if (motherSide.containsKey(event.getPersonID()))
                {
                    parentFilteredEvents.put(event.getEventID(), event);
                }
            }
        }
        else if (!filters.get("Father's Side").isFilterOn() && !filters.get("Mother's Side").isFilterOn())
        {

        }
        else
        {
            parentFilteredEvents = events;
        }

        for (Event event : parentFilteredEvents.values())
        {
            String filteredName = event.getEventType();
            if (!filters.get(filteredName).isFilterOn()) continue;

            String personID = event.getPersonID();
            char gender = findPersonGender(personID);
            if (!filters.get("Male Events").isFilterOn() && gender == 'm') continue;;
            if (!filters.get("Female Events").isFilterOn() && gender == 'f') continue;

            filteredEvents.add(event);
        }
    }*/

    private void findParents(Person person, TreeMap family)
    {
        if (person.getFatherID() != null && persons.containsKey(person.getFatherID()))
        {
            Person father = persons.get(person.getFatherID());
            family.put(father.getPersonID(), father);
            findParents(father, family);
        }
        if (person.getMotherID() != null && persons.containsKey(person.getMotherID()))
        {
            Person mother = persons.get(person.getMotherID());
            family.put(mother.getPersonID(), mother);
            findParents(mother, family);
        }
    }

    public char findPersonGender(String personID) {

        return persons.get(personID).getGender().charAt(0);
    }

    public Person getPerson (String personID)
    {
        try { return persons.get(personID); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public Event getEvent (String eventID)
    {
        try { return events.get(eventID); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public String getAuthToken() { return authToken; }

    public void setAuthToken (String authToken) { this.authToken = authToken; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public TreeMap<String, Person> getPersons() { return persons; }

    public void setPersons(TreeMap<String, Person> persons) { this.persons = persons; }

    public TreeMap<String, Event> getEvents() { return events; }

    public void setEvents(TreeMap<String, Event> events)
    {
        this.events = events;
        ArrayList<String> eventTypes = new ArrayList<>();

        for (Event event : userInfo.getEvents().values())
        {
            if (!eventTypes.contains(event.getEventType()))
            {
                eventTypes.add(event.getEventType());
            }
        }

        ArrayList<String> colors = new ArrayList<>();
        colors.add("Blue");
        colors.add("Green");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("Red");
        colors.add("Azure");
        colors.add("Violet");
        colors.add("Cyan");

        int i = 0;
        for (String eventType : eventTypes)
        {
            eventTypeColors.put(eventType, colors.get(i));
            i++;
            if (i > colors.size()) { i = 0; }
        }
    }

    public HashSet<Event> getFilteredEvents()
    {
        return filteredEvents;
    }

    public void setFilteredEvents(HashSet<Event> filteredEvents)
    {
        this.filteredEvents = filteredEvents;
    }

    public HashMap<Marker, Event> getMarkerEventHashMap()
    {
        return markerEventHashMap;
    }

    public void setMarkerEventHashMap(HashMap<Marker, Event> markerEventHashMap)
    {
        this.markerEventHashMap = markerEventHashMap;
    }

    public TreeMap<String, String> getEventTypeColors()
    {
        return eventTypeColors;
    }

    public void setEventTypeColors(TreeMap<String, String> eventTypeColors)
    {
        this.eventTypeColors = eventTypeColors;
    }

    /*public HashMap<String, List<Event>> getEventMap()
    {
        List<Event> justEvents = new ArrayList<>();
        justEvents.addAll(userInfo.getEvents().values());
        eventMap.put("Life Events", justEvents);
        return eventMap;
    }

    public HashMap<String, List<Person>> getPersonMap()
    {
        List<Person> justPersons = new ArrayList<>();
        justPersons.addAll(userInfo.getPersons().values());
        personMap.put("Family", justPersons);
        return personMap;
    }*/

    public ArrayList<Event> getJustEvents()
    {
        justEvents = new ArrayList<>();
        justEvents.addAll(userInfo.getEvents().values());
        return justEvents;
    }

    public ArrayList<Person> getJustPersons()
    {
        justPersons = new ArrayList<>();
        justPersons.addAll(userInfo.getPersons().values());
        return justPersons;
    }
}
