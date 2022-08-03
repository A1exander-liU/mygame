package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.PlayerEntity;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;

public class CollisionSystem extends EntitySystem implements EntityListener {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> collisions;
    ImmutableArray<Entity> enemies;
    Entity player;
    public static World<Entity> world;

    public CollisionSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(6);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        collisions = MyGame.engine.getEntitiesFor(Families.collisions);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
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
        entity.add(new com.mygdx.game.engine.components.Item());
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
}
