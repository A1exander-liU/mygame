package com.mygdx.game.engine;

public enum ItemType {
    MATERIAL   (1),
    CONSUMABLE (2),
    FEET       (3),
    LEG        (4),
    TORSO      (5),
    HEAD       (6),
    ACCESSORY  (7),
    OFF        (8),
    MAIN       (9);

    int rank;

    public int getRank() {
        return rank;
    }

    ItemType(int rank) {
        this.rank = rank;
    }
}
