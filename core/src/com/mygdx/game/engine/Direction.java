package com.mygdx.game.engine;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
    NORTH     (new Vector2(0, 1)),
    NORTHEAST (new Vector2(1, 1)),
    EAST      (new Vector2(1, 0)),
    SOUTHEAST (new Vector2(1, -1)),
    SOUTH     (new Vector2(0, -1)),
    SOUTHWEST (new Vector2(-1, -1)),
    WEST      (new Vector2(-1, 0)),
    NORTHWEST (new Vector2(-1, 1));

    Vector2 direction;
    Direction(Vector2 direction) {
        this.direction = direction;
    }
}
