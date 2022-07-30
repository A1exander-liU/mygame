package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.mygdx.game.entityComponentSystem.EnemyState;
import com.mygdx.game.entityComponentSystem.MobEntity;

public class EnemyStateMachine extends StackStateMachine<MobEntity, EnemyState> implements Component {
    private Entity enemy;
    StateComponent stateComponent;
    // provides logic to modify state of entity

    public EnemyStateMachine(Entity entity) {
        enemy = entity;
        stateComponent = entity.getComponent(StateComponent.class);
    }

    @Override
    public void update() {}

    public void changeState(EnemyState newState) {
        stateComponent.state = newState;
    }

    @Override
    public boolean revertToPreviousState() {return false;}

    @Override
    public void setInitialState(EnemyState state) {
        stateComponent.state = state;
    }

    @Override
    public void setGlobalState(EnemyState state) {}


    public EnemyState getCurrentState() {
        return stateComponent.state;
    }

    @Override
    public boolean isInState(EnemyState state) {
        return stateComponent.state == state;
    }
}

