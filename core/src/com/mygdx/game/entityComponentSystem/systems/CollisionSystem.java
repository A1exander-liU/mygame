package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.Camera;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

import java.util.Objects;

public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private MapObjects spawnPoints;
    private Entity player;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public CollisionSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(3);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = root.engine.getEntitiesFor(Family.all(
                EntitySprite.class,
                Position.class,
                Size.class,
                Speed.class,
                ID.class).get());
        player = root.engine.getEntitiesFor(Family.all(Player.class).get()).get(0);
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            keepEntityInsideMap(entity);
            if (entity.getComponent(Enemy.class) != null) {
                keepEntityInsideSpawnZone(entity);
            }
            resolveCollisions(entity);
            updatePlayerCamPosition();
            updateEntityInMap(entity);
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
        Polygon currentEntity = getEntityArea(objects.get("" + id.ID));
        for (int i = 0; i < objects.getCount(); i++) {
            Polygon collisionSpace = new Polygon();
            Rectangle collisionZone = null;
            if (!Objects.equals(objects.get(i).getName(), "" + id.ID)) {
                if (objects.get(i) instanceof RectangleMapObject) {
                    collisionZone = ((RectangleMapObject) objects.get(i)).getRectangle();
                    collisionSpace = getEntityArea(objects.get(i));
                }
                if (objects.get(i) instanceof TextureMapObject) {
                    collisionSpace = getEntityArea(objects.get(i));
                    TextureRegion textureRegion = ((TextureMapObject) objects.get(i)).getTextureRegion();
                    float objX = ((TextureMapObject) objects.get(i)).getX();
                    float objY = ((TextureMapObject) objects.get(i)).getY();
                    float objWidth = textureRegion.getRegionWidth();
                    float objHeight = textureRegion.getRegionHeight();
                    collisionZone = new Rectangle(objX, objY, objWidth, objHeight);
                }

            }
        }
    }

    private Polygon getEntityArea(MapObject mapObject) {
        TextureMapObject textureMapObject = (TextureMapObject) mapObject;
        TextureRegion textureRegion = textureMapObject.getTextureRegion();
        float mapHeight = gameMapProperties.mapHeight;
        float objX = textureMapObject.getX();
        float objY = textureMapObject.getY();
        float objWidth = textureRegion.getRegionWidth();
        float objHeight = textureRegion.getRegionHeight();
        float[] vertices =
                {objX, mapHeight - (objY + objHeight),
                 objX, objY,
                 objX + objWidth, objY,
                 objX + objWidth, mapHeight - (objY + objHeight)};
        return new Polygon(vertices);
    }

    private void keepEntityInsideSpawnZone(Entity entity) {
        ID id = cg.getID(entity);
        Position pos = cg.getPosition(entity);
        Rectangle spawnZone = ((RectangleMapObject) spawnPoints.get(id.ID - 1)).getRectangle();
        if (pos.x < spawnZone.x)
            pos.x = spawnZone.x;
        else if (pos.x > spawnZone.x + spawnZone.width)
            pos.x = spawnZone.x + spawnZone.width;
        else if (pos.y < spawnZone.y)
            pos.y = spawnZone.y;
        else if (pos.y > spawnZone.y + spawnZone.height)
            pos.y = spawnZone.y + spawnZone.height;
    }

    private void updatePlayerCamPosition() {
        Camera camera = cg.getCamera(player);
        Position pos = cg.getPosition(player);
        camera.camera.update();
        camera.camera.position.x = pos.x;
        camera.camera.position.y = pos.y;
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
