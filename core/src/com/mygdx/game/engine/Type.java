package com.mygdx.game.engine;

public enum Type {
    MAIN       (1),
    OFF        (2),
    ACCESSORY  (3),
    HEAD       (4),
    TORSO      (5),
    LEG        (6),
    FEET       (7),
    CONSUMABLE (8),
    MATERIAL   (9);

    int rank;

    public int getRank() {
        return rank;
    }

    Type(int rank) {
        this.rank = rank;
    }
}
