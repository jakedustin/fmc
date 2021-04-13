package edu.byu.cs.familymapclient.Fragments;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Architecture.Settings;
import edu.byu.cs.familymapclient.R;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

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
        MenuItem searchItem = menu.findItem(R.id.search_mag_icon);
        MenuItem settingsItem = menu.findItem(R.id.settings_icon);
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

        Map<String, Float> mappedColors = new HashMap<String, Float>();
        Map<PersonIdEventTypeMapKey, Event> mappedEvents = new HashMap<PersonIdEventTypeMapKey, Event>();

        for (Event event : events) {
            if (!mappedColors.containsKey(event.getEventType().toLowerCase())) {
                double d = (double) new Random().nextInt(360);
                mappedColors.put(event.getEventType(), (float) d);
            }

            mappedEvents.put(new PersonIdEventTypeMapKey(event.getPersonID(), event.getEventType().toLowerCase()), event);

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

        if (Settings.getInstance().isShowSpouseLines()) {
            addSpouseLines(map, people, mappedEvents);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                String eventID = (String) marker.getTag();
                Toast.makeText(getActivity(), eventID, Toast.LENGTH_SHORT).show();
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });
    }

    private void addSpouseLines(GoogleMap map, Person[] people, Map<PersonIdEventTypeMapKey, Event> mappedEvents) {
        ArrayList<Person> peopleCopy = new ArrayList<Person>();
        for (Person person : people) {
            if (person.getSpouseID() != null) {
                peopleCopy.add(person);
            }
        }

        for (Person person : peopleCopy) {
            Event birthEvent = mappedEvents.get(new PersonIdEventTypeMapKey(person.getPersonID(), "birth"));
            Event spouseBirthEvent = mappedEvents.get(new PersonIdEventTypeMapKey(person.getSpouseID(), "birth"));

            Polyline p = map.addPolyline(new PolylineOptions().add(
                    new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude()),
                    new LatLng(spouseBirthEvent.getLatitude(), spouseBirthEvent.getLongitude())));
        }


        //need to run through events
        //on finding a birth event, get the associated person object
        //get the associated spouse
        //get associated spouse birth
        //draw line between births
        //pop people and births off their associated arrays
    }

    private Map<String, String> mapPeople(Person[] people) {
        HashMap<String, String> mappedPeople = new HashMap<String, String>();
        for (Person person : people) {
            mappedPeople.put(person.getPersonID(), person.getFirstName() + " " + person.getLastName());
        }

        return mappedPeople;
    }

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
}
