package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.GameMapProperties;

import java.util.Random;

// component are only "data bags" (no logic/methods/actions, just data)
public class Position implements Component {
    public float x;
    public float y;
    public float futureX = x;
    public float futureY = y;
}
