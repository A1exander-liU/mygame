package com.mygdx.game;

public enum CharAttributes {
    // different values affixes can have
    // attributeName is for displaying affix (ex. 4 Strength)
    STR (0, "Strength"),
    VIT (0, "Vitality"),
    DEX (0, "Dexterity");

    int value;
    String attributeName;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    CharAttributes(int value, String attributeName) {
        this.value = value;
        this.attributeName = attributeName;
    }
}
