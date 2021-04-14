package edu.byu.cs.familymapclient.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Activities.MainActivity;
import edu.byu.cs.familymapclient.Activities.SearchActivity;
import edu.byu.cs.familymapclient.Activities.SettingsActivity;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Architecture.Settings;
import edu.byu.cs.familymapclient.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private final static String PERSON_NAME = "person_name";
    private final static String EVENT_TYPE = "event_type";
    private final static String EVENT_LOCATION = "event_location";
    private final static String GENDER = "gender";
    private final static String PERSON_ID = "person_id";
    private final static String EVENT_ID = "event_id";
    private LatLng init = null;
    private boolean displayMenu = true;
    private String eventID;

    public MapFragment() {

    }

    public MapFragment(String eventID) {
        init = new LatLng(DataCache.getInstance().getEventMap().get(eventID).getLatitude(),
                            DataCache.getInstance().getEventMap().get(eventID).getLongitude());
        displayMenu = false;
        this.eventID = eventID;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setHasOptionsMenu(displayMenu);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (displayMenu) {
            inflater.inflate(R.menu.fragment_map, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
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
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        setUpMap();

        map.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            String _eventID = (String) marker.getTag();
            Event event = DataCache.getInstance().getEventMap().get(_eventID);
            Person person = DataCache.getInstance().getPeopleMap().get(event.getPersonID());

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
    };

    @Override
    public void onResume() {
        super.onResume();

        if (Settings.settingsHaveChanged()) {
            map.clear();
            setUpMap();
        }
    }

    private void setUpMap() {
        ArrayList<Event> events = DataCache.getInstance().getRelevantEvents();

        for (Event event : events) {
            LatLng x = new LatLng(event.getLatitude(), event.getLongitude());

            Marker markerX = map.addMarker(new MarkerOptions()
                    .position(x)
                    .icon(BitmapDescriptorFactory.defaultMarker(DataCache.getInstance().getColorMap().get(event.getEventType().toLowerCase()))));
            markerX.setTag(event.getEventID());
        }

        if (init != null) {
            map.animateCamera(CameraUpdateFactory.newLatLng(init));
            Event event = DataCache.getInstance().getEventMap().get(eventID);
            Person person = DataCache.getInstance().getPeopleMap().get(event.getPersonID());

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
        }
    }
}
