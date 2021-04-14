package edu.byu.cs.familymapclient.Architecture;

import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;

public class DataCache {

    private static DataCache instance;

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache() {}

    private String mAuthtoken;
    private String mPersonID;
    private String mServerHost;
    private int mServerPort;
    private String mUsername;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mGender;
    private Person[] persons;
    private Event[] events;
    private Map<String, Float> mColorMap = null;
    private Map<String, Person> mPeopleMap = null;
    private Map<String, Event> mEventMap = null;
    private Map<String, List<Person>> mChildrenMap;
    private Map<String, List<Event>> mAssociatedEventMap;

    public String getPersonID() {
        return mPersonID;
    }

    public void setPersonID(String personID) {
        mPersonID = personID;
    }

    public String getAuthtoken() {
        return mAuthtoken;
    }

    public void setAuthtoken(String authtoken) {
        mAuthtoken = authtoken;
    }

    public String getServerHost() {
        return mServerHost;
    }

    public void setServerHost(String serverHost) {
        mServerHost = serverHost;
    }

    public int getServerPort() {
        return mServerPort;
    }

    public void setServerPort(int serverPort) {
        mServerPort = serverPort;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Map<String, Float> getColorMap() {
        return mColorMap;
    }

    public void setColorMap(Map<String, Float> colors) {
        this.mColorMap = colors;
    }

    public Map<String, Person> getPeopleMap() {
        return mPeopleMap;
    }

    public void setPeopleMap(Map<String, Person> people) {
        this.mPeopleMap = people;
    }

    public Map<String, Event> getEventMap() {
        return mEventMap;
    }

    public void setEventMap(Map<String, Event> eventMap) {
        mEventMap = eventMap;
    }

    public Map<String, List<Person>> getChildrenMap() {
        return mChildrenMap;
    }

    public void setChildrenMap(Map<String, List<Person>> childrenMap) {
        mChildrenMap = childrenMap;
    }

    public Map<String, List<Event>> getAssociatedEventMap() {
        return mAssociatedEventMap;
    }

    public void setAssociatedEventMap(Map<String, List<Event>> associatedEventMap) {
        mAssociatedEventMap = associatedEventMap;
    }
}
