package edu.byu.cs.familymapclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Architecture.Settings;
import edu.byu.cs.familymapclient.Fragments.MapFragment;
import edu.byu.cs.familymapclient.R;

public class EventActivity extends AppCompatActivity {

    private static final String EVENT_ID = "event_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EVENT_ID);
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventID);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment mapFragment;
        if (Settings.getInstance().isShowSpouseLines()) {
            mapFragment = new MapFragment(eventID, setSpouseLine());
        } else {
            mapFragment = new MapFragment(eventID);
        }
        mapFragment.setArguments(args);
        ft.add(R.id.fragment_container, mapFragment).commit();
    }

    private LatLng setSpouseLine() {
        Person thisPerson = DataCache.getInstance().getPeopleMap().get(
                DataCache.getInstance().getEventMap().get(
                        getIntent().getStringExtra(EVENT_ID)).getPersonID());

        if (thisPerson.getSpouseID() != null) {
            Person spouse = DataCache.getInstance().getPeopleMap().get(thisPerson.getSpouseID());
            if (DataCache.getInstance().getAssociatedEvents(spouse.getPersonID()).get(0) != null) {
                Event firstEvent = DataCache.getInstance().getAssociatedEvents(spouse.getPersonID()).get(0);
                return new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude());
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(EventActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }
}
