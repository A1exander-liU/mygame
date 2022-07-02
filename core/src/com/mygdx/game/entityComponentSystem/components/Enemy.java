package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;

public class Enemy implements Component {
    public boolean spawned = false;
    public boolean isAlive = true;
}
