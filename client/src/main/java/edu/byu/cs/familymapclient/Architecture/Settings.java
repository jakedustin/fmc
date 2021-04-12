package edu.byu.cs.familymapclient.Architecture;

public class Settings {

    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    private Settings() {}

    boolean mShowSpouseLines;
    boolean mShowFamilyTreeLines;
    boolean mShowLifeStoryLines;
    boolean mFilterByFathersSide;
    boolean mFilterByMothersSide;
    boolean mFilterByMaleEvents;
    boolean mFilterByFemaleEvents;

    public boolean isShowSpouseLines() {
        return mShowSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        mShowSpouseLines = showSpouseLines;
    }

    public boolean isShowFamilyTreeLines() {
        return mShowFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        mShowFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowLifeStoryLines() {
        return mShowLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        mShowLifeStoryLines = showLifeStoryLines;
    }

    public boolean isFilterByFathersSide() {
        return mFilterByFathersSide;
    }

    public void setFilterByFathersSide(boolean filterByFathersSide) {
        mFilterByFathersSide = filterByFathersSide;
    }

    public boolean isFilterByMothersSide() {
        return mFilterByMothersSide;
    }

    public void setFilterByMothersSide(boolean filterByMothersSide) {
        mFilterByMothersSide = filterByMothersSide;
    }

    public boolean isFilterByMaleEvents() {
        return mFilterByMaleEvents;
    }

    public void setFilterByMaleEvents(boolean filterByMaleEvents) {
        mFilterByMaleEvents = filterByMaleEvents;
    }

    public boolean isFilterByFemaleEvents() {
        return mFilterByFemaleEvents;
    }

    public void setFilterByFemaleEvents(boolean filterByFemaleEvents) {
        mFilterByFemaleEvents = filterByFemaleEvents;
    }
}