package edu.byu.cs.familymapclient.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Models.Event;
import Models.Person;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.R;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    private static final String PERSON_ID = "person_id";
    private static final String EVENT_ID = "event_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String query;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            RecyclerView recyclerView = findViewById(R.id.RecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
            List<Person> matchingPersons = searchPersons(query);
            List<Event> matchingEvents = searchEvents(query);

            SearchAdapter adapter = new SearchAdapter(matchingPersons, matchingEvents);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Person> mPeople;
        private final List<Event> mEvents;

        SearchAdapter(List<Person> persons, List<Event> events) {
            this.mPeople = persons;
            this.mEvents = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < mPeople.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.list_item_person, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.list_item_event, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < mPeople.size()) {
                holder.bind(mPeople.get(position));
            }
            else {
                holder.bind(mEvents.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mPeople.size() + mEvents.size();
        }
    }

    private class SearchViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int viewType;

        private final TextView name;
        private final TextView location;
        private final TextView date;

        private Person person;
        private Event event;

        public SearchViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.list_item_person_name);
                location = itemView.findViewById(R.id.list_item_person_relationship);
                date = null;
            }
            else {
                name = itemView.findViewById(R.id.list_item_event_type);
                location = itemView.findViewById(R.id.list_item_event_location);
                date = itemView.findViewById(R.id.list_item_event_date);
            }
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
            location.setText("");
        }

        private void bind(Event event) {
            this.event = event;
            name.setText(event.getEventType());
            location.setText(event.getCity() + ", " + event.getCountry());
            date.setText(Integer.toString(event.getYear()));
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PERSON_ID, person.getPersonID());
            }
            else {
                intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EVENT_ID, event.getEventID());
            }
            startActivity(intent);
        }
    }

    private List<Person> searchPersons(String query) {
        List<Person> matchingPersons = new ArrayList<Person>();
        for (Person person : DataCache.getInstance().getPersons()) {
            if ((person.getFirstName() + " " + person.getLastName()).toLowerCase().contains(query.toLowerCase())) {
                matchingPersons.add(person);
            }
        }

        return matchingPersons;
    }

    private List<Event> searchEvents(String query) {
        List<Event> matchingEvents = new ArrayList<Event>() {};
        for (Event event : DataCache.getInstance().getEvents()) {
            if (event.getCity().toLowerCase().contains(query.toLowerCase())
                || event.getCountry().toLowerCase().contains(query.toLowerCase())
                || event.getEventType().toLowerCase().contains(query.toLowerCase())
                || Integer.toString(event.getYear()).contains(query)) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    //need to get search results (search field?)
    // for item in persons / events
    // if item.contains(string)
    // add item to results and display
    //build recyclerview out of the search results
    // split recyclerview into two, one for events and one for people (people first)
}
