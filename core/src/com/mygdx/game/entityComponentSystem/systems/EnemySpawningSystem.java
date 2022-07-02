package com.mygdx.game.entityComponentSystem.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.EntitySprite;
import com.mygdx.game.entityComponentSystem.components.Health;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Name;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Speed;

public class EnemySpawningSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private MapObjects spawnPoints;
    private MapObjects objects;
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;

    public EnemySpawningSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(1);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        spawnPoints = gameMapProperties.tiledMap.getLayers().get("Enemy Spawns").getObjects();
        objects = gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = root.engine.getEntitiesFor(Family.all(
                Enemy.class, EntitySprite.class, Health.class, ID.class,
                Name.class, Position.class, Size.class, Speed.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Enemy enemy = cg.getEnemy(entity);
            if (!enemy.spawned)
                spawnEnemy(entity);
        }
    }

    private void spawnEnemy(Entity entity) {
        ID id = cg.getID(entity);
        Enemy enemy = cg.getEnemy(entity);
        Position pos = cg.getPosition(entity);
        enemy.spawned = true;
        gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects().get(0);
        Rectangle spawnArea = ((RectangleMapObject) spawnPoints.get(id.ID - 1)).getRectangle();
        float xCenter = spawnArea.x + (spawnArea.width / 2f);
        float yCenter = spawnArea.y + (spawnArea.height / 2f);
        pos.x = xCenter;
        pos.y = yCenter;
        TextureMapObject textureMapObject = (TextureMapObject) objects.get("" + id.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
    }
}
