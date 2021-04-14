package edu.byu.cs.familymapclient.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Models.Event;
import Models.Person;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import edu.byu.cs.familymapclient.Architecture.DataCache;
import edu.byu.cs.familymapclient.R;
import edu.byu.cs.familymapclient.Tasks.LoginTask;
import edu.byu.cs.familymapclient.Tasks.PersonTask;
import edu.byu.cs.familymapclient.Tasks.RegisterTask;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private EditText mServerHostField;
    private EditText mServerPortField;
    private EditText mUserNameField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private CheckBox mIsMaleBox;
    private CheckBox mIsFemaleBox;
    private Button mRegisterButton;
    private Button mLoginButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate method called");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView method called");
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setButtons();
            }
        };

        mServerHostField = v.findViewById(R.id.server_host);
        mServerHostField.addTextChangedListener(textWatcher);

        mServerPortField = v.findViewById(R.id.server_port);
        mServerPortField.addTextChangedListener(textWatcher);

        mUserNameField = v.findViewById(R.id.username);
        mUserNameField.addTextChangedListener(textWatcher);

        mPasswordField = v.findViewById(R.id.password);
        mPasswordField.addTextChangedListener(textWatcher);

        mFirstNameField = v.findViewById(R.id.first_name);
        mFirstNameField.addTextChangedListener(textWatcher);

        mLastNameField = v.findViewById(R.id.last_name);
        mLastNameField.addTextChangedListener(textWatcher);

        mEmailField = v.findViewById(R.id.email);
        mEmailField.addTextChangedListener(textWatcher);

        mIsFemaleBox = v.findViewById(R.id.female_box);
        mIsFemaleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsMaleBox.isChecked()) {
                    mIsMaleBox.setChecked(false);
                }
            }
        });

        mIsMaleBox = v.findViewById(R.id.male_box);
        mIsMaleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFemaleBox.isChecked()) {
                    mIsFemaleBox.setChecked(false);
                }
            }
        });


        mRegisterButton = v.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View u) {
                // string will never be null because button is unavailable unless one of these is checked
                String gender = "";
                if (mIsFemaleBox.isChecked()) {
                    gender = "f";
                } else if (mIsMaleBox.isChecked()) {
                    gender = "m";
                }
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setUsername(mUserNameField.getText().toString());
                registerRequest.setPassword(mPasswordField.getText().toString());
                registerRequest.setEmail(mEmailField.getText().toString());
                registerRequest.setFirstName(mFirstNameField.getText().toString());
                registerRequest.setLastName(mLastNameField.getText().toString());
                registerRequest.setGender(gender);

                @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();

                        if (bundle.containsKey("REGISTER")) {
                            if (bundle.getBoolean("SUCCESS")) {
                                PersonTask personTask = new PersonTask(this, DataCache.getInstance().getAuthtoken(), DataCache.getInstance().getPersonID(), mServerHostField.getText().toString(), mServerPortField.getText().toString());
                                ExecutorService personExecutor = Executors.newSingleThreadExecutor();
                                personExecutor.submit(personTask);
                            } else {
                                Toast.makeText(getActivity(), "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (bundle.containsKey("FIRST_NAME")) {
                            String personName = bundle.getString("FIRST_NAME") + " " + bundle.getString("LAST_NAME");

                            Toast.makeText(getActivity(), personName, Toast.LENGTH_SHORT).show();
                            openMap();
                        }
                    }
                };

                RegisterTask task = new RegisterTask(uiThreadMessageHandler, registerRequest, mServerHostField.getText().toString(), mServerPortField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        mLoginButton = v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View u) {
                LoginRequest loginRequest = new LoginRequest(
                        mUserNameField.getText().toString(),
                        mPasswordField.getText().toString());

                @SuppressLint("HandlerLeak") Handler uiThreadMessageHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if (bundle.containsKey("LOGIN")) {
                            //put login stuff here
                            if (bundle.getBoolean("SUCCESS")) {
                                PersonTask personTask = new PersonTask(this, DataCache.getInstance().getAuthtoken(), DataCache.getInstance().getPersonID(), mServerHostField.getText().toString(), mServerPortField.getText().toString());
                                ExecutorService personExecutor = Executors.newSingleThreadExecutor();
                                personExecutor.submit(personTask);
                            }
                            else {
                                Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (bundle.containsKey("FIRST_NAME")) {
                            String personName = bundle.getString("FIRST_NAME") + " " + bundle.getString("LAST_NAME");
                            Toast.makeText(getActivity(), personName, Toast.LENGTH_SHORT).show();
                            openMap();
                        }
                    }
                };

                LoginTask task = new LoginTask(uiThreadMessageHandler, loginRequest, mServerHostField.getText().toString(), mServerPortField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        return v;
    }

    private void instantiateDataCache() {
        DataCache.getInstance().setColorMap(new HashMap<String, Float>());
        DataCache.getInstance().setPeopleMap(new HashMap<String, Person>());
        DataCache.getInstance().setEventMap(new HashMap<String, Event>());
        for (Event event : DataCache.getInstance().getEvents()) {
            if (!DataCache.getInstance().getColorMap().containsKey(event.getEventType().toLowerCase())) {
                double d = (double) new Random().nextInt(360);
                DataCache.getInstance().getColorMap().put(event.getEventType().toLowerCase(), (float) d);
            }
            DataCache.getInstance().getEventMap().put(event.getEventID(), event);
        }

        for (Person person : DataCache.getInstance().getPersons()) {
            DataCache.getInstance().getPeopleMap().put(person.getPersonID(), person);
        }

        getAssociatedEvents();
        getChildren();
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

    private void openMap() {
        instantiateDataCache();
        Fragment mapFragment = new MapFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean validLogin() {
        return (mServerPortField.getText().toString().length() > 0 &&
                mServerHostField.getText().toString().length() > 0 &&
                mUserNameField.getText().toString().length() > 0 &&
                mPasswordField.getText().toString().length() > 0);
    }

    private boolean validRegister() {
        return (validLogin() &&
                mFirstNameField.getText().toString().length() > 0 &&
                mLastNameField.getText().toString().length() > 0 &&
                mEmailField.getText().toString().length() > 0);
    }

    private void setButtons() {
        mLoginButton.setEnabled(validLogin());
        mRegisterButton.setEnabled(validRegister());
    }
}
