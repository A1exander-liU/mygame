package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;

public class Enemy implements Component {
    // wander: un-aggravated, wandering inside spawn zone
    // pursue: following the player
    // return: moving back to spawn point
    public enum States {WANDER, PURSUE, RETURN};
    public States state;
    public boolean spawned = false;
    public boolean isAlive = true;
    public boolean hunting = false;
}
