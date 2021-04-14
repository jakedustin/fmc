package edu.byu.cs.familymapclient.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

import Models.Event;
import edu.byu.cs.familymapclient.Architecture.DataCache;
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
        Fragment mapFragment = new MapFragment(eventID);
        mapFragment.setArguments(args);
        ft.add(R.id.fragment_container, mapFragment).commit();
    }
}
