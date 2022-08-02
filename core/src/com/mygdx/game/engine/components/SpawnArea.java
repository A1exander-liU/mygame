package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class SpawnArea implements Component {
    public Entity owner;
    public float xCenter;
    public float yCenter;
    public float lastTimeOfDeath;

    public SpawnArea() {}
}
