package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

// component are only "data bags" (no logic/methods/actions, just data)
public class Position implements Component {
    public float x;
    public float y;
    public float oldX;
    public float oldY;
    public Vector2 position = new Vector2();
    public Vector2 oldPosition = new Vector2();
}
