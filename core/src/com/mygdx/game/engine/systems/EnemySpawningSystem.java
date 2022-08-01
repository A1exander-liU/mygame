package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.JsonSearcher;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.utils.EntityTextureObject;

import java.util.Objects;

public class EnemySpawningSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private MapObjects spawnPoints;
    private MapObjects objects;
    private MapObjects temp;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    // Each spawn zone on tiled map is named with monster it spawns
    // have json file with list of enemies, the key will be the enemy name
    // loop through the spawn zones and take the name
    // use the name and build the mobEntity
    // in mobEntity will need to take the name and search json for enemy
    // after searching, need to take all stats and fill in stats
    // need a parameter component to contain all the stats

    public EnemySpawningSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(1);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
        objects = gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        // create a enemy and place at each spawn point
        // spawns are named to determine the enemy to spawn
        temp = new MapObjects();
        initialSpawn();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = MyGame.engine.getEntitiesFor(Family.all(
                Enemy.class, Sprite.class, Health.class, ID.class,
                Name.class, Position.class, Size.class, Speed.class).get());
    }

    @Override
    public void update(float deltaTime) {
//        for (int i = 0; i < entities.size(); i++) {
//            Entity entity = entities.get(i);
//            Enemy enemy = cg.getEnemy(entity);
//            if (!enemy.spawned) {
//                spawnEnemy(entity);
//                // when spawned start of as wander
//                // once player enterred spawn, change to pursise
//                // if player too far, change to return
//                // once returned back to spawn switch back to wander
//                enemy.state = Enemy.States.WANDER;
//            }
//        }
        // assign enemy to
    }

    private void spawnEnemy(Entity entity) {
        ID id = cg.getID(entity);
        Enemy enemy = cg.getEnemy(entity);
        Position pos = cg.getPosition(entity);
        Spawn spawn = cg.getSpawn(entity);
        enemy.spawned = true;
        gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects().get(0);
        Rectangle spawnArea = ((RectangleMapObject) spawnPoints.get(id.ID - 1)).getRectangle();
        float xCenter = spawnArea.x + (spawnArea.width / 2f);
        float yCenter = spawnArea.y + (spawnArea.height / 2f);
        pos.x = xCenter;
        pos.y = yCenter;
        spawn.spawnPosX = xCenter;
        spawn.spawnPosY = yCenter;
        pos.futureX = pos.x;
        pos.futureY = pos.y;
        pos.position.x = pos.x;
        pos.position.y = pos.y;
        TextureMapObject textureMapObject = (TextureMapObject) objects.get("" + id.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
    }
    // now need to reference the texture map object MapChar of entity

    private void initialSpawn() {
        // first time when loading spawns all enemies
        // the update loop just only keep spawning once enemy has been
        // dead for a certain period of time
        // build all the enemies in the world first
        // to determine what to spawn need to check if the enemy name
        // and spawn area are the same spawn area are the same

        // go through each spawn
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            // the name of enemy to spawn
            String name = spawnPoints.get(i).getName();
            // the name determines the stats it will have when built
            // building the enemy, the parameters are set too
            // all enemy entities are built
            // this also adds the entity to the map (the textureMapObject to the map)
            MobEntity mobEntity = new MobEntity(cg, root, gameMapProperties, name);
        }
        spawnEnemies();
        addBack();
    }

    private void spawnEnemies() {
        // since the enemies are already added to the map
        // no need to make more texture map objects
        // just need to set their X and Y values to their spawn
        // loop through all spawn points
        // loop through the objects
        // check if the name of the spawn point and object are the same
        // if they are the same
        // after setting remove from the objects
        // after spawning all enemies, restore the original objects

        //
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            MapObject spawn = spawnPoints.get(i);
            spawn(spawn);
        }
    }

    private void spawn(MapObject spawn) {
        for (int i = 0; i < objects.getCount(); i++) {
            Rectangle spawnArea = ((RectangleMapObject) spawn).getRectangle();
            if (objects.get(i) instanceof EntityTextureObject) {
                if (Objects.equals(spawn.getName(), objects.get(i).getName())) {
                    float xCenter = spawnArea.x + (spawnArea.width / 2);
                    float yCenter = spawnArea.y + (spawnArea.height / 2);
                    EntityTextureObject textureObject = (EntityTextureObject) objects.get(i);
                    Entity enemy = textureObject.getOwner();
                    Position pos = cg.getPosition(enemy);
                    Spawn spawnPoint = cg.getSpawn(enemy);
                    pos.x = xCenter;
                    pos.y = yCenter;
                    pos.oldX = pos.x;
                    pos.oldY = pos.y;
                    spawnPoint.spawnPosX = pos.x;
                    spawnPoint.spawnPosY = pos.y;
                    textureObject.setX(xCenter);
                    textureObject.setY(yCenter);
                    temp.add(objects.get(i));
                    objects.remove(i);
                    break;
                }
            }
        }
    }

    private void setEntityValues(float xCenter, float yCenter, String spawnName) {
        // can't do this, enemies can have the same name
        // there can be multiple of the same enemy
        // sol'n: extend TextureMapObject and add those to map instead
        // extended will have owner attribute to reference which entity it is part of
        // then when you retrieve it cast back to the subclass and grab the owner
        //
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Name name = cg.getName(entity);
            Spawn spawn = cg.getSpawn(entity);
            Position pos = cg.getPosition(entity);
            if (Objects.equals(name.name, spawnName) && spawn.spawnPosX == 0 && spawn.spawnPosY == 0) {
                // checking if they are zero (if they are zero it means a spawn was already set)
                pos.x = xCenter;
                pos.y = yCenter;
                pos.oldX = pos.x;
                pos.oldY = pos.y;
                spawn.spawnPosX = xCenter;
                spawn.spawnPosY = yCenter;
            }
        }
    }

    private void addBack() {
        for (MapObject mapObject : temp) {
            objects.add(mapObject);
        }
        temp = new MapObjects();
    }
}
