package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.EnemyState;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.PlayerEntity;
import com.mygdx.game.engine.components.EnemyStateMachine;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.SpawnArea;

public class CollisionSystem extends EntitySystem implements EntityListener {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> collisions;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> spawns;
    Entity player;
    public static World<Entity> world;

    public CollisionSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(7);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        collisions = MyGame.engine.getEntitiesFor(Families.collisions);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        world = new World<>();
        for (int i = 0; i < collisions.size(); i++) {
            Entity entity = collisions.get(i);
            addToWorld(entity);
        }
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < collisions.size(); i++) {
            Entity entity = collisions.get(i);
            com.mygdx.game.engine.components.Item item = cg.getItem(entity);
            Position pos = cg.getPosition(entity);

            // simulate movement through the world
            // later need to add item removal code in entity removal system:
            // remove from world and dereference the item of the entity

            // in spawn system:
            // need to create new entity with item component
            // add the item value of item component to world

            // userdata here is the entity itself

            CollisionFilter obstacleCollisionFilter = new CollisionFilter() {
                @Override
                public Response filter(Item item, Item other) {
                    // only enemies and players can collide into objects
                    // there is no obstacle to obstacle collision here (all obstacles are static anyway)
                    Entity itemData = (Entity) item.userData;

                    // if the item moving is a enemy or player
                    // only enemies and player can move
                    if (itemData instanceof MobEntity || itemData instanceof PlayerEntity) {
                        return Response.slide;
                    }
                    else {
                        return null;
                    }
                }
            };

            if (entity instanceof MobEntity) {
                keepEntityInsideSpawnZone(entity);
            }

            Response.Result result = world.move(item.item, pos.x, pos.y, obstacleCollisionFilter);
            if (result.projectedCollisions.size() < 1) {
                continue;
            }
            for (int j = 0; j < result.projectedCollisions.size(); j++) {
                Collision collision = result.projectedCollisions.get(j);
                Rect rect = world.getRect(collision.item);
                Entity dynamic = (Entity) collision.item.userData;
                Position itemPos = cg.getPosition(dynamic);
                itemPos.x = rect.x;
                itemPos.y = rect.y;
            }
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        addToWorld(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        removeFromWorld(entity);
    }

    private void addToWorld(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        Item<Entity> item = new Item<>(entity);
        cg.getItem(entity).item = item;
        world.add(item, pos.x, pos.y, size.width, size.height);
    }

    private void removeFromWorld(Entity entity) {
        com.mygdx.game.engine.components.Item item = cg.getItem(entity);
        world.remove(item.item);
    }

    private void keepEntityInsideSpawnZone(Entity entity) {
        // this makes the enemy warp back to spawn since hunting is set back to false when player too far
        // need to move enemy back inside spawn point first
        Position pos = cg.getPosition(entity);
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        if (stateMachine.getCurrentState() == EnemyState.IDLE) {
            Rectangle spawnZone = findEnemySpawn(entity);
            if (spawnZone != null) {
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
    }

    private Rectangle findEnemySpawn(Entity entity) {
        for (int i = 0; i < spawns.size(); i++) {
            Entity spawn = spawns.get(i);
            SpawnArea spawnArea = cg.getSpawnArea(spawn);
            if (spawnArea.owner == entity) {
                Position pos = cg.getPosition(spawn);
                Size size = cg.getSize(spawn);
                return new Rectangle(pos.x, pos.y, size.width, size.height);
            }
        }
        return null;
    }
}
