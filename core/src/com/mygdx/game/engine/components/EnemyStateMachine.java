package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.mygdx.game.engine.utils.EnemyState;

public class EnemyStateMachine extends StackStateMachine<Entity, EnemyState> implements Component {
    public Entity entity;
    // provides logic to modify state of entity

    public EnemyStateMachine(Entity entity) {
        super(entity, EnemyState.IDLE);
    }

    @Override
    public void update() {
        // call update and call update of current state to run current state
        getCurrentState().update(super.owner);
    }

    public void changeState(EnemyState newState) {
        StateComponent stateCom = super.owner.getComponent(StateComponent.class);
        stateCom.state = newState;
    }

    @Override
    public boolean revertToPreviousState() {return false;}

    @Override
    public void setInitialState(EnemyState state) {
        StateComponent stateCom = super.owner.getComponent(StateComponent.class);
        stateCom.state = state;
    }

    @Override
    public void setGlobalState(EnemyState state) {}


    public EnemyState getCurrentState() {
        StateComponent stateComponent = super.owner.getComponent(StateComponent.class);
        return stateComponent.state;
    }

    @Override
    public boolean isInState(EnemyState state) {
        StateComponent stateComponent = super.owner.getComponent(StateComponent.class);
        return stateComponent.state == state;
    }
}

