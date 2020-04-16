package com.example.familymapclient.Fragments;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymapclient.Activities.PersonActivity;
import com.example.familymapclient.Activities.SearchActivity;
import com.example.familymapclient.Activities.SettingsActivity;
import com.example.familymapclient.Lines;
import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import shared.Model1.Event;
import shared.Model1.Person;
import shared.Model1.User;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private static final int MARKER_SIZE_VAL = 120;
    private GoogleMap mMap;

    ArrayList<Polyline> polylines = new ArrayList<>();

    private View view;
    private LinearLayout infoWindow;
    private TextView infoWindowUpperText;
    private TextView infoWindowLowerText;
    private ImageView genderImage;

    private HashMap<Marker, Event> markerEventHashMap;

    private Event selectedEvent;

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        TreeMap<String, Event> events = UserInfo.getUserInfo().getEvents();
        UserInfo.getUserInfo().setEvents(events);
        markerEventHashMap = UserInfo.getUserInfo().getMarkerEventHashMap();

        makeFilters();
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();

        for (Event event : filteredEvents)
        {
            double latitude = event.getLatitude();
            double longitude = event.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            Drawable color = color(UserInfo.getUserInfo().getEventTypeColors().get(event.getEventType()));
            markerOptions.icon(getMarkerIconFromDrawable(color));
            Marker marker = mMap.addMarker(markerOptions);
            markerEventHashMap.put(marker, event);
        }

        String eventID = getActivity().getIntent().getStringExtra("currentEvent");
        if(eventID != null)
        {
            selectedEvent = UserInfo.getUserInfo().getEvent(eventID);
            eventWindow();
        }
    }

    private static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable)
    {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(MARKER_SIZE_VAL, MARKER_SIZE_VAL, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, MARKER_SIZE_VAL, MARKER_SIZE_VAL);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Drawable color(String color)
    {
        Drawable drawable;
        switch (color)
        {
            case "Blue": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.blue);
            case "Green": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.green);
            case "Orange": return  drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.orange);
            case "Yellow": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.yellow);
            case "Red": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.red);
            case "Azure": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.azure);
            case "Violet": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.violet);
            case "Dark Green": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.dark_green);
            case "Maroon": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.maroon);
            case "Light Purple": return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.light_purple);
            default:
                return drawable = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.cyan);
        }
    }

    public MapFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        HashSet<Event> filter = new HashSet<>();
        filter.addAll(UserInfo.getUserInfo().getJustEvents());
        UserInfo.getUserInfo().setFilteredEvents(filter);
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.map_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.search_icon:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.settings_icon:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //when they click on a button in toolbar this one gets activated
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        //UserInfo.getUserInfo();
        infoWindow = view.findViewById(R.id.infoWindow);
        infoWindow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getActivity(),"reached onClick", Toast.LENGTH_SHORT).show();
                infoWindowClicked();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    private void infoWindowClicked()
    {
        if (selectedEvent == null) return;
        UserInfo userInfo = UserInfo.getUserInfo();

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("currentPerson", selectedEvent.getPersonID());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        //say what happens when they click on a marker - this is where the person and event activities get called
        Event event = markerEventHashMap.get(marker);
        selectedEvent = event;

        //this is where lines get made
        makeFilters();
        makePolyLines();
        eventWindow();
        return false;
    }

    private void makePolyLines()
    {
        for(Polyline line : polylines)
        {
            line.remove();
        }
        polylines.clear();

        if(UserInfo.getUserInfo().isSpouseLinesOn() && UserInfo.getUserInfo().isMaleEventsOn() && UserInfo.getUserInfo().isFemaleEventsOn()) makeSpouseLines(); //blue
        if(UserInfo.getUserInfo().isFamilyTreeLineOn()) makeFamilyLines(); //yellow
        if(UserInfo.getUserInfo().isLifeStoryLineOn()) makeLifeLines(); //green
    }

    private void makeFilters()
    {
        if(!UserInfo.getUserInfo().isMaleEventsOn()) filterMaleEvents();
        if(!UserInfo.getUserInfo().isFemaleEventsOn()) filterFemaleEvents();
        /*if(!UserInfo.getUserInfo().isFathersSideOn())
        {
            HashSet<Event> fathersSide = new HashSet<>();
            fathersSide.addAll(UserInfo.getUserInfo().getFathersSideEvents());
            UserInfo.getUserInfo().setFilteredEvents(fathersSide);
        }*/
    }

    private void makeSpouseLines()
    {
        String personID = selectedEvent.getPersonID();
        Person person = UserInfo.getUserInfo().getPerson(personID);
        Person spouse = UserInfo.getUserInfo().getPerson(person.getSpouseID());
        if(spouse != null)
        {
            List<Event> spouseEvents = new ArrayList<>();
            for (Event event : UserInfo.getUserInfo().getJustEvents())
            {
                if (event.getPersonID().equals(spouse.getPersonID())) { spouseEvents.add(event); }
            }
            Collections.sort(spouseEvents, new EventsCompare());

            LatLng currentEvent = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            LatLng spouseEvent = new LatLng(spouseEvents.get(0).getLatitude(), spouseEvents.get(0).getLongitude());
            polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, spouseEvent).width(10).color(Color.BLUE)));
        }
    }

    private void makeFamilyLines()
    {
        int width = 10;
        String personID = selectedEvent.getPersonID();
        Person person = UserInfo.getUserInfo().getPerson(personID);

        //father
        String fatherID = person.getFatherID();
        if(fatherID != null)
        {
            Person father = UserInfo.getUserInfo().getPerson(fatherID);
            List<Event> fatherEvents = new ArrayList<>();
            for (Event event : UserInfo.getUserInfo().getJustEvents())//
            {
                if(event.getPersonID().equals(father.getPersonID())) { fatherEvents.add(event); }
            }
            Collections.sort(fatherEvents, new EventsCompare());

            LatLng currentEvent = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            LatLng fatherEvent = new LatLng(fatherEvents.get(0).getLatitude(), fatherEvents.get(0).getLongitude());
            if(UserInfo.getUserInfo().isMaleEventsOn()) polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, fatherEvent).width(width).color(Color.YELLOW)));
            //call helper function - width needs to be passed in so it can get smaller
            makeFamilyLinesHelper(width, father, fatherEvents.get(0));
        }

        //mother
        String motherID = person.getMotherID();
        if(motherID != null)
        {
            Person mother = UserInfo.getUserInfo().getPerson(motherID);
            List<Event> motherEvents = new ArrayList<>();
            for (Event event : UserInfo.getUserInfo().getJustEvents())//
            {
                if(event.getPersonID().equals(mother.getPersonID())) { motherEvents.add(event); }
            }
            Collections.sort(motherEvents, new EventsCompare());

            LatLng currentEvent = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            LatLng motherEvent = new LatLng(motherEvents.get(0).getLatitude(), motherEvents.get(0).getLongitude());
            if(UserInfo.getUserInfo().isFemaleEventsOn()) polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, motherEvent).width(width).color(Color.YELLOW)));
            //call helper function
            makeFamilyLinesHelper(width, mother, motherEvents.get(0));
        }
    }

    private void makeFamilyLinesHelper(int w , Person person, Event inputEvent)
    {
        int width = w-2;
        //father
        String fatherID = person.getFatherID();
        if(fatherID != null)
        {
            Person father = UserInfo.getUserInfo().getPerson(fatherID);
            List<Event> fatherEvents = new ArrayList<>();
            for (Event event : UserInfo.getUserInfo().getJustEvents())//getJustEvents()
            {
                if(event.getPersonID().equals(father.getPersonID())) { fatherEvents.add(event); }
            }
            Collections.sort(fatherEvents, new EventsCompare());

            LatLng currentEvent = new LatLng(inputEvent.getLatitude(), inputEvent.getLongitude());
            LatLng fatherEvent = new LatLng(fatherEvents.get(0).getLatitude(), fatherEvents.get(0).getLongitude());
            if(UserInfo.getUserInfo().isMaleEventsOn()) polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, fatherEvent).width(width).color(Color.YELLOW)));
            //call helper function - width needs to be passed in so it can get smaller
            makeFamilyLinesHelper(width, father, fatherEvents.get(0));
        }

        //mother
        String motherID = person.getMotherID();
        if(motherID != null)
        {
            Person mother = UserInfo.getUserInfo().getPerson(motherID);
            List<Event> motherEvents = new ArrayList<>();
            for (Event event : UserInfo.getUserInfo().getJustEvents())//
            {
                if(event.getPersonID().equals(mother.getPersonID())) { motherEvents.add(event); }
            }
            Collections.sort(motherEvents, new EventsCompare());

            LatLng currentEvent = new LatLng(inputEvent.getLatitude(), inputEvent.getLongitude());
            LatLng motherEvent = new LatLng(motherEvents.get(0).getLatitude(), motherEvents.get(0).getLongitude());
            if(UserInfo.getUserInfo().isFemaleEventsOn()) polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, motherEvent).width(width).color(Color.YELLOW)));
            //call helper function
            makeFamilyLinesHelper(width, mother, motherEvents.get(0));
        }
    }

    private void makeLifeLines()
    {
        String personID = selectedEvent.getPersonID();
        Person person = UserInfo.getUserInfo().getPerson(personID);

        List<Event> lifeEvents = new ArrayList<>();
        lifeEvents.add(selectedEvent);
        for (Event event : UserInfo.getUserInfo().getJustEvents())
        {
            if (event.getPersonID().equals(person.getPersonID())) { lifeEvents.add(event); }
        }
        Collections.sort(lifeEvents, new EventsCompare());

        for(int i = 0; i < lifeEvents.size()-1; i++)
        {
            LatLng currentEvent = new LatLng(lifeEvents.get(i).getLatitude(), lifeEvents.get(i).getLongitude());
            LatLng lifeEvent = new LatLng(lifeEvents.get(i+1).getLatitude(), lifeEvents.get(i+1).getLongitude());
            polylines.add(mMap.addPolyline(new PolylineOptions().add(currentEvent, lifeEvent).width(10).color(Color.GREEN)));
        }
    }

    private static class EventsCompare implements Comparator<Event>
    {
        //-1 means o1 comes first
        //1 means o2 comes first
        //0 means they are tied

        @Override
        public int compare(Event o1, Event o2)
        {
            if (o1.getEventID().toLowerCase().equals("birth") || o2.getEventType().toLowerCase().equals("death")) return -1;
            else if (o2.getEventType().toLowerCase().equals("birth") || o1.getEventType().toLowerCase().equals("death")) return 1;
            else if (o1.getYear() < o2.getYear()) return -1;
            else if (o2.getYear() < o1.getYear()) return 1;
            else return o1.getEventType().compareTo(o2.getEventType());
        }
    }

    private void filterMaleEvents()
    {
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();
        HashSet<Event> femaleEvents = new HashSet<>();
        for(Event event : filteredEvents)
        {
            String personID = event.getPersonID();
            Person person = UserInfo.getUserInfo().getPerson(personID);
            if(person.getGender().equals("f"))
            {
                femaleEvents.add(event);
            }
        }
        UserInfo.getUserInfo().setFilteredEvents(femaleEvents);
    }

    private void filterFemaleEvents()
    {
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();
        HashSet<Event> maleEvents = new HashSet<>();
        for(Event event : filteredEvents)
        {
            String personID = event.getPersonID();
            Person person = UserInfo.getUserInfo().getPerson(personID);
            if(person.getGender().equals("m"))
            {
                maleEvents.add(event);
            }
        }
        UserInfo.getUserInfo().setFilteredEvents(maleEvents);
    }

    /*HashSet<Event> fathersSide = new HashSet<>();
    private void filterFatherSide()
    {
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();

        String personID = selectedEvent.getPersonID();
        Person person = UserInfo.getUserInfo().getPerson(personID);
        String fatherID = person.getFatherID();
        Person father = UserInfo.getUserInfo().getPerson(fatherID);

        for(Event event : filteredEvents)
        {
            if(event.getPersonID().equals(fatherID)) fathersSide.add(event);
        }
        if(fatherID != null) filterSide(father);
        UserInfo.getUserInfo().setFilteredEvents(fathersSide);
    }

    private void filterSide(Person person)
    {
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();
        String fatherID = person.getFatherID();
        Person father = UserInfo.getUserInfo().getPerson(fatherID);
        for(Event event : filteredEvents)
        {
            if(event.getPersonID().equals(fatherID)) fathersSide.add(event);
        }
        String motherID = person.getMotherID();
        Person mother = UserInfo.getUserInfo().getPerson(motherID);
        for(Event event : filteredEvents)
        {
            if(event.getPersonID().equals(motherID)) fathersSide.add(event);
        }
        if(fatherID != null) filterSide(father);
        if(motherID != null) filterSide(mother);
    }*/

    private void eventWindow()
    {
        LatLng position = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 4));

        infoWindowUpperText = view.findViewById(R.id.name_text);
        infoWindowLowerText = view.findViewById(R.id.event_text);
        genderImage = view.findViewById(R.id.gender_icon);

        Person person = UserInfo.getUserInfo().getPerson(selectedEvent.getPersonID());
        String name = person.getFirstName() + " " + person.getLastName();

        String event = selectedEvent.getEventType() + ": " + selectedEvent.getCity() + ", " + selectedEvent.getCountry() + " (" + selectedEvent.getYear() + ")";

        infoWindowUpperText.setText(name);
        infoWindowLowerText.setText(event);

        Drawable maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
        Drawable femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);

        if(person.getGender().equals("m")) genderImage.setImageDrawable(maleIcon);
        else genderImage.setImageDrawable(femaleIcon);
    }
}
