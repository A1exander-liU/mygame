package com.mygdx.game.utils.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Response;
import com.mygdx.game.engine.utils.entities.MobEntity;
import com.mygdx.game.engine.systems.gameplay.movement.CollisionSystem;

import java.util.ArrayList;

public class GameRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {
    Entity owner;

    public GameRaycastCollisionDetector(Entity owner) {
        this.owner = owner;
    }

    @Override
    public boolean collides(Ray<Vector2> ray) {
        return findCollision(null ,ray);
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
        // need to look for possible overlaps against all map objects
        // the player
        // other enemies
        // obstacles
        // loop through collision layer and check overlaps
        return thereIsCollision(outputCollision, inputRay);
    }

    private boolean thereIsCollision(Collision<Vector2> outputCollision, Ray<Vector2> ray) {
        ArrayList<ItemInfo> items = new ArrayList<>();
        Vector2 start = ray.start;
        Vector2 end = ray.end;

        CollisionFilter rayFilter = new CollisionFilter() {
            @Override
            public Response filter(Item item, Item other) {
                // check if it is checking against an enemy
                if (item.userData instanceof MobEntity) {
                    // make sure it doesn't do collision with owner of the ray
                    if (item.userData == owner)
                        return null;
                    return Response.cross;
                }
                return null;
            }
        };

        // query the world for anything that collides with the given ray
        items = CollisionSystem.world.queryRayWithCoords(
                start.x, start.y, end.x, end.y,
                rayFilter,
                items);

        for (int i = 0; i < items.size(); i++) {
            ItemInfo info = items.get(i);
            Vector2 point = new Vector2(info.ti1, info.ti2);
            Vector2 normal = new Vector2(info.x1, info.y1);
            outputCollision.set(point, normal);
        }
        // make sure there was a collision
        return items.size() > 0;
    }
}
