package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;

// component are only "data bags" (no logic/methods/actions, just data)
public class Position implements Component {
    public float oldX = 0f;
    public float oldY = 0f;
    public float x = 0f;
    public float y = 0f;
}
