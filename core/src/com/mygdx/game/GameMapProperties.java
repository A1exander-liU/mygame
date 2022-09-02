package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.engine.EntityFactory;
import com.mygdx.game.engine.Families;

public class GameMapProperties {
    public TiledMap tiledMap;
    public static int mapWidth;
    public static int mapHeight;
    public static int tileWidth;
    public static int tileHeight;
    public static String GROUND_LAYER;
    public static String ENEMY_SPAWNS;
    public static String COLLISIONS;
    public static String ENEMY_DROPS;
    public static String OBSTACLE_LAYER;
    // all the collision boxes on the map
    public ImmutableArray<Entity> staticObstacles;

    EntityFactory entityFactory;

    public GameMapProperties(TiledMap tiledMap, EntityFactory entityFactory) {
        this.tiledMap = tiledMap;
        this.entityFactory = entityFactory;
        setMapProperties();
        makeEntitiesFromCollisions();
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
        ENEMY_DROPS = tiledMap.getLayers().get(3).getName();
        OBSTACLE_LAYER = tiledMap.getLayers().get(4).getName();
    }

    public MapLayer getMapLayer(String name) {
        return tiledMap.getLayers().get(name);
    }

    private void makeEntitiesFromCollisions() {
        // will only account for non enemies
        MapObjects collisions = getMapLayer(COLLISIONS).getObjects();
        for (int i = 0; i < collisions.getCount(); i++) {
            // note: texture map objects are enemies everything else is obstacle
            // the rectangle map objects are the collision boxes defined initially in the tiled map
            if (collisions.get(i) instanceof RectangleMapObject) {
                entityFactory.makeObstacle(((RectangleMapObject) collisions.get(i)).getRectangle());
            }
        }
        staticObstacles = MyGame.engine.getEntitiesFor(Families.obstacles);
    }
}
