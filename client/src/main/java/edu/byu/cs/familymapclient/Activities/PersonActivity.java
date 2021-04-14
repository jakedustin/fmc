package edu.byu.cs.familymapclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        //need to pass the personID string in as an intent extra
        ExpandableListView expandableListView = findViewById(R.id.person_activity_expandable_list_view);

        Intent intent = getIntent();
        String personID = intent.getExtras().getString(PERSON_ID);

        expandableListView.setAdapter(new ExpandableListAdapter(getAssociatedPeople(personID), getAssociatedEvents(personID)));

    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int PERSON_GROUP_POSITION = 0;
        private static final int EVENT_GROUP_POSITION = 1;

        private final List<Person> associatedPeople;
        private final List<Event> associatedEvents;

        ExpandableListAdapter(List<Person> people, List<Event> events) {
            this.associatedPeople = people;
            this.associatedEvents = events;
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
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
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

    private List<Event> getAssociatedEvents(String personID) {
        ArrayList<Event> associatedEvents = (ArrayList<Event>) DataCache.getInstance().getAssociatedEventMap().get(personID);

        return associatedEvents;
    }
}
