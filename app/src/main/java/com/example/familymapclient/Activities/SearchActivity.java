package com.example.familymapclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import shared.Model1.Event;
import shared.Model1.Person;

public class SearchActivity extends AppCompatActivity
{
    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;

    String searchWord;

    SearchAdapter adapter;

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
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchView searchView = findViewById(R.id.search);
        //searchWord = (String) searchView.getQuery();

        final List<Event> events = UserInfo.getUserInfo().getJustEvents();
        final List<Person> persons = UserInfo.getUserInfo().getJustPersons();

        final List<Event> filteredEvents = new ArrayList<>();
        final List<Person> filteredPersons = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filteredEvents.clear();
                filteredPersons.clear();
                String q = query.toLowerCase();
                for(Event event : events)
                {
                    Integer year = event.getYear();
                    if (event.getCountry().toLowerCase().contains(q) || event.getCity().toLowerCase().contains(q) || event.getEventType().toLowerCase().contains(q) || year.toString().contains(q))
                    {
                        filteredEvents.add(event);
                    }
                }
                for(Person person : persons)
                {
                    if (person.getFirstName().toLowerCase().contains(q) || person.getLastName().toLowerCase().contains(q))
                    {
                        filteredPersons.add(person);
                    }
                }
                if (filteredEvents.size() == 0 && filteredPersons.size() == 0)
                {
                    Toast.makeText(SearchActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        adapter = new SearchAdapter(filteredEvents, filteredPersons);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map: Person Details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>
    {
        private final List<Event> events;
        private final List<Person> persons;

        SearchAdapter(List<Event> events, List<Person> persons)
        {
            this.events = events;
            this.persons = persons;
        }

        @Override
        public int getItemViewType(int position)
        {
            return position < events.size() ? EVENT_ITEM_VIEW_TYPE :PERSON_ITEM_VIEW_TYPE;
            //return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position)
        {
            if (position < persons.size()){ holder.bind(persons.get(position)); }
            else if(position < events.size() + persons.size()) { holder.bind(events.get(position - persons.size())); }
            else
            {
                System.out.println("don't know what's going on");
            }
        }

        @Override
        public int getItemCount() { return events.size() + persons.size(); }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView top;
        private final TextView bottom;
        private final ImageView icon;

        private final int viewType;
        private Event event;
        private Person person;

        SearchViewHolder (View view, int viewType)
        {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            top = itemView.findViewById(R.id.item);
            bottom = itemView.findViewById(R.id.subtitle);
            icon = itemView.findViewById(R.id.icon);
        }

        private void bind(Event event)
        {
            this.event = event;
            String personID = event.getPersonID();
            Person person = UserInfo.getUserInfo().getPerson(personID);
            top.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            bottom.setText(person.getFirstName() + " " + person.getLastName());

            Drawable mapIcon = color(Objects.requireNonNull(UserInfo.getUserInfo().getEventTypeColors().get(event.getEventType())));
            icon.setImageDrawable(mapIcon);
        }

        private void bind(Person person)
        {
            this.person = person;
            top.setText(person.getFirstName() + " " + person.getLastName());
            bottom.setText("");

            Drawable maleIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
            Drawable femaleIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);
            if(person.getGender().equals("m")) { icon.setImageDrawable(maleIcon); }
            else { icon.setImageDrawable(femaleIcon); }
        }

        @Override
        public void onClick(View v)
        {
            if(viewType == EVENT_ITEM_VIEW_TYPE)
            {
                //String eventID = event.getEventID();
                //Event clickedEvent = eventsToGive.get(childPosition);
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("currentEvent", event.getEventID());
                startActivity(intent);
            }
            else
            {
                //Person clickedPerson = personsToGive.get(childPosition);
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("currentPerson", person.getPersonID());
                startActivity(intent);
            }
        }

        private Drawable color(String color)
        {
            Drawable drawable;
            switch (color)
            {
                case "Blue": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.blue);
                case "Green": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green);
                case "Orange": return  drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.orange);
                case "Yellow": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.yellow);
                case "Red": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.red);
                case "Azure": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.azure);
                case "Violet": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.violet);
                case "Dark Green": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.dark_green);
                case "Maroon": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.maroon);
                case "Light Purple": return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.light_purple);
                default:
                    return drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.cyan);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        return super.onCreateOptionsMenu(menu);
    }
}
