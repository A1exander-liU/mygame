package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.utils.EntityTextureObject;

import java.util.Objects;
import java.util.Random;

public class MovementAndCollision extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> enemies;
    private MapObjects spawnPoints;
    private Entity player;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public MovementAndCollision(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(2);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
        entities = MyGame.engine.getEntitiesFor(Families.collisions);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        // there is only one player hence we just get index 0
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        // does not actually move
        // makes a pretend move and checks if that spot is ok
        // if spot is ok, the actual x and y is set to future x and y
        // if spot is not ok. nothing happens to the x and y
//        for (int i = 0; i < enemies.size(); i++) {
//            Entity entity = enemies.get(i);
//            moveEnemy(getRandomDirection(), entity);
//        }
        // once char moves update their x and y
        // 200, 200
        // 200, 205 (0,5)
        // (200,200) - (0,5) = (200,200)

        // get player move
        // at this point each enemy and player has their future move
        playerMovement();
        // now we can detect and resolve any collisions
        // *will need to implement broad phase detection in future*
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            if (cg.getEnemy(entity) != null)
                keepEntityInsideSpawnZone(entity);
//            updateEntityInMap(entity);
//            handleCollisions(entity);
        }
//        updateEntityInMap(player);
//        handlePlayerCollisions();
//        updatePlayerCamPosition();
    }

    private void moveEnemy(String direction, Entity entity) {
        Position pos = cg.getPosition(entity);
        Speed speed = cg.getSpeed(entity);
//        if (Gdx.input.isKeyPressed(Input.Keys.UP))
//            pos.y += speed.ySpeed;
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//            pos.x += speed.xSpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//            pos.y -= speed.ySpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//            pos.x -= speed.xSpeed;
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        switch (direction) {
            case "N":
                pos.y += speed.ySpeed;
//                pos.oldY = pos.y - speed.ySpeed;
                break;
            case "NE":
                pos.x += speed.xSpeed;
                pos.y += speed.ySpeed;
//                pos.oldX = pos.x - speed.xSpeed;
//                pos.oldY = pos.y - speed.ySpeed;
                break;
            case "E":
                pos.x += speed.xSpeed;
//                pos.oldX = pos.x - speed.xSpeed;
                break;
            case "SE":
                pos.x += speed.xSpeed;
                pos.y -= speed.ySpeed;
//                pos.oldX = pos.x - speed.xSpeed;
//                pos.oldY = pos.y + speed.ySpeed;
                break;
            case "S":
                pos.y -= speed.ySpeed;
//                pos.oldY = pos.y + speed.ySpeed;
                break;
            case "SW":
                pos.x -= speed.xSpeed;
                pos.y -= speed.ySpeed;
//                pos.oldX = pos.x + speed.xSpeed;
//                pos.oldY = pos.y + speed.ySpeed;
                break;
            case "W":
                pos.x -= speed.xSpeed;
//                pos.oldX = pos.x + speed.xSpeed;
                break;
            case "NW":
                pos.x -= speed.xSpeed;
                pos.y += speed.ySpeed;
//                pos.oldX = pos.x + speed.xSpeed;
//                pos.oldY = pos.y - speed.ySpeed;
                break;
        }
    }

    private String getRandomDirection() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        Random random = new Random();
        return directions[random.nextInt(directions.length)];
    }

    private void playerMovement() {
        Position pos = cg.getPosition(player);
        Speed speed = cg.getSpeed(player);
        // old pos is position before move change
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            pos.y += speed.ySpeed;
//            pos.oldY = pos.y - speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            pos.x += speed.xSpeed;
//            pos.oldX = pos.x - speed.xSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pos.y -= speed.ySpeed;
//            pos.oldY = pos.y + speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pos.x -= speed.xSpeed;
//            pos.oldX = pos.x + speed.xSpeed;
        }
    }

    private void keepEntityInsideMap(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        // checks if future move is legal or not

        if (pos.futureX < 0)
            pos.x = 0;
        if (pos.futureX + size.width > gameMapProperties.mapWidth)
            pos.x = gameMapProperties.mapWidth - size.width;
        if (pos.futureY < 0)
            pos.y = 0;
        if (pos.futureY + size.height > gameMapProperties.mapHeight)
            pos.y = gameMapProperties.mapHeight - size.height;
    }

    private void keepEntityInsideSpawnZone(Entity entity) {
        // this makes the enemy warp back to spawn since hunting is set back to false when player too far
        // need to move enemy back inside spawn point first
        if (cg.getEnemy(entity).state == Enemy.States.WANDER) {
            ID id = cg.getID(entity);
            Position pos = cg.getPosition(entity);
            Rectangle spawnZone = ((RectangleMapObject) spawnPoints.get(id.ID - 1)).getRectangle();
            if (pos.x < spawnZone.x)
                pos.x = spawnZone.x;
            else if (pos.x > spawnZone.x + spawnZone.width)
                pos.x = spawnZone.x + spawnZone.width;
            else if (pos.y < spawnZone.y)
                pos.y = spawnZone.y;
            else if (pos.y > spawnZone.y + spawnZone.height)
                pos.y = spawnZone.y + spawnZone.height;
        }
    }

    private void handleCollisions(Entity entity) {
        ID id = cg.getID(entity);
        Position pos = cg.getPosition(entity);
        MapObjects mapObjects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon playerArea = getEntityArea(entity);
        // loop through each map object to see if entity collides with any of them
        for (int i = 0; i < mapObjects.getCount(); i++) {
            if (!Objects.equals(mapObjects.get(i).getName(), "" + id.ID)) {
                Polygon collisionSpace = new Polygon();
                Polyline wall = null;
                // resolving the different sub classes
                // creating polygon from the map objects
                if (mapObjects.get(i) instanceof RectangleMapObject)
                    collisionSpace = getEntityArea((RectangleMapObject) mapObjects.get(i));
                if (mapObjects.get(i) instanceof TextureMapObject) {
                    collisionSpace = getEntityArea(mapObjects.get(i));
                }
                if (mapObjects.get(i) instanceof PolylineMapObject) {
                    wall = ((PolylineMapObject) mapObjects.get(i)).getPolyline();
                }

                // passing them to static method to determine if the two mapObjects collide
                boolean intersected = Intersector.overlapConvexPolygons(playerArea, collisionSpace);
                boolean intersectedWithBorder = false;
                // checking if there was a collision with map border
                if (wall != null)
                    intersectedWithBorder = checkWallCollisions(wall, playerArea);

                if (intersected || intersectedWithBorder) {
                    pos.x = pos.oldX;
                    pos.y = pos.oldY;
                }
                // updates their position
                updateEntityInMap(entity);
            }
        }
    }

    private void handlePlayerCollisions() {
        // in player movement, future was recorded
        Name name = cg.getName(player);
        Position pos = cg.getPosition(player);
        MapObjects mapObjects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon playerArea = getEntityArea(player);
        for (int i = 0; i < mapObjects.getCount(); i++) {
            // not using id anymore, need to check something else
            // check if they are the same entity
            if (!Objects.equals(mapObjects.get(i).getName(), name.name)) {
                Polygon collisionSpace = new Polygon();
                Polyline wall = null;
                if (mapObjects.get(i) instanceof RectangleMapObject)
                    collisionSpace = getEntityArea((RectangleMapObject) mapObjects.get(i));
                if (mapObjects.get(i) instanceof TextureMapObject) {
                    // better to get the location they are currently at
                    collisionSpace = getEntityArea(mapObjects.get(i));
//                    collisionSpace = null;

                    // this is getting the enemies future move
//                    int enemyID = Integer.parseInt(mapObjects.get(i).getName());
//                    Entity enemy = cg.findEntity(enemyID);
//                    collisionSpace = getEntityArea(enemy);
                }
                if (mapObjects.get(i) instanceof PolylineMapObject) {
                    wall = ((PolylineMapObject) mapObjects.get(i)).getPolyline();
                }

                boolean intersected = false;
                intersected = Intersector.overlapConvexPolygons(playerArea, collisionSpace);

                if (intersected) {
                    pos.x = pos.oldX;
                    pos.y = pos.oldY;
                }

                if (wall != null) {
                    intersected = checkWallCollisions(wall, playerArea);
                    if (intersected) {
                        pos.x = pos.oldX;
                        pos.y = pos.oldY;
                    }
                }
                updateEntityInMap(player);
            }
        }
    }

    private boolean checkWallCollisions(Polyline wall, Polygon dynamicObject) {
        // probaly don't need just make length 1 rectangles surrounding border of the map
        float[] polylineVertices = wall.getTransformedVertices();
        // start bottom right corner then counter clock wise
        Array<Vector2> vertices = toVector2Array(polylineVertices);
//        for (int i = 0; i < vertices.size; i++) {
//            Vector2 point1 = vertices.get(i);
//            Vector2 point2;
//            if (i >= vertices.size)
//                point2 = vertices.get(0);
//            else
//                point2 = vertices.get(i+1);
//            if (Intersector.intersectLinePolygon(point1, point2, dynamicObject))
//                return true;
//        }
        float[] polygonVertices = dynamicObject.getTransformedVertices();
        for (int i = 0; i < polygonVertices.length; i+=2) {
            if (wall.contains(polygonVertices[i], polygonVertices[i+1]))
                return true;
        }
        return false;
    }

    private Array<Vector2> toVector2Array(float[] vertices) {
        Array<Vector2> temp = new Array<>(0);
        for (int i = 0; i < vertices.length; i+=2) {
            temp.add(new Vector2(vertices[i], vertices[i+1]));
        }
        return temp;
    }

    private void resolveCollisions(Entity entity) {
        ID id = cg.getID(entity);
        Position pos = cg.getPosition(entity);
        MapObjects objects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon currentEntity = getEntityArea(entity);
        for (int i = 0; i < objects.getCount(); i++) {
            Polygon collisionSpace = new Polygon();
            if (!Objects.equals(objects.get(i).getName(), "" + id.ID)) {
                if (objects.get(i) instanceof RectangleMapObject) {
                    collisionSpace = getEntityArea((RectangleMapObject) objects.get(i));
                }
                // dynamic objects: movable things like enemies
                if (objects.get(i) instanceof TextureMapObject) {
                    int enemyID = Integer.parseInt(objects.get(i).getName());
                    Entity enemy = cg.findEntity(enemyID);
                    collisionSpace = getEntityArea(enemy);
                }
                boolean intersected = Intersector.overlapConvexPolygons(currentEntity, collisionSpace);
                if (!intersected) {
//                    System.out.println("intersected");
//                    System.out.println(pos.futureX + ", " + pos.futureY);
//                    System.out.println(pos.x + ", " + pos.y);
                    pos.x = pos.futureX;
                    pos.y = pos.futureY;

                }
//                else {
//                    System.out.println("intersected");
//                    System.out.println(pos.futureX + ", " + pos.futureY);
//                    System.out.println(pos.x + ", " + pos.y);
//                    System.out.println("------------");
//                }
            }
        }
    }

    private Polygon getEntityArea(MapObject mapObject) {
        // (x,y) is at top left
        TextureMapObject textureMapObject = (TextureMapObject) mapObject;
        TextureRegion textureRegion = textureMapObject.getTextureRegion();
        float objX = textureMapObject.getX();
        float objY = textureMapObject.getY();
        float objWidth = textureRegion.getRegionWidth();
        float objHeight = textureRegion.getRegionHeight();

        float[] vertices =
                {objX, objY + objHeight,
                        objX, objY,
                        objX + objWidth, objY,
                        objX + objWidth, objY + objHeight};
        return new Polygon(vertices);
    }

    private Polygon getEntityArea(RectangleMapObject rectangleMapObject) {
        // rectangle already has (x,y) in bottom left
        Rectangle rectangle = rectangleMapObject.getRectangle();
        float x = rectangle.getX();
        float y = rectangle.getY();
        float width = rectangle.getWidth();
        float height = rectangle.getHeight();
        float[] vertices =
                {x, y,
                        x, y + height,
                        x + width, y + height,
                        x + width, y};
        return new Polygon(vertices);
    }

    private Polygon getEntityArea(Entity entity) {
        Size size = cg.getSize(entity);
        Position pos = cg.getPosition(entity);
        float[] vertices =
                {pos.x, pos.y,
                        pos.x, pos.y + size.height,
                        pos.x + size.width, pos.y + size.height,
                        pos.x + size.width, pos.y};
        return new Polygon(vertices);
    }

    private void updatePlayerCamPosition() {
        Camera camera = cg.getCamera(player);
        Position pos = cg.getPosition(player);
        camera.camera.update();
        camera.camera.position.x = pos.x;
        camera.camera.position.y = pos.y;
    }

    private void updateEntityInMap(Entity entity) {
        // not referenced by id anymore
        Position pos = cg.getPosition(entity);
        EntityTextureObject textureObject = findSameOwner(entity);
        if (textureObject != null) {
            textureObject.setX(pos.x);
            textureObject.setY(pos.y);
        }
    }

    private EntityTextureObject findSameOwner(Entity entity) {
        MapObjects collisions = gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        for (int i = 0; i < collisions.getCount(); i++) {
            if (collisions.get(i) instanceof EntityTextureObject) {
                EntityTextureObject textureObject = (EntityTextureObject) collisions.get(i);
                if (textureObject.getOwner() == entity)
                    return textureObject;
            }
        }
        return null;
    }
}
