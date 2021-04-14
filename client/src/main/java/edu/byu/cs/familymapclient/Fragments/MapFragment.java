package edu.byu.cs.familymapclient.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Activities.MainActivity;
import edu.byu.cs.familymapclient.Activities.SearchActivity;
import edu.byu.cs.familymapclient.Activities.SettingsActivity;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Architecture.Settings;
import edu.byu.cs.familymapclient.R;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private final static String PERSON_NAME = "person_name";
    private final static String EVENT_TYPE = "event_type";
    private final static String EVENT_LOCATION = "event_location";
    private final static String GENDER = "gender";
    private final static String PERSON_ID = "person_id";

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_mag_icon:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.settings_icon:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // will need this instance of the map to add lines, markers, and listeners
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        Event[] events = DataCache.getInstance().getEvents();
        Person[] people = DataCache.getInstance().getPersons();

        if (DataCache.getInstance().getColorMap() == null) {
            DataCache.getInstance().setColorMap(new HashMap<String, Float>());
            DataCache.getInstance().setPeopleMap(new HashMap<String, Person>());
            DataCache.getInstance().setEventMap(new HashMap<String, Event>());
            for (Event event : events) {
                if (!DataCache.getInstance().getColorMap().containsKey(event.getEventType().toLowerCase())) {
                    double d = (double) new Random().nextInt(360);
                    DataCache.getInstance().getColorMap().put(event.getEventType(), (float) d);
                }
                DataCache.getInstance().getEventMap().put(event.getEventID(), event);
            }

            for (Person person : people) {
                DataCache.getInstance().getPeopleMap().put(person.getPersonID(), person);
            }

            getAssociatedEvents();
            getChildren();
        }

        final Map<String, Float> mappedColors = DataCache.getInstance().getColorMap();
        final Map<String, Person> mappedPeople = DataCache.getInstance().getPeopleMap();
        final Map<String, Event> mappedEvents = DataCache.getInstance().getEventMap();

        for (Event event : events) {
            LatLng x = new LatLng(event.getLatitude(), event.getLongitude());

            try {
                Marker markerX = map.addMarker(new MarkerOptions()
                        .position(x)
                        .icon(BitmapDescriptorFactory.defaultMarker(mappedColors.get(event.getEventType().toLowerCase()))));
                markerX.setTag(event.getEventID());
            } catch (NullPointerException n) {
                System.out.println(n.getMessage());
            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                String eventID = (String) marker.getTag();
                Event event = mappedEvents.get(eventID);
                Person person = mappedPeople.get(event.getPersonID());

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment eventDisplay = new EventDisplayFragment();
                Bundle arguments = new Bundle();

                arguments.putString(PERSON_NAME, person.getFirstName() + " " + person.getLastName());
                arguments.putString(EVENT_TYPE, event.getEventType() + ", " + event.getYear());
                arguments.putString(EVENT_LOCATION, event.getCity() + ", " + event.getCountry());
                arguments.putString(GENDER, person.getGender());
                arguments.putString(PERSON_ID, person.getPersonID());
                eventDisplay.setArguments(arguments);

                ft.add(R.id.fragment_container, eventDisplay);
                ft.commit();
                return true;
            }
        });
    }

    // need to fix this, should display the line to the earliest event from the spouse's events
   /* private void addSpouseLines(GoogleMap map, Person[] people, Map<PersonIdEventTypeMapKey, Event> mappedEvents) {
        try {
            ArrayList<Person> peopleCopy = new ArrayList<Person>();
            for (Person person : people) {
                if (person.getSpouseID() != null) {
                    peopleCopy.add(person);
                }
            }

            Map<PersonIdEventTypeMapKey, Event> parsedEvents = new HashMap<PersonIdEventTypeMapKey, Event>();

            for (Person person : peopleCopy) {
                PersonIdEventTypeMapKey personKey = new PersonIdEventTypeMapKey(person.getPersonID(), "birth");
                PersonIdEventTypeMapKey spouseKey = new PersonIdEventTypeMapKey(person.getSpouseID(), "birth");

                if ((mappedEvents.containsKey(personKey) && mappedEvents.containsKey(spouseKey))
                    && !(parsedEvents.containsKey(personKey))) {
                    Event birthEvent = mappedEvents.get(personKey);
                    Event spouseBirthEvent = mappedEvents.get(spouseKey);

                    map.addPolyline(new PolylineOptions().add(
                            new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude()),
                            new LatLng(spouseBirthEvent.getLatitude(), spouseBirthEvent.getLongitude())));

                    parsedEvents.put(personKey, birthEvent);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }*/

    private class PersonIdEventTypeMapKey {
        String mPersonID;
        String mEventType;

        public PersonIdEventTypeMapKey(String personID, String eventType) {
            mPersonID = personID;
            mEventType = eventType;
        }

        public String getPersonID() {
            return mPersonID;
        }

        public void setPersonID(String personID) {
            mPersonID = personID;
        }

        public String getEventType() {
            return mEventType;
        }

        public void setEventType(String eventType) {
            mEventType = eventType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersonIdEventTypeMapKey that = (PersonIdEventTypeMapKey) o;
            return Objects.equals(mPersonID, that.mPersonID) &&
                    Objects.equals(mEventType, that.mEventType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mPersonID, mEventType);
        }
    }

    private void getAssociatedEvents() {
        Map<String, List<Event>> eventMap = new HashMap<String, List<Event>>();
        for (Person person : DataCache.getInstance().getPersons()) {
            eventMap.put(person.getPersonID(), new ArrayList<Event>());
        }

        for (Event event : DataCache.getInstance().getEvents()) {
            eventMap.get(event.getPersonID()).add(event);
        }

        DataCache.getInstance().setAssociatedEventMap(eventMap);
    }

    private void getChildren() {
        Map<String, List<Person>> childrenMap = new HashMap<String, List<Person>>();
        for (Person person : DataCache.getInstance().getPersons()) {
            childrenMap.put(person.getPersonID(), new ArrayList<Person>());
        }

        for (Person person : DataCache.getInstance().getPersons()) {
            if (person.getMotherID() != null) {
                childrenMap.get(person.getMotherID()).add(person);
                childrenMap.get(person.getFatherID()).add(person);
            }
        }

        DataCache.getInstance().setChildrenMap(childrenMap);
    }
}
