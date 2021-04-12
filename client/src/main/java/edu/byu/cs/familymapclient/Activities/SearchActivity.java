package edu.byu.cs.familymapclient.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //need to get search results (search field?)
    // for item in persons / events
    // if item.contains(string)
    // add item to results and display
    //build recyclerview out of the search results
    // split recyclerview into two, one for events and one for people (people first)
}
