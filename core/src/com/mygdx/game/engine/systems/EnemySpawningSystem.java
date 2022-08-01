package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.SpawnArea;
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
        makeSpawnAreaEntities();
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

    private void initialSpawn() {
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            // the name of enemy to spawn
            String name = spawnPoints.get(i).getName();
            // the name determines the stats it will have when built
            // building the enemy, the parameters are set too
            // all enemy entities are built
            // this also adds the entity to the map (the textureMapObject to the map)
            MobEntity mobEntity = new MobEntity(cg, root, gameMapProperties, name);
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

    private void setEntityValues(float xCenter, float yCenter, String spawnName) {
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
        // add back the enemies now
        for (MapObject mapObject : temp) {
            objects.add(mapObject);
        }
        // clear the collection
        temp = new MapObjects();
    }

    private void makeSpawnAreaEntities() {
        for (int i = 0; i < spawnPoints.getCount(); i++) {
            Rectangle spawn = ((RectangleMapObject) spawnPoints.get(i)).getRectangle();
            Entity spawnArea = new Entity();
            spawnArea.add(new Position());
            spawnArea.add(new Size());
            spawnArea.add(new SpawnArea());
            Position pos = cg.getPosition(spawnArea);
            Size size = cg.getSize(spawnArea);
            SpawnArea spawnAreaComponent = cg.getSpawnArea(spawnArea);
            pos.x = spawn.x;
            pos.y = spawn.y;
            size.width = spawn.width;
            size.height = spawn.height;
            spawnAreaComponent.xCenter = spawn.x + (spawn.width / 2);
            spawnAreaComponent.yCenter = spawn.y + (spawn.height / 2);
            MyGame.engine.addEntity(spawnArea);
        }
    }

    private void setSpawnAreaOwner(Entity entity) {
        ImmutableArray<Entity> spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        for (int i = 0; i < spawns.size(); i++) {
            
        }
    }
}
