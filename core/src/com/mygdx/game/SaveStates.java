package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveStates {
    public static final String SLOT_ONE = "slotOne";
    public static final String SLOT_TWO = "slotTwo";
    public static final String SLOT_THREE = "slotThree";
    public static final String PREFS_NAME = "saveStates";

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public void setSlotOneData(String serializedData) {
        getPrefs().putString(SLOT_ONE, serializedData);
        getPrefs().flush();
    }

    public String getSlotOneSerializedData() {
        return getPrefs().getString(SLOT_ONE, "");
    }
}
