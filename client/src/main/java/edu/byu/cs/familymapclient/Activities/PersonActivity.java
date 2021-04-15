package edu.byu.cs.familymapclient.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.R;

public class PersonActivity extends AppCompatActivity {

    private static final String PERSON_ID = "person_id";
    private static final String EVENT_ID = "event_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        //need to pass the personID string in as an intent extra
        ExpandableListView expandableListView = findViewById(R.id.person_activity_expandable_list_view);

        Intent intent = getIntent();
        String personID = intent.getExtras().getString(PERSON_ID);

        TextView personName = findViewById(R.id.person_activity_first_name);
        personName.setText(DataCache.getInstance().getPeopleMap().get(personID).getFirstName());

        TextView personLastName = findViewById(R.id.person_activity_last_name);
        personLastName.setText(DataCache.getInstance().getPeopleMap().get(personID).getLastName());

        TextView personGender = findViewById(R.id.person_activity_gender);
        String gender;

        String s = DataCache.getInstance().getPeopleMap().get(personID).getGender();
        if ("f".equals(s)) {
            gender = "female";
        } else if ("m".equals(s)) {
            gender = "male";
        } else {
            throw new IllegalArgumentException("Invalid gender entry: " + DataCache.getInstance().getPeopleMap().get(personID).getGender());
        }
        personGender.setText(gender);

        expandableListView.setAdapter(new ExpandableListAdapter(getAssociatedPeople(personID), DataCache.getInstance().getAssociatedEvents(personID), DataCache.getInstance().getPeopleMap().get(personID)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PersonActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int PERSON_GROUP_POSITION = 0;
        private static final int EVENT_GROUP_POSITION = 1;

        private final List<Person> associatedPeople;
        private List<Event> associatedEvents;
        private final Person originalPerson;

        ExpandableListAdapter(List<Person> people, List<Event> events, Person originalPerson) {
            this.associatedPeople = people;
            this.associatedEvents = events;
            this.originalPerson = originalPerson;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return associatedPeople.size();
                case EVENT_GROUP_POSITION:
                    return associatedEvents.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return associatedPeople.size();
                case EVENT_GROUP_POSITION:
                    return associatedEvents.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return associatedPeople.get(childPosition);
                case EVENT_GROUP_POSITION:
                    return associatedEvents.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.list_title);

            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.person_group_title);
                    break;
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.event_group_title);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item_person, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item_event, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePersonView(View personView, final int childPosition) {
            TextView personNameView = personView.findViewById(R.id.list_item_person_name);
            String personName = associatedPeople.get(childPosition).getFirstName() + " " + associatedPeople.get(childPosition).getLastName();
            personNameView.setText(personName);

            TextView personRelationshipView = personView.findViewById(R.id.list_item_person_relationship);
            personRelationshipView.setText(getRelationship(associatedPeople.get(childPosition)));

            ImageView personIconView = personView.findViewById(R.id.list_item_person_icon);
            if (associatedPeople.get(childPosition).getGender().equals("m")) {
                personIconView.setImageResource(R.drawable.ic_person_male_foreground);
            } else {
                personIconView.setImageResource(R.drawable.ic_person_female_foreground);
            }

            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                    intent.putExtra(PERSON_ID, associatedPeople.get(childPosition).getPersonID());
                    startActivity(intent);
                }
            });
        }

        private void initializeEventView(View eventView, final int childPosition) {
            TextView eventTypeView = eventView.findViewById(R.id.list_item_event_type);
            eventTypeView.setText(associatedEvents.get(childPosition).getEventType());

            TextView eventLocationView = eventView.findViewById(R.id.list_item_event_location);
            String location = associatedEvents.get(childPosition).getCity() + ", " + associatedEvents.get(childPosition).getCountry();
            eventLocationView.setText(location);

            TextView eventDateView = eventView.findViewById(R.id.list_item_event_date);
            String year = Integer.toString(associatedEvents.get(childPosition).getYear());
            eventDateView.setText(year);

            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                    intent.putExtra(EVENT_ID, associatedEvents.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private String getRelationship(Person relative) {
            String originalPersonID = originalPerson.getPersonID();
            String relativePersonID = relative.getPersonID();
            if (originalPerson.getFatherID() != null) {
                if (originalPerson.getFatherID().equals(relativePersonID)) {
                    return "Father";
                } else if (originalPerson.getMotherID().equals(relativePersonID)) {
                    return "Mother";
                }
            }

            if (originalPerson.getSpouseID() != null) {
                if (originalPerson.getSpouseID().equals(relativePersonID)) {
                    return "Spouse";
                }
            }

            if (relative.getFatherID().equals(originalPersonID) || relative.getMotherID().equals(originalPersonID)) {
                return "Child";
            }

            throw new IllegalArgumentException("This person isn't even closely related ya dum dum");
        }
    }

    private List<Person> getAssociatedPeople(String personID) {
        ArrayList<Person> associatedPeople = new ArrayList<Person>();

        if (DataCache.getInstance().getChildrenMap().get(personID).size() > 0) {
            associatedPeople.addAll(DataCache.getInstance().getChildrenMap().get(personID));
        }

        if (DataCache.getInstance().getPeopleMap().get(personID).getSpouseID() != null) {
            associatedPeople.add(
                    DataCache.getInstance().getPeopleMap().get(
                            DataCache.getInstance().getPeopleMap().get(personID).getSpouseID()));
        }

        if (DataCache.getInstance().getPeopleMap().get(personID).getFatherID() != null) {
            associatedPeople.add(DataCache.getInstance().getPeopleMap().get(
                    DataCache.getInstance().getPeopleMap().get(personID).getFatherID()));
            associatedPeople.add(DataCache.getInstance().getPeopleMap().get(
                    DataCache.getInstance().getPeopleMap().get(personID).getMotherID()));
        }

        return associatedPeople;
    }
}
