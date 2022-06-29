package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Objects;

public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public CollisionSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(2);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = root.engine.getEntitiesFor(Family.all(
                EntitySprite.class,
                Health.class,
                Position.class,
                Size.class,
                Speed.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size() - 1; i++) {
            Entity entity = entities.get(i);
            keepEntityInsideMap(entity);
            resolveCollisions(entity);
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
        for (int i = 0; i < objects.getCount() - 1; i++) {
            MapProperties objectProperties = objects.get(i).getProperties();
            if (!Objects.equals(objects.get(i).getName(), "" + id.ID)) {
                float objX = objectProperties.get("x", Float.class);
                float objY = objectProperties.get("y", Float.class);
                float objWidth = objectProperties.get("width", Float.class);
                float objHeight = objectProperties.get("height", Float.class);
                Rectangle collisionArea = new Rectangle(objX, objY, objWidth, objHeight);
                Rectangle currentEntity = getEntityArea(objects.get("" + id.ID));
                if (currentEntity.overlaps(collisionArea)) {
                    pos.x = pos.oldX;
                    pos.y = pos.oldY;
                }
            }
        }
    }

    private Rectangle getEntityArea(MapObject mapObject) {
        MapProperties entityProperties = mapObject.getProperties();
        float objX = entityProperties.get("x", Float.class);
        float objY = entityProperties.get("y", Float.class);
        float objWidth = entityProperties.get("width", Float.class);
        float objHeight = entityProperties.get("height", Float.class);
        return new Rectangle(objX, objY, objWidth, objHeight);
    }
}
