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
import com.mygdx.game.entityComponentSystem.components.EnemyComponent;
import com.mygdx.game.entityComponentSystem.components.IDComponent;
import com.mygdx.game.entityComponentSystem.components.NameComponent;
import com.mygdx.game.entityComponentSystem.components.SpriteComponent;
import com.mygdx.game.entityComponentSystem.components.HealthComponent;
import com.mygdx.game.entityComponentSystem.components.PositionComponent;
import com.mygdx.game.entityComponentSystem.components.SizeComponent;
import com.mygdx.game.entityComponentSystem.components.SpeedComponent;

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
                EnemyComponent.class, SpriteComponent.class, HealthComponent.class, IDComponent.class,
                NameComponent.class, PositionComponent.class, SizeComponent.class, SpeedComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            EnemyComponent enemyComponent = cg.getEnemy(entity);
            if (!enemyComponent.spawned)
                spawnEnemy(entity);
        }
    }

    private void spawnEnemy(Entity entity) {
        IDComponent idComponent = cg.getID(entity);
        EnemyComponent enemyComponent = cg.getEnemy(entity);
        PositionComponent pos = cg.getPosition(entity);
        enemyComponent.spawned = true;
        gameMapProperties.tiledMap.getLayers().get("Object Layer 1").getObjects().get(0);
        Rectangle spawnArea = ((RectangleMapObject) spawnPoints.get(idComponent.ID - 1)).getRectangle();
        float xCenter = spawnArea.x + (spawnArea.width / 2f);
        float yCenter = spawnArea.y + (spawnArea.height / 2f);
        pos.x = xCenter;
        pos.y = yCenter;
        pos.futureX = pos.x;
        pos.futureY = pos.y;
        pos.position.x = pos.x;
        pos.position.y = pos.y;
        TextureMapObject textureMapObject = (TextureMapObject) objects.get("" + idComponent.ID);
        textureMapObject.setX(pos.x);
        textureMapObject.setY(pos.y);
    }
}
