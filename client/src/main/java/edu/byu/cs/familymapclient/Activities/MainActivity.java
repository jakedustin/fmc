package edu.byu.cs.familymapclient.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.Fragments.LoginFragment;
import edu.byu.cs.familymapclient.Fragments.MapFragment;
import edu.byu.cs.familymapclient.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate method called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment; // = fm.findFragmentById(R.id.fragment_container);

        if (DataCache.getInstance().getAuthtoken() == null) {
            fragment = new LoginFragment();
        }
        else {
            fragment = new MapFragment();
        }

        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause method called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop method called");
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        Log.i(TAG, "onSaveInstanceState method called");
        super.onSaveInstanceState(outState, outPersistentState);
    }
}