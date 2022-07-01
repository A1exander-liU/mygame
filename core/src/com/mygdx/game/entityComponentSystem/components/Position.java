package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.GameMapProperties;

import java.util.Random;

// component are only "data bags" (no logic/methods/actions, just data)
public class Position implements Component {
    Random random = new Random();
    TiledMap tiledMap = new TmxMapLoader().load("untitled.tmx");
    GameMapProperties gameMapProperties = new GameMapProperties(tiledMap);
    float randomX = random.nextInt(gameMapProperties.mapWidth - 100);
    float randomY = random.nextInt(gameMapProperties.mapHeight - 100);
    public float oldX = randomX;
    public float oldY = randomY;
    public float x = randomX;
    public float y = randomY;
}
