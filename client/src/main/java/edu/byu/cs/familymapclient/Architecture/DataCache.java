package edu.byu.cs.familymapclient.Architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private Person[] persons;
    private Event[] events;
    private Map<String, Float> mColorMap = null;
    private Map<String, Person> mPeopleMap = null;
    private final ArrayList<Person> mMothersSide = new ArrayList<Person>();
    private final ArrayList<Person> mFathersSide = new ArrayList<Person>();
    private final ArrayList<Event> mFathersSideEvents = new ArrayList<Event>();
    private final ArrayList<Event> mMothersSideEvents = new ArrayList<Event>();
    private Map<String, Event> mEventMap = null;

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

    public ArrayList<Event> getFathersSideEvents() {
        return mFathersSideEvents;
    }

    public ArrayList<Event> getMothersSideEvents() {
        return mMothersSideEvents;
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

        splitPeopleBySide();
        splitEventsBySide();
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

    public ArrayList<Event> getRelevantEvents() {
        Set<Event> relevantEvents = new HashSet<Event>();
        if (Settings.getInstance().isFilterByFathersSide()) {
            relevantEvents.addAll(DataCache.getInstance().getFathersSideEvents());
        }
        if (Settings.getInstance().isFilterByMothersSide()) {
            relevantEvents.addAll(DataCache.getInstance().getMothersSideEvents());
        }
        if (!Settings.getInstance().isFilterByMaleEvents()) {
            for (Iterator<Event> iterator = relevantEvents.iterator(); iterator.hasNext();) {
                Event event = iterator.next();
                if (this.getPeopleMap().get(event.getPersonID()).getGender().equals("m")) {
                    iterator.remove();
                }
            }
        }
        if (!Settings.getInstance().isFilterByFemaleEvents()) {
            for (Iterator<Event> iterator = relevantEvents.iterator(); iterator.hasNext();) {
                Event event = iterator.next();
                if (this.getPeopleMap().get(event.getPersonID()).getGender().equals("f")) {
                    iterator.remove();
                }
            }
        }

        return new ArrayList<Event>(relevantEvents);
    }

    public List<Event> getAssociatedEvents(String personID) {
        ArrayList<Event> associatedEvents = new ArrayList<Event>();

        for (Event event : DataCache.getInstance().getRelevantEvents()) {
            if (event.getPersonID().equals(personID)) {
                associatedEvents.add(event);
            }
        }

        return associatedEvents;
    }

}
