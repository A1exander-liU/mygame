package com.mygdx.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.Iterator;

public class GameMapProperties {
    public TiledMap tiledMap;
    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;
    public static String GROUND_LAYER;
    public static String ENEMY_SPAWNS;
    public static String COLLISIONS;
    public static String OBSTACLE_LAYER;

    public GameMapProperties(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        setMapProperties();
    }

    private void setMapProperties() {
        MapProperties mapProps = tiledMap.getProperties();
        int xCells = mapProps.get("width", Integer.class);
        int yCells = mapProps.get("height", Integer.class);
        tileWidth = mapProps.get("tilewidth", Integer.class);
        tileHeight = mapProps.get("tileheight", Integer.class);
        mapWidth = xCells * tileWidth;
        mapHeight = yCells * tileHeight;
        // are in order from bottom most layer to top most layer
        // order is defined from looking at the actual tiled map layers
        GROUND_LAYER = tiledMap.getLayers().get(0).getName();
        ENEMY_SPAWNS = tiledMap.getLayers().get(1).getName();
        COLLISIONS = tiledMap.getLayers().get(2).getName();
        OBSTACLE_LAYER = tiledMap.getLayers().get(3).getName();
    }

    public MapLayer getMapLayer(String name) {
        return tiledMap.getLayers().get(name);
    }

    public MapLayer getMapLayer(int index) {
        return tiledMap.getLayers().get(index);
    }
}
