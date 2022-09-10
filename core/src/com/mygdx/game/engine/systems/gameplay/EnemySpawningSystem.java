package com.mygdx.game.engine.systems.gameplay;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.engine.systems.TimeSystem;
import com.mygdx.game.utils.map.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.ComponentGrabber;
import com.mygdx.game.engine.utils.factories.EntityFactory;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.SpawnArea;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.utils.map.EntityTextureObject;

import java.util.Objects;

public class EnemySpawningSystem extends EntitySystem {
    private final ImmutableArray<Entity> spawns;
    private final MapObjects spawnPoints;
    private final MapObjects objects;
    private MapObjects temp;
    ComponentGrabber cg;
    EntityFactory entityFactory;

    public EnemySpawningSystem(ComponentGrabber cg,
    EntityFactory entityFactory) {
        super(2);
        this.cg = cg;
        this.entityFactory = entityFactory;
        spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        spawnPoints = MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
        objects = MyGame.gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        // create a enemy and place at each spawn point
        // spawns are named to determine the enemy to spawn
        temp = new MapObjects();
        makeSpawnAreaEntities();
        initialSpawn();
    }

    @Override
    public void update(float deltaTime) {
        // this will be for future spawns when an enemy dies
        // once enemy dies they need to be removed from:
        // the engine (the entity)
        // the map (the entity texture object)

        // need spawn area to owner (the enemy spawned here)
        // when enemy dies, new one has to spawn back after certain time

        // make entities from spawn points
        // components: size, pos, and new component which will hold owner
        // owner is the entity that is spawned at that location
        // idea: loop through spawn area entities and check for ones w/o owner
        // if there is no owner that means it was removed (dead)
        // then create a timer and once it ends spawn a new enemy at the spot
        // set owner to newly spawned enemy
        for (int i = 0; i < spawns.size(); i++) {
            Entity spawn = spawns.get(i);
            SpawnArea spawnArea = cg.getSpawnArea(spawn);
            if (spawnArea.owner == null) {
                float RESPAWN_TIME = 90;
                if (TimeSystem.time - spawnArea.lastTimeOfDeath >= RESPAWN_TIME) {
                    spawn(spawn);
                }
            }
        }
    }

    private void initialSpawn() {
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            // the name of enemy to spawn
            String name = spawnPoints.get(i).getName();
            // the name determines the stats it will have when built
            // building the enemy, the parameters are set too
            // all enemy entities are built
            // this also adds the entity to the map (the textureMapObject to the map)
            entityFactory.makeEnemy(name);
        }
        // placing the enemy on the map
        spawnEnemies();
        // since map objects were temp removed, need to add them back
        addBack();
    }

    private void spawnEnemies() {
        // going through each spawn area to spawn an enemy there
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            MapObject spawn = spawnPoints.get(i);
            spawn(spawn);
        }
    }

    private void spawn(MapObject spawn) {
        for (int i = 0; i < objects.getCount(); i++) {
            Rectangle spawnArea = ((RectangleMapObject) spawn).getRectangle();
            // entity texture objects are enemies
            if (objects.get(i) instanceof EntityTextureObject) {
                // check if they have same name
                // ex. spawn area is named slime and the enemy is named slime
                // basically determining the correct enemy to spawn at each area
                if (Objects.equals(spawn.getName(), objects.get(i).getName())) {
                    // spawn them at the center of the spawn area
                    float xCenter = spawnArea.x + (spawnArea.width / 2);
                    float yCenter = spawnArea.y + (spawnArea.height / 2);
                    EntityTextureObject textureObject = (EntityTextureObject) objects.get(i);
                    Entity enemy = textureObject.getOwner();
                    Position pos = cg.getPosition(enemy);
                    Spawn spawnPoint = cg.getSpawn(enemy);
                    // setting the enemy position values
                    pos.x = xCenter;
                    pos.y = yCenter;
                    pos.oldX = pos.x;
                    pos.oldY = pos.y;
                    // setting their spawn point so they can move back here
                    spawnPoint.spawnPosX = pos.x;
                    spawnPoint.spawnPosY = pos.y;
                    textureObject.setX(xCenter);
                    textureObject.setY(yCenter);
                    // remove from objects so same enemy with same name is not used
                    // adding to temp to keep a copy
                    temp.add(objects.get(i));
                    objects.remove(i);
                    // getting the spawn area and setting the owner
                    setSpawnAreaOwner(enemy);
                    break;
                }
            }
        }
    }

    private void spawn(Entity entity) {
        Name name = cg.getName(entity);
        SpawnArea spawnArea = cg.getSpawnArea(entity);
        entityFactory.makeEnemy(name.name);
        // since it was just added, it will be the last element
        EntityTextureObject textureObject = (EntityTextureObject) objects.get(objects.getCount() - 1);
        Entity enemy = textureObject.getOwner();

        Position pos = cg.getPosition(enemy);
        Spawn spawnPoint = cg.getSpawn(enemy);
        // setting the enemy position values
        pos.x = spawnArea.xCenter;
        pos.y = spawnArea.yCenter;
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        // setting their spawn point so they can move back here
        spawnPoint.spawnPosX = pos.x;
        spawnPoint.spawnPosY = pos.y;
        textureObject.setX(pos.x);
        textureObject.setY(pos.y);
        spawnArea.owner = enemy;
//        addToWorld(enemy);
    }

    private void addBack() {
        // add back the enemies now
        for (MapObject mapObject : temp) {
            objects.add(mapObject);
        }
        // clear the collection
        temp = new MapObjects();
    }

    private void makeSpawnAreaEntities() {
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            entityFactory.makeSpawn(spawnPoints.get(i));
        }
    }

    private void setSpawnAreaOwner(Entity entity) {
        ImmutableArray<Entity> spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        Spawn spawnPoint = cg.getSpawn(entity);
        for (int i = 0; i < spawns.size(); i++) {
            Entity spawn = spawns.get(i);
            SpawnArea spawnArea = cg.getSpawnArea(spawn);
            if (spawnPoint.spawnPosX == spawnArea.xCenter && spawnPoint.spawnPosY == spawnArea.yCenter) {
                spawnArea.owner = entity;
                break;
            }
        }
    }
}
