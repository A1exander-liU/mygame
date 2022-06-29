package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class StoredPreferences {
    public final static String MUSIC_VOLUME = "musicVolume";
    public static final String IS_MUSIC_ENABLED = "music.enabled";
    public static final String SOUND_VOLUME = "soundVolume";
    public static final String IS_SOUND_ENABLED = "sound.enabled";
    private static final String PREFS_NAME = "mygdxgame";

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public void setMusicVolume(float musicVolume) {
        getPrefs().putFloat(MUSIC_VOLUME, musicVolume);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(MUSIC_VOLUME, 0.5f);
    }

    public void setIsMusicEnabled(boolean isMusicEnabled) {
        getPrefs().putBoolean(IS_MUSIC_ENABLED, isMusicEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(IS_MUSIC_ENABLED, true);
    }

    public void setSoundVolume(float soundVolume) {
        getPrefs().putFloat(SOUND_VOLUME, soundVolume);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(SOUND_VOLUME, 0.5f);
    }

    public void setIsSoundEnabled(boolean isSoundEnabled) {
        getPrefs().putBoolean(IS_SOUND_ENABLED, isSoundEnabled);
        getPrefs().flush();
    }

    public boolean isSoundEnabled() {
        return getPrefs().getBoolean(IS_SOUND_ENABLED, true);
    }

}
