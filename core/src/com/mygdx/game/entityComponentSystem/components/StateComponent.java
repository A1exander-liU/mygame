package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.game.entityComponentSystem.EnemyState;
import com.mygdx.game.entityComponentSystem.MobEntity;

// state component is state only
// have system to handle the update logic of changing state of the agent
public class StateComponent implements Component {
    public EnemyState state;

    public StateComponent() {

    }

    public StateComponent(EnemyState state) {
        this.state = state;
    }
}