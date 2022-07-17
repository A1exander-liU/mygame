package com.mygdx.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class GameMapProperties {
    public TiledMap tiledMap;
    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;

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
    }

    public MapLayer getMapLayer(String name) {
        return tiledMap.getLayers().get(name);
    }

    public MapLayer getMapLayer(int index) {
        return tiledMap.getLayers().get(index);
    }
}
