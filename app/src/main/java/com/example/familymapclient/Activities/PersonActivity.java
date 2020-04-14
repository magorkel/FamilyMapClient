package com.example.familymapclient.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.R;
import com.example.familymapclient.UserInfo;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import shared.Model1.Event;
import shared.Model1.Person;

public class PersonActivity extends AppCompatActivity
{
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;

    TextView firstName;
    TextView lastName;
    TextView gender;

    private Person currentPerson;

    private class ExpandableListViewClass extends BaseExpandableListAdapter
    {
        List<Person> persons;
        List<Event> events;

        private ExpandableListViewClass(List<Person> persons, List<Event> events)
        {
            this.persons = persons;
            this.events = events;
        }

        @Override
        public int getGroupCount() { return 2; }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            if (groupPosition == 0) { return events.size(); }
            else { return persons.size(); }
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            //returns string for header
            if (groupPosition == 0) { return "Life Events";}
            else { return "Family Members"; }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            if (groupPosition  == 0) { return events.get(childPosition); }
            else { return persons.get(childPosition); }
        }

        @Override
        public long getGroupId(int groupPosition) { return groupPosition; }

        @Override
        public long getChildId(int groupPosition, int childPosition) { return groupPosition * childPosition; }

        @Override
        public boolean hasStableIds() { return false; }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);
            TextView view = (TextView) layoutInflater.inflate(R.layout.expandable_view, parent, false);//expandable view - is this right??
            view.setText(expandableListTitle.get(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            TextView item = view.findViewById(R.id.item);
            TextView subtitle = view.findViewById(R.id.subtitle);
            ImageView icon = view.findViewById(R.id.icon);

            if(groupPosition == 0)
            {
                Event event = events.get(childPosition);
                String line = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
                item.setText(line);
                subtitle.setText(currentPerson.getFirstName() + " "+ currentPerson.getLastName());
                Drawable mapIcon = color(Objects.requireNonNull(UserInfo.getUserInfo().getEventTypeColors().get(event.getEventType())));
                icon.setImageDrawable(mapIcon);
            }
            else
            {
                Person person = persons.get(childPosition);
                String line = person.getFirstName() + " " + person.getLastName();
                item.setText(line);

                Drawable maleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
                Drawable femaleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);

                String relationship = "";

                if(person.getFatherID() != null && person.getFatherID().equals(currentPerson.getPersonID()))
                {
                    relationship = "Child";
                    if(person.getGender().equals("m")) { icon.setImageDrawable(maleIcon); }
                    else { icon.setImageDrawable(femaleIcon); }
                }
                else if(person.getMotherID() != null && person.getMotherID().equals(currentPerson.getPersonID()))
                {
                    relationship = "Child";
                    if(person.getGender().equals("m")) { icon.setImageDrawable(maleIcon); }
                    else { icon.setImageDrawable(femaleIcon); }
                }
                else if(person.getSpouseID() != null && person.getSpouseID().equals(currentPerson.getPersonID()))
                {
                    relationship = "Spouse";
                    if(person.getGender().equals("m")) { icon.setImageDrawable(maleIcon); }
                    else { icon.setImageDrawable(femaleIcon); }
                }
                else if(currentPerson.getFatherID() != null && currentPerson.getFatherID().equals(person.getPersonID()))
                {
                    relationship = "Father";
                    icon.setImageDrawable(maleIcon);
                }
                else if(currentPerson.getMotherID() != null && currentPerson.getMotherID().equals(person.getPersonID()))
                {
                    relationship = "Mother";
                    icon.setImageDrawable(femaleIcon);
                }
                subtitle.setText(relationship);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

        private Drawable color(String color)
        {
            Drawable drawable;
            switch (color)
            {
                case "Blue": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.blue);
                case "Green": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green);
                case "Orange": return  drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.orange);
                case "Yellow": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.yellow);
                case "Red": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.red);
                case "Azure": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.azure);
                case "Violet": return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.violet);
                default:
                    return drawable = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.cyan);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        String personID = getIntent().getStringExtra("currentPerson");
        currentPerson = UserInfo.getUserInfo().getPerson(personID);

        String genderLetter = currentPerson.getGender();
        String genderWord;
        if (genderLetter.equals("m")) genderWord = "Male";
        else genderWord = "Female";

        firstName = findViewById(R.id.first_name_text);
        lastName = findViewById(R.id.last_name_text);
        gender = findViewById(R.id.gender_text);

        firstName.setText(currentPerson.getFirstName());
        lastName.setText(currentPerson.getLastName());
        gender.setText(genderWord);

        expandableListTitle = new ArrayList<>();
        expandableListTitle.add("Life Events");
        expandableListTitle.add("Family");

        //findViewById - to get expandable view
        expandableListView = findViewById(R.id.life_events_and_family);

        ArrayList<Event> eventsFromUserInfo = UserInfo.getUserInfo().getJustEvents();
        ArrayList<Event> eventsToGive = new ArrayList<>();
        for (Event event : eventsFromUserInfo)
        {
            if (event.getPersonID().equals(currentPerson.getPersonID())) { eventsToGive.add(event); }
        }

        ArrayList<Person> personsFromUserInfo = UserInfo.getUserInfo().getJustPersons();
        final ArrayList<Person> personsToGive = new ArrayList<>();
        for (Person person : personsFromUserInfo)
        {
            if (currentPerson.getSpouseID() != null && currentPerson.getSpouseID().equals(person.getPersonID())) { personsToGive.add(person); } //spouse
            else if (person.getFatherID() != null && person.getFatherID().equals(currentPerson.getPersonID())) { personsToGive.add(person); } //child
            else if (person.getMotherID() != null && person.getMotherID().equals(currentPerson.getPersonID())) { personsToGive.add(person); } //child
            else if (currentPerson.getFatherID() != null && currentPerson.getFatherID().equals(person.getPersonID())) { personsToGive.add(person); } //father
            else if (currentPerson.getMotherID() != null && currentPerson.getMotherID().equals(person.getPersonID())) { personsToGive.add(person); } //mother
            //System.out.println(person.getPersonID());
        }

        expandableListAdapter = new ExpandableListViewClass(personsToGive, eventsToGive);
        expandableListView.setAdapter(expandableListAdapter);
        //construct adapter and save to datamember of this class to access in later functions

        //onChildClickListener - expandable view - not adapter - tells what happens when you click on an event
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                //0 event
                if (groupPosition == 0)
                {

                }
                else
                {
                    Person clickedPerson = personsToGive.get(childPosition);
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("currentPerson", clickedPerson.getPersonID());
                    startActivity(intent);
                }
                return false;
            }
        });


        //expandable view.setAdapter
        //put adapter into function
    }


}
