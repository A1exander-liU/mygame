package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Objects;
import java.util.Random;

public class EnemyMovementSystem extends IntervalSystem {
    private ImmutableArray<Entity> entities;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public EnemyMovementSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(1, 1);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
    }

    @Override
    public void addedToEngine(Engine engine) {
        /* selecting all entities which have a position and speed
         * basically saying movement system for entities that have both position and speed component */
        entities = root.engine.getEntitiesFor(Family.all(EntitySprite.class, Position.class, Speed.class, Size.class).get());
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    protected void updateInterval() {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            moveEnemy(entity, getRandomDirection());
            keepEntityInsideMap(entity);
            resolveCollisions(entity);
            updateEntityInMap(entity);
        }
    }

    private String getRandomDirection() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        Random random = new Random();
        return directions[random.nextInt(directions.length)];
    }

    private void moveEnemy(Entity entity, String direction) {
        Position pos = cg.getPosition(entity);
        Speed speed = cg.getSpeed(entity);
        pos.oldX = pos.x;
        pos.oldY = pos.y;
        switch (direction) {
            case "N":
                pos.y += speed.ySpeed;
                break;
            case "NE":
                pos.x += speed.xSpeed;
                pos.y += speed.ySpeed;
                break;
            case "E":
                pos.x += speed.xSpeed;
                break;
            case "SE":
                pos.x += speed.xSpeed;
                pos.y -= speed.ySpeed;
                break;
            case "S":
                pos.y -= speed.ySpeed;
                break;
            case "SW":
                pos.x -= speed.xSpeed;
                pos.y -= speed.ySpeed;
                break;
            case "W":
                pos.x -= speed.xSpeed;
                break;
            case "NW":
                pos.x -= speed.xSpeed;
                pos.y += speed.ySpeed;
                break;
        }
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
        System.out.println("(" + pos.x + ", " + pos.y + ")");
        MapObjects objects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
        for (int i = 0; i < objects.getCount() - 1; i++) {
            Rectangle collisionZone = null;
            if (!Objects.equals(objects.get(i).getName(), "" + id.ID)) {
                if (objects.get(i) instanceof RectangleMapObject) {
                    collisionZone = ((RectangleMapObject) objects.get(i)).getRectangle();
                }
                if (objects.get(i) instanceof TextureMapObject) {
                    TextureRegion textureRegion = ((TextureMapObject) objects.get(i)).getTextureRegion();
                    float objX = textureRegion.getRegionX();
                    float objY = textureRegion.getRegionY();
                    float objWidth = textureRegion.getRegionWidth();
                    float objHeight = textureRegion.getRegionHeight();
                    collisionZone = new Rectangle(objX, objY, objWidth, objHeight);
                }
                Rectangle currentEntity = getEntityArea(objects.get("" + id.ID));
                if (collisionZone != null && currentEntity.overlaps(collisionZone)) {
                    System.out.println("there was a collision!");
                    pos.x = pos.oldX;
                    pos.y = pos.oldY;
                }
            }
        }
    }

    private Rectangle getEntityArea(MapObject mapObject) {
        TextureMapObject textureMapObject = (TextureMapObject) mapObject;
        TextureRegion textureRegion = textureMapObject.getTextureRegion();
        float objX = textureMapObject.getX();
        float objY = textureMapObject.getY();
        System.out.println(objX + ", " + objY);
        float objWidth = textureRegion.getRegionWidth();
        float objHeight = textureRegion.getRegionHeight();
        return new Rectangle(objX, objY, objWidth, objHeight);
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
