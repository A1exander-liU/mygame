package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.Camera;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Arrays;
import java.util.Objects;

public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private MapObjects spawnPoints;
    private Entity player;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public CollisionSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(3);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = root.engine.getEntitiesFor(Family.all(
                EntitySprite.class,
                Position.class,
                Size.class,
                Speed.class,
                ID.class).get());
        player = root.engine.getEntitiesFor(Family.all(Player.class).get()).get(0);
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            keepEntityInsideMap(entity);
            if (entity.getComponent(Enemy.class) != null) {
                keepEntityInsideSpawnZone(entity);
            }
            resolveCollisions(entity);
            updatePlayerCamPosition();
            updateEntityInMap(entity);
        }
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    private void keepEntityInsideMap(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        if (pos.x < 0)
            pos.x = 0;
        if (pos.x + size.width > gameMapProperties.mapWidth)
            pos.x = gameMapProperties.mapWidth - size.width;
        if (pos.y < 0)
            pos.y = 0;
        if (pos.y + size.height > gameMapProperties.mapHeight)
            pos.y = gameMapProperties.mapHeight - size.height;
    }

    private void resolveCollisions(Entity entity) {
        ID id = cg.getID(entity);
        Position pos = cg.getPosition(entity);
        MapObjects objects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        Polygon currentEntity = getEntityArea(objects.get("" + id.ID));
        for (int i = 0; i < objects.getCount(); i++) {
            Polygon collisionSpace = new Polygon();
            Rectangle collisionZone = null;
            if (!Objects.equals(objects.get(i).getName(), "" + id.ID)) {
                if (objects.get(i) instanceof RectangleMapObject) {
                    collisionZone = ((RectangleMapObject) objects.get(i)).getRectangle();
                    collisionSpace = getEntityArea(objects.get(i));
                }
                if (objects.get(i) instanceof TextureMapObject) {
                    collisionSpace = getEntityArea(objects.get(i));
                    TextureRegion textureRegion = ((TextureMapObject) objects.get(i)).getTextureRegion();
                    float objX = ((TextureMapObject) objects.get(i)).getX();
                    float objY = ((TextureMapObject) objects.get(i)).getY();
                    float objWidth = textureRegion.getRegionWidth();
                    float objHeight = textureRegion.getRegionHeight();
                    collisionZone = new Rectangle(objX, objY, objWidth, objHeight);
                }

                boolean collided = GJK(currentEntity, collisionSpace);
                if (collided) {

                }
            }
        }
    }

    private boolean GJK(Polygon s1, Polygon s2) {
        // convert the float array of vertices to Vector2 array
        Array<Vector2> s1Vectors = toVectorArray(s1);
        Array<Vector2> s2Vectors = toVectorArray(s2);

        Vector2 A;
        Vector2 B;
        Vector2 C;
        Array<Vector2> simplexPoints = new Array<>(0);

        Vector2 center1 = centerPoint(s1Vectors);
        Vector2 center2 = centerPoint(s2Vectors);
        // initial direction
        Vector2 direction = center1.sub(center2);
        if (direction.x == 0 && direction.y == 0)
            direction.x = 1;

        C = support(s1Vectors, s2Vectors, direction);
        simplexPoints.add(C);



        return false;
    }

    private Array<Vector2> toVectorArray(Polygon polygon) {
        Array<Vector2> temp = new Array<>(0);
        float[] vertices = polygon.getTransformedVertices();
        // even index = x
        // odd value = y
        for (int i = 0; i < vertices.length; i += 2) {
            temp.add(new Vector2(vertices[i], vertices[i+1]));
        }
        return temp;
    }

    private Vector2 centerPoint(Array<Vector2> vertices) {
        Vector2 averageCenter = new Vector2(0, 0);
        for (int i = 0; i < vertices.size; i++) {
            averageCenter.x += vertices.get(i).x;
            averageCenter.y += vertices.get(i).y;
        }
        averageCenter.x /= vertices.size;
        averageCenter.y /= vertices.size;
        return averageCenter;
    }

    private Vector2 support(Array<Vector2> s1Vectors, Array<Vector2> s2Vectors, Vector2 direction) {
        // getting most extreme points in opposite directions of the 2 shapes
        Vector2 p1 = getFarthestPointInDirection(s1Vectors, direction);
        Vector2 p2 = getFarthestPointInDirection(s2Vectors, negate(direction));
        // to get point on the minkowski difference (point will be an exterior point)
        return p1.sub(p2);
    }

    private Vector2 getFarthestPointInDirection(Array<Vector2> vertices, Vector2 direction) {
        /* direction is just another vector
        * it compares the location of each of the vertices to it
        * and calculates the dot product.
        * The highest dot product means that vector is the furthest
        * vector in the direction (Dot Product: dot product grows larger
        *  as the vector gets closer to the direction or magnitude increases
        * closer to direction of the vector) */
        float maxProduct = dotProduct(direction, vertices.get(0));
        int index = 0;
        for (int i = 0; i < vertices.size; i++) {
            float product = dotProduct(direction, vertices.get(i));
            if (product > maxProduct) {
                maxProduct = product;
                index = 1;
            }
        }
        return vertices.get(index);
    }

    private float dotProduct(Vector2 v1, Vector2 v2) {
        return (v1.x * v2.x) + (v1.y * v2.y);
    }

    private Vector2 negate(Vector2 vector2) {
        return new Vector2(-1 * vector2.x, -1 * vector2.y);
    }

    private Polygon getEntityArea(MapObject mapObject) {
        TextureMapObject textureMapObject = (TextureMapObject) mapObject;
        TextureRegion textureRegion = textureMapObject.getTextureRegion();
        float mapHeight = gameMapProperties.mapHeight;
        float objX = textureMapObject.getX();
        float objY = textureMapObject.getY();
        float objWidth = textureRegion.getRegionWidth();
        float objHeight = textureRegion.getRegionHeight();
        float[] vertices =
                {objX, mapHeight - (objY + objHeight),
                 objX, objY,
                 objX + objWidth, objY,
                 objX + objWidth, mapHeight - (objY + objHeight)};
        return new Polygon(vertices);
    }

    private void keepEntityInsideSpawnZone(Entity entity) {
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

    private void updatePlayerCamPosition() {
        Camera camera = cg.getCamera(player);
        Position pos = cg.getPosition(player);
        camera.camera.update();
        camera.camera.position.x = pos.x;
        camera.camera.position.y = pos.y;
    }

    private void updateEntityInMap(Entity entity) {
        ID id = cg.getID(entity);
        Position pos = cg.getPosition(entity);
        TextureMapObject textureMapObject = (TextureMapObject) gameMapProperties.tiledMap
                .getLayers().get("Object Layer 1").getObjects().get("" + id.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
    }
}
