package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;

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
        spawnZones = gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
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
                System.out.println("entered spawn zone");
                // id is for the enemy
                int id = i + 1;
                Entity entity = cg.findEntity(id);
                Enemy enemy = cg.getEnemy(entity);
                if (enemy != null) {
                    enemy.hunting = true;
                    enemy.state = Enemy.States.PURSUE;
                }

            }
        }
        for (int i = 0; i < spawnZones.getCount(); i++) {
            int id = i + 1;
            Entity entity = cg.findEntity(id);
            if (cg.getEnemy(entity).hunting) {
//                System.out.println(distanceFromSpawnZone(spawnZones.get(i)));
                if (distanceFromSpawnZone(spawnZones.get(i)) > 400) {
                    System.out.println("too far");
//                    cg.getEnemy(entity).hunting = false;
                    // to far from spawn make enemy stop hunting
                    // now we need to make the enemy move back to their spawn
                    // need to make sure the enemy is back at spawn before changing hunting
                    GameLocation spawnPoint = new GameLocation();
                    spawnPoint.setPosition(
                            cg.getSpawn(entity).spawnPosX,
                            cg.getSpawn(entity).spawnPosY);
                    cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity), spawnPoint)
                            .setArrivalTolerance(3f)
                            .setDecelerationRadius(10f)
                            .setTimeToTarget(0.1f);
//                    cg.getEnemy(entity).hunting = false;
                }
            }
        }
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
}
