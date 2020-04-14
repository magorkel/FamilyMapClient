package com.example.familymapclient.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import shared.Model1.Event;
import shared.Model1.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private static final int MARKER_SIZE_VAL = 120;
    private GoogleMap mMap;

    private View view;
    private LinearLayout infoWindow;
    private TextView infoWindowUpperText;
    private TextView infoWindowLowerText;
    private ImageView genderImage;

    private HashMap<Marker, Event> markerEventHashMap;

    private Event selectedEvent;
    private int lineColor;

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        TreeMap<String, Event> events = UserInfo.getUserInfo().getEvents();
        UserInfo.getUserInfo().setEvents(events);
        HashSet<Event> filteredEvents = UserInfo.getUserInfo().getFilteredEvents();
        markerEventHashMap = UserInfo.getUserInfo().getMarkerEventHashMap();

        for (Event event : events.values())
        {
            double latitude = event.getLatitude();
            double longitude = event.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            //markerOptions.title(event.getEventType());
            Drawable color = color(UserInfo.getUserInfo().getEventTypeColors().get(event.getEventType()));
            markerOptions.icon(getMarkerIconFromDrawable(color));
            Marker marker = mMap.addMarker(markerOptions);
            markerEventHashMap.put(marker, event);
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
        eventWindow();
        return false;
    }

    private void eventWindow()
    {
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
