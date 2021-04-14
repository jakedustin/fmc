package edu.byu.cs.familymapclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.byu.cs.familymapclient.R;

public class EventDisplayFragment extends Fragment {

    private final static String PERSON_NAME = "person_name";
    private final static String EVENT_TYPE = "event_type";
    private final static String EVENT_LOCATION = "event_location";
    private final static String GENDER  = "gender";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;

        if (this.getArguments().getString(GENDER).toLowerCase().equals("f")) {
            v = inflater.inflate(R.layout.fragment_event_display_female, container, false);
        }
        else {
            v = inflater.inflate(R.layout.fragment_event_display_male, container, false);
        }

        Bundle bundle = this.getArguments();
        String personName = bundle.getString(PERSON_NAME);
        String eventType = bundle.getString(EVENT_TYPE);
        String eventLocation = bundle.getString(EVENT_LOCATION);
        String gender = bundle.getString(GENDER);

        TextView mPersonName = (TextView) v.findViewById(R.id.person_name);
        mPersonName.setText(personName);

        TextView mEventType = (TextView) v.findViewById(R.id.event_type);
        mEventType.setText(eventType);

        TextView mEventLocation = (TextView) v.findViewById(R.id.event_location);
        mEventLocation.setText(eventLocation);

        return v;
    }
}
