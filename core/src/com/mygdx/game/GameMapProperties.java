package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Steering;

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
    MyGame root;
    // all the collision boxes on the map
    public ImmutableArray<Entity> staticObstacles;

    public GameMapProperties(TiledMap tiledMap, MyGame root) {
        this.tiledMap = tiledMap;
        this.root = root;
        setMapProperties();
        makeEntitiesFromCollisions();
    }

    public GameMapProperties(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
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
        OBSTACLE_LAYER = tiledMap.getLayers().get(3).getName();
    }

    public MapLayer getMapLayer(String name) {
        return tiledMap.getLayers().get(name);
    }

    public MapLayer getMapLayer(int index) {
        return tiledMap.getLayers().get(index);
    }

    private void makeEntitiesFromCollisions() {
        // will only account for non enemies
        MapObjects collisions = getMapLayer(COLLISIONS).getObjects();
        for (int i = 0; i < collisions.getCount(); i++) {
            // note: texture map objects are enemies everything else is obstacle
            // the rectangle map objects are the collision boxes defined initially in the tiled map
            if (collisions.get(i) instanceof RectangleMapObject) {
                Rectangle area = ((RectangleMapObject) collisions.get(i)).getRectangle();
                Entity obstacle = new Entity();
                obstacle.add(new Position());
                obstacle.add(new Size());
                setPosition(obstacle, area);
                setSize(obstacle, area);
                obstacle.add(new Steering(obstacle));
//                obstacle.add(new DetectionProximity(obstacle, 20, root));
                root.engine.addEntity(obstacle);
            }
        }
        staticObstacles = root.engine.getEntitiesFor(Families.obstacles);
    }

    private void setPosition(Entity entity, Rectangle area) {
        Position pos = entity.getComponent(Position.class);
        pos.x = area.x;
        pos.y = area.y;
        pos.oldX = area.x;
        pos.oldY = area.y;
    }

    private void setSize(Entity entity, Rectangle area) {
        Size size = entity.getComponent(Size.class);
        size.width = area.width;
        size.height = area.height;
    }
}
