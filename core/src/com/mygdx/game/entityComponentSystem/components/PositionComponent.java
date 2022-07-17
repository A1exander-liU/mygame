package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;

import java.util.Random;

// component are only "data bags" (no logic/methods/actions, just data)
public class PositionComponent implements Component {
    public float x;
    public float y;
    public float oldX;
    public float oldY;
    public float futureX;
    public float futureY;
    public Vector2 position = new Vector2();
    public Vector2 oldPosition = new Vector2();
}
