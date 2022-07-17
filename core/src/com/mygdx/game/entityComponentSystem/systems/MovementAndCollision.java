package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
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
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.CameraComponent;
import com.mygdx.game.entityComponentSystem.components.EnemyComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.PlayerComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

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
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = root.engine.getEntitiesFor(Family.all(
                SpriteComponent.class,
                PositionComponent.class,
                SizeComponent.class,
                SpeedComponent.class,
                IDComponent.class).get());
        enemies = root.engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        player = root.engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
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
        playerMovement();
        keepEntityInsideMap(player);
        handlePlayerCollisions();
        updatePlayerCamPosition();
//        for (int i = 0; i < entities.size(); i++) {
//            Entity entity = entities.get(i);
//            keepEntityInsideMap(entity);
//            if (cg.getEnemy(entity) != null)
//                keepEntityInsideSpawnZone(entity);
//            resolveCollisions(entity);
//            updatePlayerCamPosition();
//            updateEntityInMap(entity);
//        }
    }

    private void moveEnemy(String direction, Entity entity) {
        PositionComponent pos = cg.getPosition(entity);
        SpeedComponent speedComponent = cg.getSpeed(entity);
//        if (Gdx.input.isKeyPressed(Input.Keys.UP))
//            pos.y += speed.ySpeed;
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//            pos.x += speed.xSpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//            pos.y -= speed.ySpeed;
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//            pos.x -= speed.xSpeed;
        switch (direction) {
            case "N":
                pos.futureY += speedComponent.ySpeed;
                break;
            case "NE":
                pos.futureX += speedComponent.xSpeed;
                pos.futureY += speedComponent.ySpeed;
                break;
            case "E":
                pos.futureX += speedComponent.xSpeed;
                break;
            case "SE":
                pos.futureX += speedComponent.xSpeed;
                pos.futureY -= speedComponent.ySpeed;
                break;
            case "S":
                pos.futureY -= speedComponent.ySpeed;
                break;
            case "SW":
                pos.futureX -= speedComponent.xSpeed;
                pos.futureY -= speedComponent.ySpeed;
                break;
            case "W":
                pos.futureX -= speedComponent.xSpeed;
                break;
            case "NW":
                pos.futureX -= speedComponent.xSpeed;
                pos.futureY += speedComponent.ySpeed;
                break;
        }
    }

    private String getRandomDirection() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        Random random = new Random();
        return directions[random.nextInt(directions.length)];
    }

    private void playerMovement() {
        PositionComponent pos = cg.getPosition(player);
        SpeedComponent speedComponent = cg.getSpeed(player);
        // old pos is position before move change
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            pos.y += speedComponent.ySpeed;
//            pos.oldX = pos.x;
            pos.oldY = pos.y - speedComponent.ySpeed;
//            pos.futureY += speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            pos.x += speedComponent.xSpeed;
            pos.oldX = pos.x - speedComponent.xSpeed;
//            pos.oldY = pos.y;
//            pos.futureX += speed.xSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pos.y -= speedComponent.ySpeed;
//            pos.oldX = pos.x;
            pos.oldY = pos.y + speedComponent.ySpeed;
//            pos.futureY -= speed.ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pos.x -= speedComponent.xSpeed;
            pos.oldX = pos.x + speedComponent.xSpeed;
//            pos.oldY = pos.y;
//            pos.futureX -= speed.xSpeed;
        }
    }

    private void keepEntityInsideMap(Entity entity) {
        PositionComponent pos = cg.getPosition(entity);
        SizeComponent sizeComponent = cg.getSize(entity);
        // checks if future move is legal or not
        if (pos.futureX < 0)
            pos.x = 0;
        if (pos.futureX + sizeComponent.width > gameMapProperties.mapWidth)
            pos.x = gameMapProperties.mapWidth - sizeComponent.width;
        if (pos.futureY < 0)
            pos.y = 0;
        if (pos.futureY + sizeComponent.height > gameMapProperties.mapHeight)
            pos.y = gameMapProperties.mapHeight - sizeComponent.height;
    }

    private void keepEntityInsideSpawnZone(Entity entity) {
        IDComponent idComponent = cg.getID(entity);
        PositionComponent pos = cg.getPosition(entity);
        Rectangle spawnZone = ((RectangleMapObject) spawnPoints.get(idComponent.ID - 1)).getRectangle();
        if (pos.x < spawnZone.x)
            pos.x = spawnZone.x;
        else if (pos.x > spawnZone.x + spawnZone.width)
            pos.x = spawnZone.x + spawnZone.width;
        else if (pos.y < spawnZone.y)
            pos.y = spawnZone.y;
        else if (pos.y > spawnZone.y + spawnZone.height)
            pos.y = spawnZone.y + spawnZone.height;
    }

    private void handlePlayerCollisions() {
        // in player movement, future was recorded
        IDComponent idComponent = cg.getID(player);
        PositionComponent pos = cg.getPosition(player);
        MapObjects mapObjects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon playerArea = getEntityArea(player);
        for (int i = 0; i < mapObjects.getCount(); i++) {
            if (!Objects.equals(mapObjects.get(i).getName(), "" + idComponent.ID)) {
                Polygon collisionSpace = new Polygon();
                Polyline wall = null;
                if (mapObjects.get(i) instanceof RectangleMapObject)
                    collisionSpace = getEntityArea((RectangleMapObject) mapObjects.get(i));
                if (mapObjects.get(i) instanceof TextureMapObject) {
                    // better to get the location they are currently at
                    collisionSpace = getEntityArea(mapObjects.get(i));

                    // this is getting the enemies future move
//                    int enemyID = Integer.parseInt(mapObjects.get(i).getName());
//                    Entity enemy = cg.findEntity(enemyID);
//                    collisionSpace = getEntityArea(enemy);
                }
                if (mapObjects.get(i) instanceof PolylineMapObject) {
                    wall = ((PolylineMapObject) mapObjects.get(i)).getPolyline();
                }
                boolean intersected = Intersector.overlapConvexPolygons(playerArea, collisionSpace);

                if (intersected) {
                    System.out.println("intersected");
                    pos.x = pos.oldX;
                    pos.y = pos.oldY;
                }
                if (wall != null) {
                    intersected = checkWallCollisions(wall, playerArea);
                    if (intersected) {
                        System.out.println("intersected with wall");
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
        IDComponent idComponent = cg.getID(entity);
        PositionComponent pos = cg.getPosition(entity);
        MapObjects objects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon currentEntity = getEntityArea(entity);
        for (int i = 0; i < objects.getCount(); i++) {
            Polygon collisionSpace = new Polygon();
            if (!Objects.equals(objects.get(i).getName(), "" + idComponent.ID)) {
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
                else {
                    System.out.println("intersected");
                    System.out.println(pos.futureX + ", " + pos.futureY);
                    System.out.println(pos.x + ", " + pos.y);
                    System.out.println("------------");
                }
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
        SizeComponent sizeComponent = cg.getSize(entity);
        PositionComponent pos = cg.getPosition(entity);
        float[] vertices =
                {pos.x, pos.y,
                        pos.x, pos.y + sizeComponent.height,
                        pos.x + sizeComponent.width, pos.y + sizeComponent.height,
                        pos.x + sizeComponent.width, pos.y};
        return new Polygon(vertices);
    }

    private void updatePlayerCamPosition() {
        CameraComponent cameraComponent = cg.getCamera(player);
        PositionComponent pos = cg.getPosition(player);
        cameraComponent.camera.update();
        cameraComponent.camera.position.x = pos.x;
        cameraComponent.camera.position.y = pos.y;
    }

    private void updateEntityInMap(Entity entity) {
        IDComponent idComponent = cg.getID(entity);
        PositionComponent pos = cg.getPosition(entity);
        TextureMapObject textureMapObject = (TextureMapObject) gameMapProperties.tiledMap
                .getLayers().get("Object Layer 1").getObjects().get("" + idComponent.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
    }
}
