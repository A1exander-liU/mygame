package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.ID;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class SeekingSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    Entity player;

    public SeekingSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(4);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
        player = root.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        Steering playerSteering = cg.getSteering(player);
        Position playerPos = cg.getPosition(player);
        playerSteering.position.x = playerPos.x;
        playerSteering.position.y = playerPos.y;
        GdxAI.getTimepiece().update(delta);
        // checks if the enemy is hunting (aggressive towards player)
        checkIfEnemyHunting();
//        GdxAI.getTimepiece().update(delta);
        for (int i = 0; i < enemies.size(); i++) {
            // update the steering behavior of
            Entity entity = enemies.get(i);
            if (cg.getSteering(entity).steeringBehavior != null) {
                cg.getSteering(entity).update(delta);
                updateEntityInMap(entity);
            }
        }

    }

    private void checkIfEnemyHunting() {
        for (int i = 0; i < enemies.size(); i++) {
            if (cg.getEnemy(enemies.get(i)).hunting)
                cg.getSteering(enemies.get(i)).steeringBehavior = setToArrive(cg.getSteering(enemies.get(i)));
            else
                cg.getSteering(enemies.get(i)).steeringBehavior = null;
        }
    }

    private Arrive<Vector2> setToArrive(Steering steering) {
        return new Arrive<>(steering)
                .setArrivalTolerance(3f)
                .setDecelerationRadius(10f)
                .setTimeToTarget(0.1f)
                .setTarget(cg.getSteering(player));
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
