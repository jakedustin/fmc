package edu.byu.cs.familymapclient.Architecture;

public class Settings {

    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        mSettingsHaveChanged = false;
        return instance;
    }

    private Settings() {}

    boolean mShowSpouseLines = false;
    boolean mShowFamilyTreeLines = false;
    boolean mShowLifeStoryLines = false;
    boolean mFilterByFathersSide = true;
    boolean mFilterByMothersSide = true;
    boolean mFilterByMaleEvents = true;
    boolean mFilterByFemaleEvents = true;

    private static boolean mSettingsHaveChanged;

    public static boolean settingsHaveChanged() {
        return mSettingsHaveChanged;
    }

    public boolean isShowSpouseLines() {
        return mShowSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        mSettingsHaveChanged = true;
        mShowSpouseLines = showSpouseLines;
    }

    public boolean isShowFamilyTreeLines() {
        return mShowFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        mSettingsHaveChanged = true;
        mShowFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowLifeStoryLines() {
        return mShowLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        mSettingsHaveChanged = true;
        mShowLifeStoryLines = showLifeStoryLines;
    }

    public boolean isFilterByFathersSide() {
        return mFilterByFathersSide;
    }

    public void setFilterByFathersSide(boolean filterByFathersSide) {
        mSettingsHaveChanged = true;
        mFilterByFathersSide = filterByFathersSide;
    }

    public boolean isFilterByMothersSide() {
        return mFilterByMothersSide;
    }

    public void setFilterByMothersSide(boolean filterByMothersSide) {
        mSettingsHaveChanged = true;
        mFilterByMothersSide = filterByMothersSide;
    }

    public boolean isFilterByMaleEvents() {
        return mFilterByMaleEvents;
    }

    public void setFilterByMaleEvents(boolean filterByMaleEvents) {
        mSettingsHaveChanged = true;
        mFilterByMaleEvents = filterByMaleEvents;
    }

    public boolean isFilterByFemaleEvents() {
        return mFilterByFemaleEvents;
    }

    public void setFilterByFemaleEvents(boolean filterByFemaleEvents) {
        mSettingsHaveChanged = true;
        mFilterByFemaleEvents = filterByFemaleEvents;
    }
}
