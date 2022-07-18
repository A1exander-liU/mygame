package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;

public class Enemy implements Component {
    // wander: un-aggravated, wandering inside spawn zone
    // arrive: following the player
    // flee: moving back to spawn point
    public enum States {WANDER, ARRIVE, FLEE};
    public boolean spawned = false;
    public boolean isAlive = true;
    public boolean hunting = false;
}
