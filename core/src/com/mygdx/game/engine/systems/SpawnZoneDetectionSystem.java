package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Spawn;

public class SpawnZoneDetectionSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    Entity player;
    MapObjects spawnZones;
    ImmutableArray<Entity> enemies;

    public SpawnZoneDetectionSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(5);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        player = root.engine.getEntitiesFor(Families.player).get(0);
        spawnZones = gameMapProperties.getMapLayer(gameMapProperties.ENEMY_SPAWNS).getObjects();
        enemies = root.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        Polygon playerArea = getEntityArea(player);
        for (int i = 0; i < spawnZones.getCount(); i++) {
            // spawn zones are all rectangles
            Polygon spawnZone = getEntityArea((RectangleMapObject) spawnZones.get(i));
            boolean crossedSpawnZone = Intersector.overlapConvexPolygons(playerArea, spawnZone);
            if (crossedSpawnZone) {
                Entity entity = searchForEntity(spawnZones.get(i));
                if (cg.getEnemy(entity).state != Enemy.States.RETURN) {
                    cg.getEnemy(entity).state = Enemy.States.PURSUE;
                }
            }
        }

        for (int i = 0; i < spawnZones.getCount(); i++) {
            int id = i + 1;
            Rectangle rect = ((RectangleMapObject) spawnZones.get(i)).getRectangle();
            Entity entity = findEntity(rect);
            if (cg.getEnemy(entity).state == Enemy.States.PURSUE) {
                if (distanceFromSpawnZone(spawnZones.get(i)) > 400) {
                    System.out.println("too far");
                    cg.getEnemy(entity).state = Enemy.States.RETURN;
                }
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            if (cg.getEnemy(entity).state == Enemy.States.RETURN) {
                checkIfReturnedToSpawn(entity);
            }
        }
    }

    private void checkIfReturnedToSpawn(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        Spawn spawn = cg.getSpawn(entity);
        Rectangle enemy = new Rectangle(
                pos.x, pos.y, size.width, size.height
        );
        Rectangle spawnArea = getSpawnArea(spawn);
        if (spawnArea != null)
            if (spawnArea.contains(enemy))
                cg.getEnemy(entity).state = Enemy.States.WANDER;
    }

    private Rectangle getSpawnArea(Spawn spawn) {
        for (int i = 0; i < spawnZones.getCount(); i++) {
            Rectangle spawnArea = ((RectangleMapObject) spawnZones.get(i)).getRectangle();
            float xCenter = spawnArea.x + (spawnArea.width / 2f);
            float yCenter = spawnArea.y + (spawnArea.height / 2f);
            if (xCenter == spawn.spawnPosX && yCenter == spawn.spawnPosY)
                return spawnArea;
        }
        return null;
    }

    public Polygon getEntityArea(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        float[] vertices = {
                pos.x, pos.y,
                pos.x, pos.y + size.height,
                pos.x + size.width, pos.y + size.height,
                pos.x + size.width, pos.y
        };
        return new Polygon(vertices);
    }

    public Polygon getEntityArea(RectangleMapObject rectangleMapObject) {
        Rectangle rect = rectangleMapObject.getRectangle();
        float[] vertices = {
                rect.x, rect.y,
                rect.x, rect.y + rect.height,
                rect.x + rect.width, rect.y + rect.height,
                rect.x + rect.width, rect.y
        };
        return new Polygon(vertices);
    }

    private float distanceFromSpawnZone(MapObject spawnZone) {
        Rectangle rect = ((RectangleMapObject) spawnZone).getRectangle();
        Vector2 spawnZoneCenter = new Vector2(
                rect.x + rect.width / 2,
                rect.y + rect.height / 2
        );
        Position pos = cg.getPosition(player);
        Vector2 playerPosition = new Vector2(pos.x, pos.y);
        return playerPosition.dst(spawnZoneCenter);
    }

    private Entity searchForEntity(MapObject spawnZone) {
        // based on using the spawn component
        // calculates the spawn area's center
        // uses center to find the entity
        Rectangle spawnArea = ((RectangleMapObject) spawnZone).getRectangle();
        float spawnCenterX = spawnArea.x + spawnArea.width / 2;
        float spawnCenterY = spawnArea.y + spawnArea.height / 2;
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            Spawn spawn = cg.getSpawn(entity);
            if (spawn.spawnPosX == spawnCenterX && spawn.spawnPosY == spawnCenterY)
                return enemies.get(i);
        }
        return null;
    }

    private Entity findEntity(Rectangle rect) {
        float spawnX = rect.x + (rect.width / 2);
        float spawnY = rect.y + (rect.height / 2);
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            Spawn spawn = cg.getSpawn(entity);
            if (spawnX == spawn.spawnPosX && spawnY == spawn.spawnPosY)
                return entity;
        }
        return null;
    }
}