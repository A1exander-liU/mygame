package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.StateComponent;


public class StateSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;

    ImmutableArray<Entity> enemies;

    public StateSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(5);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            // update the state of entity depending on logic
            //IDLE: all enemies are initially wandering
        }
    }
}
