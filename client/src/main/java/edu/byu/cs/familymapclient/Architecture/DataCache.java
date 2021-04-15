package edu.byu.cs.familymapclient.Architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

    private void close() {
        instance = null;
    }

    public void logout() {
        close();
    }

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
    private ArrayList<Person> mMothersSide = new ArrayList<Person>();
    private ArrayList<Person> mFathersSide = new ArrayList<Person>();
    private ArrayList<Event> mMaleOnlyEvents = new ArrayList<Event>();
    private ArrayList<Event> mFemaleOnlyEvents = new ArrayList<Event>();
    private ArrayList<Event> mFathersSideEvents = new ArrayList<Event>();
    private ArrayList<Event> mMothersSideEvents = new ArrayList<Event>();
    private Map<String, Event> mEventMap = null;
    private Map<String, List<Person>> mChildrenMap;
    private Map<String, List<Event>> mAssociatedEventMap;
    private static final int FATHERS_SIDE = 0;
    private static final int MOTHERS_SIDE = 1;

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

    public ArrayList<Event> getMaleOnlyEvents() {
        return mMaleOnlyEvents;
    }

    public ArrayList<Event> getFemaleOnlyEvents() {
        return mFemaleOnlyEvents;
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

    public ArrayList<Event> getFathersSideEvents() {
        return mFathersSideEvents;
    }

    public ArrayList<Event> getMothersSideEvents() {
        return mMothersSideEvents;
    }

    public void setAssociatedEventMap(Map<String, List<Event>> associatedEventMap) {
        mAssociatedEventMap = associatedEventMap;
    }

    public void setMaleOnlyEvents(ArrayList<Event> maleOnlyEvents) {
        mMaleOnlyEvents = maleOnlyEvents;
    }

    public void setFemaleOnlyEvents(ArrayList<Event> femaleOnlyEvents) {
        mFemaleOnlyEvents = femaleOnlyEvents;
    }

    public void instantiateDataCache() {
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
        splitEventsByGender();
        splitPeopleBySide();
        splitEventsBySide();
    }

    private void splitEventsByGender() {
        ArrayList<Event> maleEvents = new ArrayList<Event>();
        ArrayList<Event> femaleEvents = new ArrayList<Event>();
        for (Event event : DataCache.getInstance().getEvents()) {
            if (DataCache.getInstance().getPeopleMap().get(event.getPersonID()).getGender().equals("m")) {
                maleEvents.add(event);
            }
            else {
                femaleEvents.add(event);
            }
        }

        DataCache.getInstance().setMaleOnlyEvents(maleEvents);
        DataCache.getInstance().setFemaleOnlyEvents(femaleEvents);
    }

    private void splitPeopleBySide() {
        Person rootPerson = this.getPeopleMap().get(getPersonID());
        mMothersSide.add(rootPerson);
        if (rootPerson.getMotherID() != null) {
            Person mother = this.getPeopleMap().get(rootPerson.getMotherID());
            mMothersSide.add(mother);
            addParents(mother.getPersonID(), MOTHERS_SIDE);
        }

        mFathersSide.add(rootPerson);
        if (rootPerson.getFatherID() != null) {
            Person father = this.getPeopleMap().get(rootPerson.getFatherID());
            mFathersSide.add(father);
            addParents(father.getPersonID(), FATHERS_SIDE);
        }
    }

    private void splitEventsBySide() {
        for (Person person : mFathersSide) {
            for (Event event : events) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    mFathersSideEvents.add(event);
                }
            }
        }

        for (Person person : mMothersSide) {
            for (Event event : events) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    mMothersSideEvents.add(event);
                }
            }
        }
    }

    private void addParents(String personID, int side) {
        switch(side) {
            case MOTHERS_SIDE:
                if (this.getPeopleMap().get(personID).getMotherID() != null) {
                    Person mother = this.getPeopleMap().get(this.getPeopleMap().get(personID).getMotherID());
                    mMothersSide.add(mother);
                    addParents(mother.getPersonID(), MOTHERS_SIDE);
                }
                if (this.getPeopleMap().get(personID).getFatherID() != null) {
                    Person father = this.getPeopleMap().get(this.getPeopleMap().get(personID).getFatherID());
                    mMothersSide.add(father);
                    addParents(father.getPersonID(), MOTHERS_SIDE);
                }
                break;
            case FATHERS_SIDE:

                if (this.getPeopleMap().get(personID).getMotherID() != null) {
                    Person mother = this.getPeopleMap().get(this.getPeopleMap().get(personID).getMotherID());
                    mFathersSide.add(mother);
                    addParents(mother.getPersonID(), FATHERS_SIDE);
                }
                if (this.getPeopleMap().get(personID).getFatherID() != null) {
                    Person father = this.getPeopleMap().get(this.getPeopleMap().get(personID).getFatherID());
                    mFathersSide.add(father);
                    addParents(father.getPersonID(), FATHERS_SIDE);
                }
                break;
        }
    }

    private void getAssociatedEvents() {
        Map<String, List<Event>> eventMap = new HashMap<String, List<Event>>();
        for (Person person : DataCache.getInstance().getPersons()) {
            eventMap.put(person.getPersonID(), new ArrayList<Event>());
        }

        for (Event event : DataCache.getInstance().getRelevantEvents()) {
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

    public ArrayList<Event> getRelevantEvents() {
        Set<Event> relevantEvents = new HashSet<Event>();
        if (Settings.getInstance().isFilterByMaleEvents()) {
            relevantEvents.addAll(DataCache.getInstance().getMaleOnlyEvents());
        }
        if (Settings.getInstance().isFilterByFemaleEvents()) {
            relevantEvents.addAll(DataCache.getInstance().getFemaleOnlyEvents());
        }
        if (Settings.getInstance().isFilterByFathersSide()) {
            relevantEvents.addAll(DataCache.getInstance().getFathersSideEvents());
        }
        if (Settings.getInstance().isFilterByMothersSide()) {
            relevantEvents.addAll(DataCache.getInstance().getMothersSideEvents());
        }

        return new ArrayList<Event>(relevantEvents);
    }

}
